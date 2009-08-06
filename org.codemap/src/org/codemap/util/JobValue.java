package org.codemap.util;

import java.util.EventObject;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import ch.akuhn.util.Arrays;
import ch.akuhn.values.Value;

/** This values computation runs as a background job in Eclipse.
 *<P>
 * This class has four states:
 *<UL>
 *<LI>ZEN, if its value is unknown and nobody ever requested it,</LI> 
 *<LI>WAITING, if its value has been requested, but the value of its parts is not yet available,</LI>
 *<LI>RUNNING, its value is being computed,</LI>
 *<LI>DONE, if its value is available.</LI>
 *</UL>
 *<P>
 * The concurrency issues of this class are tricky.
 *<P> 
 * The state of #error, {@link #job} and {@link #value} is protected by {@link #lock}.
 * The condition #hasValue can be used to wait for the end of a job.
 *<P> 
 * Jobs are started upon {@link #getValue} and {@link #awaitValue}.
 * The outcome of a job is that either value or error is set to non-null.
 * If a job is running it is reset upon {@link #valueChanged}, except for one special case. 
 * Which is as follows.
 * Running a job requests the value of all parts, but since this class is
 * a dependent of the parts, requesting these values triggers {@link #resetValue}.
 * Thus requesting an argument sets {@link #expectedSource} and if an updated from
 * this source arrives, the job is not restarted. 
 * 
 * @author Adrian Kuhn
 *
 * @param <V> type of value
 */
public abstract class JobValue<V> extends Value<V> {

    private static final int ZEN = 0;
    private static final int WAITING = 1;
    private static final int RUNNING = 2;
    private static final int DONE = 3;

    private final Lock lock = new ReentrantLock();
    private final Condition hasValue = lock.newCondition();

    private final Value<?>[] arguments;
    private final String name;

    private int state = ZEN;
    private Throwable error;
    private Computation job;


    private static final void DEBUGF(String format, Object... args) {
        if (Platform.inDevelopmentMode()) System.out.printf(format, args);
    }

    private static final Object T() {
        return Thread.currentThread().getName();
    }

    public JobValue(String name, Value<?>... arguments) {
        this.name = name;
        this.arguments = arguments;
        for (Value<?> each: arguments) each.addDependent(this); 
    }

    /** Requests and wait for the value.
     * @throws an exception if the computation failed.
     */
    @Override
    public V awaitValue() {
        DEBUGF("%s\tawait %s\n", T(), this);
        if (state == ZEN) this.requestValue();
        lock.lock();
        try {
            while (state != DONE) hasValue.awaitUninterruptibly();
            return value;
        }
        finally {
            lock.unlock();
        }
    }

    @Override
    public V getValue() {
        DEBUGF("%s\tget %s\n", T(), this);
        if (state == ZEN) this.requestValue();
        return value;
    }

    @Override
    public void setValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void valueChanged(EventObject event) {
        DEBUGF("%s\tupdate received %s from %s\n", T(), this, event.getSource());
        switch (state) {
        case ZEN:
            return;
        case WAITING:
        case RUNNING:
            job.valueChanged(event);
            return;
        case DONE:
            boolean changed = false;
            lock.lock();
            try {
                // XXX state transition from DONE to ZEN
                if (state == DONE) { 
                    DEBUGF("\t%s DONE -> ZEN\n", this);
                    state = ZEN;
                    value = null;
                    error = null;
                    job = null;
                    changed = true;
                }
            }
            finally {
                lock.unlock();
            }
            if (changed) changed();
        }
    }


    protected abstract V computeValue(JobMonitor monitor);

    private void requestValue() {
        DEBUGF("%s\trequest %s\n", T(), this);
        lock.lock();
        try {
            // XXX state transition from ZEN to WAITING
            if (state == ZEN) {
                DEBUGF("\t%s ZEN -> WAITING\n", this);
                state = WAITING;
                // value, error are already null
                job = new Computation();
            }
        }
        finally {
            lock.unlock();
        }
        job.start();
    }

    @SuppressWarnings("serial")
    private static class Break extends Error { /**/ }


    private class Computation {

        private Object[] values;

        public Computation() {
            values = new Object[arguments.length];
        }

        void valueChanged(EventObject event) {
            DEBUGF("%s\tnew value arrived %s\n", T(), this);
            if (!isNewValue(event.getSource())) return;
            switch (state) {
            case ZEN: 
                return;
            case RUNNING: // none cannonical order
                this.stop();
            case WAITING: 
                this.start();
                return;
            case DONE:
                return;
            }
        }

        private void stop() {
            lock.lock();
            try {
                // XXX state transition from RUNNING to WAITING
                if (state == RUNNING) {
                    DEBUGF("\t%s RUNNING -> WAITING\n", this);
                    state = WAITING;
                    job = null;
                }
            }
            finally {
                lock.unlock();
            }
        }

        void start() {
            DEBUGF("%s\tstarting %s\n", T(), this);
            for (int n = 0; n < values.length; n++) values[n] = arguments[n].getValue();
            for (Object each: values) if (each == null) return;
            lock.lock();
            try {
                // XXX state transition from WAITING to RUNNING
                if (state == WAITING) {
                    DEBUGF("\t%s WAITING -> RUNNING\n", this);
                    state = RUNNING;
                    // value, error, job unchanged
                    this.forkAndRunComputation();
                }
            }
            finally {
                lock.unlock();
            }
        }

        private void forkAndRunComputation() {
            (new Job(name) {
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    Computation.this.run(new M(monitor));
                    return Status.OK_STATUS;
                }

            }).schedule();
        }

        private void run(JobMonitor monitor) {
            DEBUGF("%s\trunning %s\n", T(), name);
            V newValue = computeValue(monitor);
            monitor.done();
            if (state != RUNNING) return;
            if (job != this) return;
            boolean changed = false;
            lock.lock(); 
            try {
                /// XXX state transition from RUNNING to DONE
                if (state == RUNNING) {
                    DEBUGF("\t%s RUNNING -> DONE\n", this);
                    if (job != this) return;
                    state = DONE;
                    value = newValue;
                    error = null;
                    job = null;
                    changed = true;
                    hasValue.signalAll(); // must be last, as it seems
                }
            }
            finally {
                lock.unlock();
            }
            if (changed) changed();
        }

        @SuppressWarnings("unchecked")
        private boolean isNewValue(Object eventSource) {
            if (!(eventSource instanceof Value<?>)) return false;
            Value source = (Value) eventSource;
            int index = Arrays.indexOf(arguments, source);
            return index >= 0 && !eq(values[index], source.getValue());
        }

        @Override
        public String toString() {
            String s = super.toString();
            return "Value(" + name + ", " + s.substring(s.lastIndexOf('@') + 1) + ")";
        }

        private class M implements JobMonitor {

            int index = 0;
            private IProgressMonitor monitor;
            private IProgressMonitor child;

            public M(IProgressMonitor monitor) {
                this.monitor = monitor;
                this.monitor.beginTask(name, 10000);
            }

            @Override
            public void begin(int totalWork) {
                child = new SubProgressMonitor(monitor, 10000);
            }

            @Override
            public Error cancel() {
                throw new Break();
            }

            @Override
            public void done() {
                if (child != null) child.done();
                monitor.done();
            }

            @Override
            public Error error(Throwable ex) {
                throw new Break();
            }

            @Override
            @SuppressWarnings("unchecked")
            public <A> A nextArgument() {
                return (A) values[index++];
            }

            @Override
            public void worked(int work) {
                child.worked(work);
            }

        }

    }


    @Override
    public String toString() {
        return "Value(" + name + ")";
    }



}

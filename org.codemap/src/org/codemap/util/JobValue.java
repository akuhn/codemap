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
 * The state of #error, {@link #_computation} and {@link #value} is protected by {@link #lock}.
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
public abstract class JobValue<V> extends Value<V> implements fromZenToWaiting<V> {

    private static final void DEBUGF(String format, Object... args) {
        if (Platform.inDevelopmentMode()) System.out.printf(format, args);
    }

    private static final Object T() {
        return Thread.currentThread().getName();
    }

    private final State<V> state;
    private final Value<?>[] arguments;
    private final String name;

    public JobValue(String name, Value<?>... arguments) {
        this.name = name;
        this.arguments = arguments;
        this.state = new State<V>();
        // FIXME don't leak this before construction done (see Book XXX, page XXX) 
        for (Value<?> each: arguments) each.addDependent(this); 
    }

    /** Requests and wait for the value.
     * @throws an exception if the computation failed.
     */
    @Override
    public V awaitValue() {
        DEBUGF("%s\tawait %s\n", T(), this);
        this.requestValue();
        return state.awaitValue();
    }

    @Override
    public V getValue() {
        DEBUGF("%s\tget %s\n", T(), this);
        this.requestValue();
        return state.getValue();
    }

    public void requestValue() {
        DEBUGF("%s\trequest %s\n", T(), this);
        if (state.getState() == State.ZEN) state.fromZenToWaiting();
    }

    @Override
    public void setValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "Value(" + name + ")";
    }

    @Override
    public void valueChanged(EventObject event) {
        DEBUGF("%s\tupdate received %s from %s\n", T(), this, event.getSource());
        switch (state.getState()) {
        case State.ZEN:
            return;
        case State.WAITING:
        case State.RUNNING:
            state.valueChanged(event);
            return;
        case State.DONE:
            state.fromDoneToZen();
        }
    }


    protected abstract V computeValue(JobMonitor monitor);

    @SuppressWarnings("serial")
    private static class Break extends Error { /**/ }

    private class Computation {

        private Object[] _values;
        private Job _job;

        public Computation() {
            _values = new Object[arguments.length];
        }

        @Override
        public String toString() {
            String s = super.toString();
            return "Value(" + name + ", " + s.substring(s.lastIndexOf('@') + 1) + ")";
        }

        void initialize() {
            DEBUGF("%s\tstarting %s\n", T(), this);
            for (int n = 0; n < _values.length; n++) _values[n] = arguments[n].getValue();
            for (Object each: _values) if (each == null) return;
            state.fromWaitingToRunning();
        }

        void valueChanged(EventObject event) {
            DEBUGF("%s\tnew value arrived %s\n", T(), this);
            if (!isNewValue(event.getSource())) return;
            switch (state.getState()) {
            case State.ZEN: 
                return;
            case State.RUNNING: // note, not in canonical order!
                state.fromRunningToWaiting();
            case State.WAITING: 
                this.initialize();
                return;
            case State.DONE:
                return;
            }
        }

        void start() {
            // called from guarded section in #fromWaitingToRunning only!
            (_job = new Job(name) {
                @Override
                protected IStatus run(IProgressMonitor monitor) {
                    DEBUGF("%s\trunning %s\n", T(), name);
                    V newValue = computeValue(new Monitor(monitor, name, _values));
                    monitor.done();
                    if (this != _job) return Status.CANCEL_STATUS;
                    if (state.getState() != State.RUNNING) return Status.CANCEL_STATUS;
                    state.fromRunningToDone(newValue);
                    return Status.OK_STATUS;
                }

            }).schedule();
        }

        @SuppressWarnings("unchecked")
        private boolean isNewValue(Object eventSource) {
            if (!(eventSource instanceof Value<?>)) return false;
            Value source = (Value) eventSource;
            int index = Arrays.indexOf(arguments, source);
            return index >= 0 && !eq(_values[index], source.getValue());
        }

        void stop() {
            // called from guarded section in #fromRunningToWaiting only!
            _job.cancel();
            _job = null;
        }

    }

    private static class Monitor implements JobMonitor {

        private int _index = 0;
        private IProgressMonitor _monitor;
        private IProgressMonitor _child;
        private Object[] _values;

        public Monitor(IProgressMonitor monitor, String name, Object[] values) {
            this._values = values;
            this._monitor = monitor;
            this._monitor.beginTask(name, 10000);
        }

        @Override
        public void begin(int totalWork) {
            _child = new SubProgressMonitor(_monitor, 10000);
        }

        @Override
        public Error cancel() {
            throw new Break();
        }

        @Override
        public void done() {
            if (_child != null) _child.done();
            _monitor.done();
        }

        @Override
        public Error error(Throwable ex) {
            throw new Break();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <A> A nextArgument() {
            return (A) _values[_index++];
        }

        @Override
        public void worked(int work) {
            _child.worked(work);
        }

    }



    private class State<S> {

        static final int ZEN = 0;
        static final int WAITING = 1;
        static final int RUNNING = 2;
        static final int DONE = 3;

        private final Lock lock = new ReentrantLock();
        private final Condition hasValue = lock.newCondition();
        private final Computation _computation = new Computation();

        private int _state = ZEN;
        private S _value;

        S getValue() {
            return _value;
        }
        
        @Override
        public String toString() {
            return JobValue.this.toString();
        }

        void valueChanged(EventObject event) {
            _computation.valueChanged(event);
        }

        void fromDoneToZen() {
            lock.lock();
            try {
                if (_state != DONE) return; 
                DEBUGF("\t%s DONE -> ZEN\n", this);
                _state = ZEN;
                _value = null;
            }
            finally {
                lock.unlock();
            }
            changed();
        }

        void fromRunningToDone(S newValue) {
            lock.lock(); 
            try {
                if (_state != RUNNING) return;
                DEBUGF("\t%s RUNNING -> DONE\n", this);
                _state = DONE;
                _value = newValue;
                hasValue.signalAll();
            }
            finally {
                lock.unlock();
            }
            changed();
        }

        void fromRunningToWaiting() {
            lock.lock();
            try {
                if (_state != RUNNING) return;
                DEBUGF("\t%s RUNNING -> WAITING\n", this);
                _state = WAITING;
                _computation.stop();
            }
            finally {
                lock.unlock();
            }
        }


        void fromWaitingToRunning() {
            lock.lock();
            try {
                if (_state != WAITING) return;
                DEBUGF("\t%s WAITING -> RUNNING\n", this);
                _state = RUNNING;
                _computation.start();
            }
            finally {
                lock.unlock();
            }
        }

        void fromZenToWaiting() {
            lock.lock();
            try {
                if (_state != ZEN) return;
                DEBUGF("\t%s ZEN -> WAITING\n", this);
                _state = WAITING;
            }
            finally {
                lock.unlock();
            }
            _computation.initialize();
        }

        int getState() {
            return _state;
        }

        S awaitValue() {
            lock.lock();
            try {
                while (_state != State.DONE) hasValue.awaitUninterruptibly();
                return _value;
            }
            finally {
                lock.unlock();
            }
        }

    }



}

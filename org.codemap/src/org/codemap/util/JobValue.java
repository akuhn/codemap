package org.codemap.util;

import java.util.EventObject;
import java.util.concurrent.atomic.AtomicReference;

import org.codemap.CodemapCore;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import ch.akuhn.util.Arrays;
import ch.akuhn.values.AbstractValue;
import ch.akuhn.values.Arguments;
import ch.akuhn.values.Value;
import ch.akuhn.values.ValueChangedListener;
import ch.akuhn.values.ValueOrError;
import ch.akuhn.values.Values;

public class JobValue<V> extends AbstractValue<V> implements ValueChangedListener {

    private final AtomicReference<State> fState;
    //private final BlockingQueue<ValueOrError<V>> fValue;
    private String name;
    private Value<?>[] parts;

    public JobValue(String name, Value<?>... parts) {
        this.name = name;
        this.parts = parts;
        //fValue = new ArrayBlockingQueue<ValueOrError<V>>(1);
        fState = new AtomicReference<State>(new State());
        for (Value<?> each: parts) each.addDependent(this);
    }


    public V awaitValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ValueOrError<V> getValueOrError() {
        DEBUGF("%s\tget %s\n", T(), this);
        this.requestValue();
        return fState.get().innerValueOrError();
    }

    private static void DEBUGF(String format, Object... args) {
        System.out.printf(format, args);
    }


    private static String T() {
        return Thread.currentThread().getName();
    }


    @Override
    public void setValue(V value) {
        throw new UnsupportedOperationException();
    }


    @Override
    public String toString() {
        return "Value("+name+")";
    }
    
    @Override
    public void valueChanged(EventObject event) {
        State state = fState.get();
        state.innerValueChanged(event);
    }
    
    protected V computeValue(JobMonitor monitor) { return null; }
    
    private void requestValue() {
        fState.get().innerRequestValue();
    }
    
    @SuppressWarnings("serial")
    private static class Break extends Error { /**/ }
    

    private class Done extends State {

        protected final ValueOrError<?>[] arguments;
        protected final V value;
        protected final Throwable error;
        
        private Done(V value, Throwable error, ValueOrError<?>... arguments) {
            if (arguments.length != parts.length) throw new IllegalArgumentException();
            this.value = value;
            this.error = error;
            this.arguments = arguments;
        }

        public IStatus asIStatus() {
            return error == null ? Status.OK_STATUS : new Status(IStatus.ERROR, CodemapCore.PLUGIN_ID, name, error);
        }

        @Override
        public void innerRequestValue() {
            return;
        }

        @Override
        public void innerValueChanged(EventObject event) {
            DEBUGF("%s\tvalue changed %s\n", T(), this);
            if (!newArgumentValue(event.getSource())) return;
            State newState = new State();
            if (fState.compareAndSet(this, newState)) changed();
        }
        
        @Override
        public ValueOrError<V> innerValueOrError() {
            return new ValueOrError<V>(value, error);
        }
     
        @Override
        public String toString() {
            return "Value(" + name + ", DONE)";
        }

        protected boolean newArgumentValue(Object source) {
            int index = Arrays.indexOf(parts, source);
            if (index < 0) return false;
            return !Values.eq(arguments[index], ((Value<?>) source).getValueOrError()); // safe cast
        }
        
    }
    
    private static class Monitor implements JobMonitor {

        private int _index = 0;
        private IProgressMonitor _monitor;
        private IProgressMonitor _child;
        private ValueOrError<?>[] _values;

        public Monitor(IProgressMonitor monitor, String name, ValueOrError<?>[] values) {
            if (values == null) throw new IllegalArgumentException();
            if (name == null) throw new IllegalArgumentException();
            if (monitor == null) throw new IllegalArgumentException();
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
            return (A) _values[_index++].getValue();
        }

        @Override
        public void worked(int work) {
            _child.worked(work);
        }

    }
    
    private class State {

        public void innerRequestValue() {
            DEBUGF("%s\trequest %s\n", T(), this);
            Working newState = new Working(null, new Error());
            if (!fState.compareAndSet(this, newState)) return;
            newState.start();
        }

        public void innerValueChanged(EventObject event) {
            return;
        }

        public ValueOrError<V> innerValueOrError() {
            return new ValueOrError<V>(null, new Error());
        }
        
        @Override
        public String toString() {
            return "Value(" + name + ", NONE)";
        }
        
    }


    private class Working extends Done {

        private final Runner job;

        private Working(V value, Throwable error) {
            super (value, error, new ValueOrError<?>[parts.length]);
            this.job = new Runner();
        }

        @Override
        public void innerValueChanged(EventObject event) {
            DEBUGF("%s\tvalue changed %s\n", T(), this);
            if (!newArgumentValue(event.getSource())) return;
            if (job.getState() == Job.NONE) {
                this.start();
            }
            else {
                Working newState = new Working(value, error);
                if (fState.compareAndSet(this, newState)) newState.start();
                this.stop();
            }
        }

        @Override
        public String toString() {
            return "Value(" + name + ", WORKING)";
        }

        private void start() {
            DEBUGF("%s\tstart %s\n", T(), this);
            for (int n = 0; n < parts.length; n++) arguments[n] = parts[n].getValueOrError();
            for (ValueOrError<?> each: arguments) if (each.hasError()) return;
            if (this != fState.get()) return;
            job.schedule();
        }
        
        private void stop() {
            DEBUGF("%s\tstop %s\n", T(), this);
            job.cancel();
        }

        private class Runner extends Job {
            
           public Runner() {
                super(name);
            }

            @Override
            protected IStatus run(IProgressMonitor monitor) {
                DEBUGF("%s\trun %s\n", T(), this);
                Done newState = innerRun(new Monitor(monitor, name, arguments));
                monitor.done();
                if (fState.compareAndSet(Working.this, newState)) {
                    changed();
                }
                DEBUGF("%s\tDONE %s\n", T(), this);
                return newState.asIStatus();
            }

            private Done innerRun(Monitor monitor) {
                try {
                    return new Done(computeValue(monitor), null, arguments);
                }
                catch (Throwable ex) {
                    return new Working(computeValue(monitor), null);
                }
            }
                
        }

    }
    
}

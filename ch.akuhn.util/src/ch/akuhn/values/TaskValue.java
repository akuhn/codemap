package ch.akuhn.values;

import java.util.EventObject;
import java.util.concurrent.atomic.AtomicReference;

import ch.akuhn.util.Arrays;
import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.TaskFactory.Callback;
import ch.akuhn.values.TaskFactory.Task;


@SuppressWarnings("unchecked")
public abstract class TaskValue<V> extends AbstractValue<V> implements ValueChangedListener {

    private static void DEBUGF(String format, Object... args) {
        System.out.printf(format, args);
    }
    
    private static String T() {
        return Thread.currentThread().getName();
    }
    
    
    private final AtomicReference<State> fState;
    //private final BlockingQueue<ValueOrError<V>> fValue;
    private String name;
    private Value[] parts;
    private boolean requiresAllArguments;
    
    /** Tells this value to start tasks even if not all arguments are available.
     * This skips the waiting phase. Subclasses must deal with missing or failed
     * arguments!
     *<P>
     * To be called by constructor of subclasses.
     * 
     */
    protected void doesNotRequireAllArguments() {
        this.requiresAllArguments = false;
    }

    public TaskValue(String name, Value<?>... parts) {
        this.name = name;
        this.parts = parts;
        this.requiresAllArguments = true;
        //fValue = new ArrayBlockingQueue<ValueOrError<V>>(1);
        fState = new AtomicReference<State>(new Missing(null));
        for (Value<?> each: parts) each.addDependent(this);
    }


    @Override
    public ImmutableValue<V> asImmutable() {
        this.requestValue();
        return fState.get().innerAsImmutable();
    }


    public V awaitValue() {
        throw new UnsupportedOperationException();
    }


    @Override
    public Throwable getError() {
        return this.asImmutable().getError();
    }


    @Override
    public V getValueOrFail() {
        return this.asImmutable().getValueOrFail();
    }

    @Override
    public boolean isError() {
        return this.asImmutable().isError();
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
    public V getValue() {
        return this.asImmutable().getValue();
    }
    
    @Override
    public void valueChanged(EventObject event) {
        fState.get().innerValueChanged(event);
    }
    
    protected abstract V computeValue(ProgressMonitor monitor, Arguments arguments);
    
    private void requestValue() {
        fState.get().innerRequestValue();
    }
    
    private class State {
        
        protected final Object value;
        protected final Throwable error;
        
        private State(Object value, Throwable error) {
            this.value = value;
            this.error = error;
        }

        @Override
        public String toString() {
            String basic = super.toString();
            return String.format("Value(%s, %s)", name, basic.substring(basic.lastIndexOf('$') + 1));
        }

        public ImmutableValue innerAsImmutable() {
            return new ImmutableValue(value, error);
        }

        public void innerRequestValue() {
            return;
        }

        public void innerValueChanged(EventObject event) {
            return;
        }

        protected boolean isNewArgumentValue(ImmutableValue[] arguments, Object source) {
            if (arguments == null) return true;
            int index = Arrays.indexOf(parts, source);
            if (index < 0) return false;
            // The cast to Value below is safe, only values have an index >= 0
            return !Values.equal(arguments[index], ((Value) source).asImmutable());
        }
        
    }
    
    private class Done extends State {

        protected ImmutableValue[] arguments;
        
        private Done(V value, Throwable error, ImmutableValue... arguments) {
            super(value, error);
            if (arguments.length != parts.length) throw new IllegalArgumentException();
            this.arguments = arguments;
        }

        @Override
        public void innerValueChanged(EventObject event) {
            DEBUGF("%s\tupdate received %s from %s\n", T(), this, event.getSource());
            if (isNewArgumentValue(arguments, event.getSource())) {
                State newState = new Missing(value);
                if (TaskValue.this.changeState(this, newState)) {
                    changed();
                }
            }
        }

        
    }    
    
    private static Throwable MISSING = new Error() {
      @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }  
    };
    
    private class Missing extends State {
        
        public Missing(Object value) {
            super(value, MISSING);
        }

        @Override
        public void innerRequestValue() {
            DEBUGF("%s\trequest %s\n", T(), this);
            Waiting newState = new Waiting(value, MISSING);
            if (TaskValue.this.changeState(this, newState)) {
                newState.start();
            }
        }

    }

    private class Waiting extends State {

        private ImmutableValue[] arguments;

        private Waiting(Object value, Throwable error) {
            super (value, error);
        }

        protected void start() {
            DEBUGF("%s\tstart %s\n", T(), this);
            arguments = new ImmutableValue[parts.length];
            for (int n = 0; n < parts.length; n++) 
                arguments [n] = parts[n].asImmutable();
            if (requiresAllArguments)
                for (ImmutableValue each: arguments) 
                    if (each.isError()) return;
            Working newState = new Working(value, error, arguments);
            if (TaskValue.this.changeState(this, newState)) {
                newState.start();
            }
        }
        
        @Override
        public void innerValueChanged(EventObject event) {
            DEBUGF("%s\tupdate received %s from %s\n", T(), this, event.getSource());
            if (!isNewArgumentValue(arguments, event.getSource())) return;
            this.start();
        }

        @Override
        public void innerRequestValue() {
            // this.start(); TODO figure out how to broadcast requests while waiting
            // without ending up in an endless loop upon errors 
        }
        
    }

    private static TaskFactory TASK_FACTORY = new TaskFactory();
    
    public static void setTaskFactory(TaskFactory factory) {
        TASK_FACTORY = factory;
    }
    
    private class Working extends State implements Callback {

        private Task job;
        private ImmutableValue[] arguments;

        private Working(Object value, Throwable error, ImmutableValue[] arguments) {
            super(value, error);
            job = TASK_FACTORY.makeTask();
            this.arguments = arguments;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void innerValueChanged(EventObject event) {
            DEBUGF("%s\tupdate received %s from %s\n", T(), this, event.getSource());
            if (isNewArgumentValue(arguments, event.getSource())) {
                Waiting newState = new Waiting(value, error);
                if (TaskValue.this.changeState(this, newState)) {
                    newState.start();
                }
                else {
                    // let the current state handle this update
                    TaskValue.this.fState.get().innerValueChanged(event);
                }
                this.stop();
            }
        }

        @Override // Callback
        public Throwable run(ProgressMonitor monitor) {
            DEBUGF("%s\trun %s\n", T(), this);
            monitor.setName(name);
            State newState = runTryCatch(monitor);
            monitor.done();
            if (TaskValue.this.changeState(this, newState)) {
               changed();
            }
            return newState.error;
        }
        
        private State runTryCatch(ProgressMonitor monitor) {
            try {
                return new Done(computeValue(monitor, new Arguments(arguments)), null, arguments);
            }
            catch (Throwable ex) {
                return new Waiting(value, ex);
            }
        }

        protected void start() {
            DEBUGF("%s\tstart-running %s\n", T(), this);
            if (this != fState.get()) return;
            job.start(this);
        }
 
        private void stop() {
            DEBUGF("%s\tstop %s\n", T(), this);
            job.stop();
        }
        
        @Override
        public void innerRequestValue() {
            return;
        }
 
    }
    
    private boolean changeState(State previousState, State newState) {
        DEBUGF("%s\tChanging state from %s to %s\n", T(), previousState, newState);
        if (fState.compareAndSet(previousState, newState)) {
            maybeDisposeValue((V) previousState.value, (V) newState.value);
            return true;
        }
        else {
            DEBUGF("%s\tFAIL, current state is %s\n", T(), fState);
            return false;
        }
    }

    protected void maybeDisposeValue(V value, V value2) {
        // react on changed values
    }

}

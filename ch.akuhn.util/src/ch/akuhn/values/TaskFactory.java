package ch.akuhn.values;

import ch.akuhn.util.ProgressMonitor;

public class TaskFactory {

    public Task makeTask() {
        return new Task() {

            @Override
            public void start(Callback callback) {
                callback.run(ProgressMonitor.NULL);
            }
            
            @Override
            public void stop() {
                // ignore
            }

        };
    }

    public interface Callback {
    
        public String getName();
        public Throwable run(ProgressMonitor monitor);
    
    }

    public interface Task {
    
        public void start(Callback callback);
        public void stop();
    
    }
    
    
}

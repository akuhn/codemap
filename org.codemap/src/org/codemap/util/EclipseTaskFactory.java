package org.codemap.util;

import java.util.concurrent.CancellationException;

import org.codemap.CodemapCore;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import ch.akuhn.util.ProgressMonitor;
import ch.akuhn.values.TaskFactory;

public class EclipseTaskFactory extends TaskFactory {

    static class M extends ProgressMonitor {

        private IProgressMonitor monitor;
        private String name;

        public M(String name, IProgressMonitor monitor) {
            this.name = name;
            this.monitor = monitor;
        }

        @Override
        public void begin(int total) {
            monitor.beginTask(name, total);
        }

        @Override
        public CancellationException cancel() {
            monitor.setCanceled(true);
            return new CancellationException();
        }

        @Override
        public void done() {
            monitor.done();
        }

        @Override
        protected String getName() {
            return name;
        }

        @Override
        public boolean isCanceled() {
            return monitor.isCanceled();
        }

        @Override
        public void setName(String name) {
            monitor.setTaskName(this.name = name);
        }

        @Override
        public void worked(int work) {
            monitor.worked(work);
        }

        @Override
        protected void worked(double work) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public ProgressMonitor spawn(String newName, int ticks) {
            return new M(newName, new SubProgressMonitor(monitor, ticks));
        }
        
    }

    @Override
    public Task makeTask() {
        return new T("");
    }    

    static class T extends Job implements Task {

        private Callback callback;

        public T(String name) {
            super(name);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            Throwable error = callback.run(new M(callback.getName(), monitor));
            if (monitor.isCanceled()) return Status.CANCEL_STATUS;
            if (error == null) return Status.OK_STATUS;
            return new Status(IStatus.ERROR, CodemapCore.PLUGIN_ID, callback.getName(), error);
        }

        @Override
        public void start(Callback taskCallback) {
            this.callback = taskCallback;
            this.schedule();
        }

        @Override
        public void stop() {
            this.cancel();
        }
        
    }
    
}

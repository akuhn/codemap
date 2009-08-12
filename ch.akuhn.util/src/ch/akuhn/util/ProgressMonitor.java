package ch.akuhn.util;

import java.util.concurrent.CancellationException;


public abstract class ProgressMonitor {

    public abstract boolean isCanceled();

    public abstract CancellationException cancel();

    public abstract void done();

    public void worked(int work) {
        this.worked((double) work);
    }

    protected abstract void worked(double work);

    protected abstract String getName();
    
    public abstract void setName(String name);
    
    public abstract void begin(int total);

    public ProgressMonitor spawn(String name, int ticks) {
        return new Spawn(this, name, ticks);
    }

    public static ProgressMonitor NULL = new Null();
        
    static class Null extends ProgressMonitor {

        private boolean cancelled = false;
        
        @Override
        public void begin(int total) {
            // ignore
        }

        @Override
        public CancellationException cancel() {
            cancelled = true;
            return new CancellationException();
        }

        @Override
        public void done() {
            // ignore
        }

        @Override
        public boolean isCanceled() {
            return cancelled;
        }

        @Override
        public void setName(String name) {
            // ignore
        }

        @Override
        public ProgressMonitor spawn(String name, int ticks) {
            return this;
        }

        @Override
        public void worked(double work) {
            // ignore
        }

        protected String getName() {
            return null;
        }
        
    };

    static class Spawn extends ProgressMonitor {

        private ProgressMonitor parent;
        private String parentName;
        private int beginCount = 0;
        private int parentWork;
        private double worked;
        private int total;

        public Spawn(ProgressMonitor parent, String name, int parentWork) {
            this.parent = parent;
            this.parentName = parent.getName();
            if (name != null) this.setName(name);
            if (parentWork > 0) this.parentWork = parentWork;
        }

        @Override
        public void begin(int total) {
            if (++beginCount == 1) this.total = total;
        }

        @Override
        public CancellationException cancel() {
            return parent.cancel();
        }

        @Override
        public void done() {
            if (beginCount-- == 1) {
                this.worked(total - worked);
                parent.setName(parentName);
            }
        }

        @Override
        public boolean isCanceled() {
            return parent.isCanceled();
        }

        @Override
        public void worked(double work) {
            if (total > 0 || beginCount == 1) parent.worked(work / total * parentWork);
        }

        @Override
        public void setName(String task) {
            parent.setName(task);
        }

        @Override
        public Spawn spawn(String name, int subtotal) {
            return new Spawn(this, name, subtotal);
        }

        @Override
        protected String getName() {
            return parent.getName();
        }

    }

    static class Console extends ProgressMonitor {

        private double worked;
        private boolean canceled = false;
        private String name = "Progress";
        private int prev = -1;

        Console() {
            this.worked(0); // print initial 0%
        }
        
        @Override
        public void begin(int total) {
            throw new UnsupportedOperationException();
        }

        @Override
        public CancellationException cancel() {
            canceled = true;
            return new CancellationException();
        }

        @Override
        public void done() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean isCanceled() {
            return canceled;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void worked(double work) {
            int curr = (int) (worked += work);
            if (curr == prev) return;
            System.out.printf("%5d%%\t%s\n", prev = curr, name);
        }

        @Override
        public String getName() {
            return name;
        }

    }

    public static ProgressMonitor console() {
        return new Console().spawn(null, 100);
    }
    
}

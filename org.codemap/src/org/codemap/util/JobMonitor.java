package org.codemap.util;

public interface JobMonitor {

    public void done();

    public void worked(int work);

    public void begin(int totalWork);

    public Error cancel();

    public Error error(Throwable ex);

    public <A> A nextArgument();

}
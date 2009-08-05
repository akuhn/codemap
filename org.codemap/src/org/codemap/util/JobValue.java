package org.codemap.util;

import java.util.EventObject;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import ch.akuhn.values.Value;

/** This values computation runs as a background job in Eclipse.
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

	private final Lock lock = new ReentrantLock();
	private final Condition hasValue = lock.newCondition();
	private final Value<?>[] arguments;
    private final String name;

    private Value<?> expectedSource;
    private Throwable error;
    private J job;

    private static final void DEBUGF(String format, Object... args) {
		System.out.printf(format, args);
	}
    
    public JobValue(String name, Value<?>... arguments) {
    	this.name = name;
    	this.arguments = arguments;
        for (Value<?> each: arguments) each.addDependent(this); 
    }
    
    @Override
    public V awaitValue() {
    	DEBUGF("await %s\n", name);
    	lock.lock();
    	try {
    		if (value == null && error == null && job == null) {
    			job = new J();
    			job.schedule(); 
    		}
    		hasValue.awaitUninterruptibly();
    		if (error != null) throw rethrowError(); 
    		return value;
    	}
    	finally {
    		lock.unlock();
    	}
    }

    private Error rethrowError() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public V getValue() {
    	DEBUGF("get %s\n", name);
    	lock.lock();
    	try {
    		if (value == null && error == null && job == null) {
    			job = new J();
    			job.schedule(); 
    		}
    		return value;
    	}
    	finally {
    		lock.unlock();
    	}
    }
    
 
    public void resetValue() {
    	DEBUGF("reset %s\n", name);
        lock.lock();
        try {
        	value = null;
        	error = null;
        	if (job != null) {
        		job.cancel();
    			job = new J();
    			job.schedule();
        	}
        }
        finally {
        	lock.unlock();
        }
        changed();
    }
    
    @Override
    public void setValue(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
	public void valueChanged(EventObject event) {
    	if (event.getSource() == expectedSource) return;
    	this.resetValue();
    }
    
    protected abstract V computeValue(JobMonitor monitor);

     	
	@SuppressWarnings("serial")
	private static class Break extends Error { /**/ }

	private class J extends Job {

		public J() {
			super(name);
		}

		@Override
		protected IStatus run(final IProgressMonitor monitor) {
			
			class M implements JobMonitor {

				int index = 0;
				boolean userCancled = false;
				Throwable userException = null;
		
				public void begin(int totalWork) {
					monitor.beginTask(name, totalWork);
				}
		
				public Error cancel() {
					this.userCancled = true;
					throw new Break();
				}
		
				public void done() {
					monitor.done();
				}
		
				public Error error(Throwable ex) {
					this.userException = ex;
					throw new Break();
				}
		
				@Override
				@SuppressWarnings("unchecked")
				public <A> A nextArgument() {
					expectedSource = arguments[index++];
					A argument = (A) expectedSource.awaitValue();
					expectedSource = null;
					if (argument == null) throw cancel();
					return argument;
				}

				public void worked(int work) {
					monitor.worked(work);
				}
	
			}
			
			V newValue = null;
			M moni = new M();
			
			try {
				DEBUGF("run %s\n", name);
				newValue = computeValue(moni);
			}
			catch (Break ex) {
				if (moni.userCancled) return Status.CANCEL_STATUS;
				Log.error(moni.userException);
				throw new RuntimeException(ex);
			}
			catch (Throwable ex) {
				Log.error(ex);
				throw new RuntimeException(ex);
			}
			finally {
				DEBUGF("update %s with %s (error %s)\n", name, newValue, moni.userException);
				if (!monitor.isCanceled()) {
					lock.lock();
					try {
						if (job == this) {
							value = newValue;
							error = moni.userException;
							job = null;
							hasValue.signalAll();
							lock.unlock();
							changed();
						}
					}
					finally {
						lock.unlock();
					}
				}
			}
			
			return moni.userCancled ? Status.CANCEL_STATUS : Status.OK_STATUS;
		
		}

	}
	
			
}

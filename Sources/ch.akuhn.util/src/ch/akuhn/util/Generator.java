//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>
//  
//  This file is part of ch.akuhn.util.
//  
//  ch.akuhn.util is free software: you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation, either version 3 of the License, or (at your
//  option) any later version.
//  
//  ch.akuhn.util is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
//  License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.
//  

package ch.akuhn.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An iterator that yields its values one at a time. Subclasses must define a method called {@link #run()}
 * and may call {@link yield(T)} to return values one at a time. 
 * 
 * <pre>Generator<Integer> fibonacci = new Generator<Integer>() {
 *	@Override
 *	public void run() {
 *		int a = 0, b = 1;
 *		while (true) {
 *			a = b + (b = a);
 *			yield(a);
 *		}
 *	}
 *};
 *	
 *for (int x : fibonacci) {
 *	if (x > 20000) break;
 *	out.println(x);
 *}</pre>
 *
 * @author Adrian Kuhn &lt;akuhn(at)iam.unibe.ch&gt;
 *
 */
public abstract class Generator<T> implements Iterable<T> {

	public abstract void run();

	public Iterator<T> iterator() {
		return new Iter();
	}

	private static final Object DONE = new Object();
	private static final Object EMPTY = new Object();
	private Object drop = EMPTY;
	private Thread th = null; 
	
	private synchronized Object take() {
		while (drop == EMPTY) {
			try { wait(); }
			catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}
		Object temp = drop;
		if (drop != DONE) drop = EMPTY;
		notifyAll();
		return temp;
	}
	
	private synchronized void put(Object value) {
		if (drop == DONE) throw new IllegalStateException();
		if (drop != EMPTY) throw new IllegalStateException();
		drop = value;
		notifyAll();
		while (drop != EMPTY) {
			try { wait(); }
			catch (InterruptedException ex) { 
				Thread.currentThread().interrupt();
			}
		}
	}
	
	protected void yield(T value) {
		put(value);
	}
	
	public synchronized void done() {
		if (drop == DONE) throw new IllegalStateException();
		if (drop != EMPTY) throw new IllegalStateException();
		drop = DONE;
		notifyAll();
	}	
	
	private class Iter implements Iterator<T>, Runnable {
	
		private Object next = EMPTY;

		public Iter() {
			if (th != null) throw new IllegalStateException("Can not run coroutine twice");
			th = new Thread(this);
			th.setDaemon(true);
			th.start();
		}
		
		public void run() {
			Generator.this.run();
			done();
		}
	
		public boolean hasNext() {
			if (next == EMPTY) next = take();
			return next != DONE;
		}

		@SuppressWarnings("unchecked")
		public T next() {
			if (next == EMPTY) next = take();
			if (next == DONE) throw new NoSuchElementException();
			Object temp = next;
			next = EMPTY;
			return (T) temp;
		}

		public void remove() {
			throw new UnsupportedOperationException();
			
		}
		
		@SuppressWarnings("deprecation")
		@Override
		protected void finalize() throws Throwable {
			th.stop(); // let's commit suicide
		}

	}
	
}

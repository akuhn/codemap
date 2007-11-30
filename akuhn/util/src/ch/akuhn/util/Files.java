package ch.akuhn.util;

import java.io.File;
import java.util.*;
import java.io.FileFilter;

public class Files {

	public static FileFilter endsWith(final String suffix) {
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(suffix);
			}
		};
	}

	public static  Iterable<File> files(String filename, FileFilter filter) {
		return files(new File(filename), filter);
	}
	
	public static  Iterable<File> files(String filename) {
		return files(new File(filename), null);
	}
	
	public static Iterable<File> files(final File folder, final FileFilter filter) {
		return new Iterable<File>() {

			@Override
			public Iterator<File> iterator() {
				return new Iterator<File>() {
					private Deque<File> queue = new LinkedList<File>(); 
					{ 
						queue.offer(folder); 
						processDirectories(); 
					}
					
					private void processDirectories() {
						while (!queue.isEmpty()) {
							if (!queue.peek().isDirectory()) break;
							File next = queue.poll();
							for (File each : next.listFiles()) {
								if (each.isDirectory() || filter == null || filter.accept(each)) {
									queue.offer(each);
								}
							}
						}
					}
					
					@Override
					public boolean hasNext() {
						this.processDirectories();
						return !queue.isEmpty();
					}

					@Override
					public File next() {
						this.processDirectories();
						if (queue.isEmpty()) throw new NoSuchElementException();
						return queue.poll();
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
					
				};
			}
		};	
	}		
	
	public static void main(String... argh) {
		for (File each : files(".", endsWith(".class"))) {
			System.out.println(each);
		}
	}
	
}

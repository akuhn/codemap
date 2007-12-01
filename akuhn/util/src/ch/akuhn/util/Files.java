//  Copyright (c) 1998-2007 Adrian Kuhn <akuhn(a)iam.unibe.ch>
//  
//  This file is part of "Adrian Kuhn's Utilities for Java".
//  
//  "Adrian Kuhn's Utilities for Java" is free software: you can redistribute
//  it and/or modify it under the terms of the GNU Lesser General Public License
//  as published by the Free Software Foundation, either version 3 of the
//  License, or (at your option) any later version.
//  
//  "Adrian Kuhn's Utilities for Java" is distributed in the hope that it will
//  be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
//  General Public License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with "Adrian Kuhn's Utilities for Java". If not, see
//  <http://www.gnu.org/licenses/>.
//  

package ch.akuhn.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/** Static methods that operate on or return files.
 * 
 *
 */
public class Files {

	public static FileFilter endsWith(final String suffix) {
		return new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().endsWith(suffix);
			}
		};
	}

	public static Iterable<File> files(final File folder,
			final FileFilter filter) {
		return new Iterable<File>() {

			@Override
			public Iterator<File> iterator() {
				return new Iterator<File>() {
					private Deque<File> queue = new LinkedList<File>();
					{
						queue.offer(folder);
						processDirectories();
					}

					@Override
					public boolean hasNext() {
						this.processDirectories();
						return !queue.isEmpty();
					}

					@Override
					public File next() {
						this.processDirectories();
						if (queue.isEmpty())
							throw new NoSuchElementException();
						return queue.poll();
					}

					private void processDirectories() {
						while (!queue.isEmpty()) {
							if (!queue.peek().isDirectory())
								break;
							File next = queue.poll();
							for (File each : next.listFiles()) {
								if (each.isDirectory() || filter == null
										|| filter.accept(each)) {
									queue.offer(each);
								}
							}
						}
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}

				};
			}
		};
	}

	public static Iterable<File> files(String filename) {
		return files(new File(filename), null);
	}

	public static Iterable<File> files(String filename, FileFilter filter) {
		return files(new File(filename), filter);
	}

	public static void main(String... argh) {
		for (File each : files(".", endsWith(".class"))) {
			System.out.println(each);
		}
	}

}

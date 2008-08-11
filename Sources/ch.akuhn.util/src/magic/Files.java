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

package magic;

import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Static methods that operate on or return files.
 * 
 */
public abstract class Files {

	public static FileFilter endsWith(final String suffix) {
		return new FileFilter() {

			public boolean accept(File file) {
				return file.getName().endsWith(suffix);
			}
		};
	}

	public static Iterable<File> all(final File folder, final FileFilter filter) {
		return new Iterable<File>() {

			public Iterator<File> iterator() {
				return new Iterator<File>() {
					private LinkedList<File> queue = new LinkedList<File>();
					{
						queue.offer(folder);
						processDirectories();
					}

					public boolean hasNext() {
						this.processDirectories();
						return !queue.isEmpty();
					}

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
								if (each.isDirectory() || filter == null || filter.accept(each)) {
									queue.offer(each);
								}
							}
						}
					}

					public void remove() {
						throw new UnsupportedOperationException();
					}

				};
			}
		};
	}

	public static Iterable<File> all(String filename) {
		return all(new File(filename));
	}

	public static Iterable<File> all(File file) {
		return all(file, (FileFilter) null);
	}

	public static Iterable<File> all(String filename, FileFilter filter) {
		return all(new File(filename), filter);
	}

	public static Iterable<File> all(File file, String pattern) {
		if (pattern.equals("*"))
			return all(file);
		if (pattern.lastIndexOf('*') != 0)
			throw // TODO support other patterns
			new UnsupportedOperationException("Patterns other than \"*abc\" not yet supported.");
		return all(file, endsWith(pattern.substring(1)));
	}

	public static Iterable<File> all(String filename, String pattern) {
		return all(new File(filename), pattern);
	}

	public static void main(String... argh) {
		for (File each : all(".", endsWith(".class"))) {
			System.out.println(each);
		}
	}

	public static Appendable openWrite(String filename) {
		return openWrite(new File(filename));
	}

	public static Appendable openWrite(File file) {
		try {
			return new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void close(Appendable appendable) {
		if (appendable instanceof Closeable) {
			try {
				((Closeable) appendable).close();
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public static CharSequence openRead(String filename) {
		return Strings.fromFile(filename);
	}

	public static CharSequence openRead(File file) {
		return Strings.fromFile(file);
	}

}

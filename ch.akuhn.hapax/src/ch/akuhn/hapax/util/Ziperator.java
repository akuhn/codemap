package ch.akuhn.hapax.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import ch.akuhn.hapax.util.Ziperator.Each;

/** Visits all entries in a ZIP file, including entries in nested ZIP files.
 * 
 * @author Adrian Kuhn
 *
 */
public class Ziperator implements Iterable<Each> {

	private File file;
	private boolean recurse;

	public Ziperator(String fname) {
		this(new File(fname));
	}
	
	public Ziperator(File file) {
		this.file = file;
		this.recurse();
	}

	public Ziperator recurse() {
		this.recurse = true;
		return this;
	}
	
	public Ziperator dontRecurse() {
		this.recurse = false;
		return this;
	}
	
	public static class Each {

		public Each parent;
		public ZipEntry entry;
		public InputStream in;

		public Each(Each parent, ZipEntry entry, InputStream in) {
			this.parent = parent;
			this.entry = entry;
			this.in = in;
		}
		
		@Override
		public String toString() {
			return parent + "::" + entry;
		}
		
		public boolean isSourceArchive() {
			return isArchive() && entry.getName().indexOf("src") > 0;
		}

		public boolean isArchive() {
			return entry.getName().endsWith(".zip") || entry.getName().endsWith(".jar");
		}
		
	}

	@Override
	public Iterator<Each> iterator() {
		return new Iter(file).recurse(recurse);
	}

	private static class Iter implements Iterator<Each> {

		private Each parent;
		private ZipInputStream zip;
		private Each next;
		private Iterator<Each> children;
		private boolean recurse;

		public Iter(File file) {
			this.parent = null;
			try {
				this.zip = new ZipInputStream(new FileInputStream(file));
			} catch (FileNotFoundException ex) {
				throw new RuntimeException(ex);
			}
		}

		public Iterator<Each> recurse(boolean recurse) {
			this.recurse = recurse;
			return this;
		}

		public Iter(Each parent, InputStream in) {
			this.parent = parent;
			this.zip = new ZipInputStream(in);
		}

		@Override
		public boolean hasNext() {
			try {
				if (children != null) {
					if (children.hasNext()) return true;
					children = null;
				}
				if (next != null) return true;
				ZipEntry entry = zip.getNextEntry();
				if (entry == null) return false;
				next = new Each(parent, entry, zip);
				if (next == null) return false;
				if (recurse && next.isArchive()) {
					children = new Iter(next, zip).recurse(recurse);
					return this.hasNext();
				}
				return true;
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		}

		@Override
		public Each next() {
			if (!hasNext()) throw new NoSuchElementException();
			if (next == null) return children.next();
			Each each = next;
			next = null;
			return each;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}

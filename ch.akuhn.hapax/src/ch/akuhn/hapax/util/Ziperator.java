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

import ch.akuhn.foreach.Each;

/** Visits all entries in a ZIP file, including entries in nested ZIP files.
 * 
 * @author Adrian Kuhn
 *
 */
public class Ziperator implements Iterable<Ziperator.Entry> {

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
	
	public static class Entry {

		public Entry parent;
		public ZipEntry entry;
		public InputStream in;

		public Entry(Entry parent, ZipEntry entry, InputStream in) {
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
	public Iterator<Entry> iterator() {
		return new Iter(file).recurse(recurse);
	}

	private static class Iter implements Iterator<Entry> {

		private Entry parent;
		private ZipInputStream zip;
		private Entry next;
		private Iterator<Entry> children;
		private boolean recurse;

		public Iter(File file) {
			this.parent = null;
			try {
				this.zip = new ZipInputStream(new FileInputStream(file));
			} catch (FileNotFoundException ex) {
				throw new RuntimeException(ex);
			}
		}

		public Iterator<Entry> recurse(boolean recurse) {
			this.recurse = recurse;
			return this;
		}

		public Iter(Entry parent, InputStream in) {
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
				next = new Entry(parent, entry, zip);
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
		public Entry next() {
			if (!hasNext()) throw new NoSuchElementException();
			if (next == null) return children.next();
			Entry each = next;
			next = null;
			return each;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}

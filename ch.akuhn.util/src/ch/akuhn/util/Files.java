
package ch.akuhn.util;

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
import java.util.Queue;

/**
 * Static methods that operate on or return files.
 * 
 */
public abstract class Files {

    private static class Find implements Iterator<File> {

        private String[] extensions;
        private Queue<File> queue;

        public Find(File folder, String... extensions) {
            this.queue = new LinkedList<File>();
            this.extensions = extensions;
            this.queue.offer(folder);
        }

        public boolean hasNext() {
            pollFolders();
            return !queue.isEmpty();
        }

        private boolean include(File file) {
            String name = file.getName();
            for (String each : extensions)
                if (name.endsWith(each)) return true;
            return false;
        }

        public File next() {
            pollFolders();
            if (queue.isEmpty()) throw new NoSuchElementException();
            return queue.poll();
        }

        private void pollFolders() {
            while (!queue.isEmpty()) {
                if (!queue.peek().isDirectory()) break;
                File next = queue.poll();
                for (File each : next.listFiles()) {
                    if (include(each) || each.isDirectory()) queue.offer(each);
                }
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static Iterable<File> all(File file) {
        return all(file, (FileFilter) null);
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
                        if (queue.isEmpty()) throw new NoSuchElementException();
                        return queue.poll();
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

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }

                };
            }
        };
    }

    public static Iterable<File> all(File file, String pattern) {
        if (pattern.equals("*")) return all(file);
        if (pattern.lastIndexOf('*') != 0) throw // TODO support other patterns
        new UnsupportedOperationException("Patterns other than \"*abc\" not yet supported.");
        return all(file, endsWith(pattern.substring(1)));
    }

    public static Iterable<File> all(String filename) {
        return all(new File(filename));
    }

    public static Iterable<File> all(String filename, FileFilter filter) {
        return all(new File(filename), filter);
    }

    public static Iterable<File> all(String filename, String pattern) {
        return all(new File(filename), pattern);
    }

    public static void close(Object appendable) {
        // Take into account that nice blog post
        if (appendable instanceof Closeable) {
            try {
                ((Closeable) appendable).close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static FileFilter endsWith(final String suffix) {
        return new FileFilter() {

            public boolean accept(File file) {
                return file.getName().endsWith(suffix);
            }
        };
    }

    public static IterableIterator<File> find(final File folder, String... extensions) {
        return IterableIteratorFactory.create(new Find(folder, extensions));
    }

    public static CharSequence openRead(File file) {
        return Strings.fromFile(file);
    }

    public static CharSequence openRead(String filename) {
        return Strings.fromFile(filename);
    }

    public static Appendable openWrite(File file) {
        try {
            return new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Appendable openWrite(String filename) {
        return openWrite(new File(filename));
    }

    public static Object extension(String name) {
        int index = name.lastIndexOf('.');
        int slash = name.lastIndexOf('/');
        if (index < slash || index < 0) return "";
        return name.substring(index + 1);
    }

    public static Iterable<File> find(String folder, String... extensions) {
        return find(new File(folder), extensions);
    }
    
    /** Matches a shell pattern. 
     *<P> 
     * Any character that appears in a pattern, other than the special pattern characters described below, matches itself. 
     * The special pattern characters have the following meanings:
	 * <ul><li><tt>*</tt> Matches any string, including the empty string.</li>
     * <li><tt>?</tt> Matches any single character.</li></ul>
     *
     */
    public static boolean match(String pattern, String name) {
    	return match(pattern, 0, name, 0);
    }
    
    private static boolean match(String pattern, int i, String name, int j) {
    	for (; i < pattern.length(); i++, j++) {
    		char p = pattern.charAt(i);
    		if (p == '*') return matchStar(pattern, i + 1, name, j);
    		if (j == name.length()) return false;
    		if (p != '?' && p != name.charAt(j)) return false;
    	}
    	return j == name.length();
    }
    
    private static boolean matchStar(String pattern, int i, String name, int j) {
    	while (j <= name.length()) if (match(pattern, i, name, j++)) return true;
    	return false;
    }

}

package ch.akuhn.hapax.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferUnderflowException;

import ch.akuhn.util.Throw;

public abstract class CharStream implements Closeable {

    // TODO replace all assertions with real exception
    
    public static final CharStream fromString(String string) {
        return new StringCharStream(string);
    }
    
    public static final CharStream fromFile(File file) {
        try {
            return new InputStreamCharStream(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            throw Throw.exception(ex);
        }
    }
 
    public static final char EOF = (char) -1;
    protected StringBuffer buf = null;
    public char curr;

    public abstract char basicNext();
    
    public abstract char basicBacktrack();
    
    static class StringCharStream extends CharStream {

        private final String string;
        private int pos = 0;
        
        public StringCharStream(String string) {
            this.string = string;
        }
        
        @Override
        public char basicBacktrack() {
            assert pos > 0;
            return string.charAt(--pos-1);
        }

        @Override
        public char basicNext() {
            if (pos >= string.length()) throw new BufferUnderflowException();
            return string.charAt(pos++);
        }

        public void close() throws IOException {
            // do nothing
        }
        
    }
    
    static class InputStreamCharStream extends CharStream {

        private InputStream input;
        private char prev, next = EOF;
        private boolean backtracked = false;
        
        public InputStreamCharStream(InputStream input) {
            this.input = input;
        }
        
        @Override
        public char basicBacktrack() {
            assert backtracked == false;
            backtracked = true;
            return prev;
        }

        @Override
        public char basicNext() {
            if (backtracked) {
                backtracked = false;
                return next;
            }
            try {
                prev = next;
                next = (char) input.read();
                if (next == EOF) throw new BufferUnderflowException();
                return next;
            } catch (IOException ex) {
                throw Throw.exception(ex);
            }
        }

        public void close() throws IOException {
            input.close();
        }
        
    }

    public boolean hasMark() {
        return buf != null;
    }

    public void mark() {
        assert buf == null;
        buf = new StringBuffer();
    }

    public String yank() {
        String result = buf.toString();
        buf = null;
        return result;
    }

    public void backtrack() {
        curr = this.basicBacktrack();
        if (buf != null && buf.length() > 0) {
            buf.setLength(buf.length() - 1);
        }
    }
    
    public char next() {
        if (buf != null) buf.append(curr);
        return curr = basicNext();
    }

    public static CharStream fromInputStream(InputStream stream) {
        return new InputStreamCharStream(stream);
    }
    
}

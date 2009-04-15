package ch.akuhn.hapax.util;

import java.io.EOFException;
import java.io.IOException;

@SuppressWarnings("serial")
public class RuntimeIOException extends RuntimeException {

    public RuntimeIOException(IOException ex) {
        super(ex);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }

    public boolean isEOF() {
        return getCause() instanceof EOFException;
    }
    
}

package ch.akuhn.hapax.corpus;

import java.io.File;
import java.io.InputStream;
import java.nio.BufferUnderflowException;

import ch.akuhn.hapax.util.CharStream;

public abstract class Scanner implements Runnable {

    protected char ch;

    protected CharStream in;
    private ScannerClient client;

    protected final void backtrack() {
        in.backtrack();
        ch = in.curr;
    }

    public Scanner client(ScannerClient client) {
        this.client = client;
        return this;
    }

    public ScannerClient getClient() {
        return client;
    }

    protected final void mark() {
        in.mark();
    }

    protected final void next() throws BufferUnderflowException {
        in.next();
        ch = in.curr;
    }

    public Scanner onFile(File file) {
        in = CharStream.fromFile(file);
        return this;
    }

    public Scanner onString(String string) {
        this.in = CharStream.fromString(string);
        return this;
    }
    
    public Scanner onString(CharSequence chars) {
        return onString(chars.toString());
    }

    //@Override
    public void run() {
        assert client != null && in != null;
        try {
            next();
            this.scan();
        } catch (BufferUnderflowException e) {
            if (!in.hasMark()) return;
            this.yank();
        }
    }

    protected abstract void scan() throws BufferUnderflowException;

    protected final void yank() {
        client.yield(in.yank());
    }

    public Scanner onStream(InputStream stream) {
        in = CharStream.fromInputStream(stream);
        return this;
    }

}

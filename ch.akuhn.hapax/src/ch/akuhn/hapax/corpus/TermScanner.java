package ch.akuhn.hapax.corpus;

import java.io.File;
import java.io.InputStream;
import java.nio.BufferUnderflowException;

import ch.akuhn.hapax.util.CharStream;

public abstract class TermScanner implements Runnable {

    protected char ch;

    protected CharStream in;
    private ScannerClient client;

    protected final void backtrack() {
        in.backtrack();
        ch = in.curr;
    }

    public TermScanner client(ScannerClient newClient) {
        this.client = newClient;
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

    public TermScanner onFile(File file) {
        in = CharStream.fromFile(file);
        return this;
    }

    public TermScanner onString(String string) {
        this.in = CharStream.fromString(string);
        return this;
    }
    
    public TermScanner onString(CharSequence chars) {
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

    public TermScanner onStream(InputStream stream) {
        in = CharStream.fromInputStream(stream);
        return this;
    }
    
    public TermScanner newInstance() {
    	try {
			return this.getClass().getConstructor().newInstance();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
    }

	public Terms fromString(String contents) {
		Terms terms = new Terms();
		this.newInstance().client(terms).onString(contents).run();
		return terms;
	}

	public Terms fromFile(File file) {
		Terms terms = new Terms();
		this.newInstance().client(terms).onFile(file).run();
		return terms;
	}

}

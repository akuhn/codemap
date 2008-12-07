package ch.akuhn.hapax;

import java.io.File;
import java.io.FileInputStream;
import java.nio.BufferUnderflowException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import ch.akuhn.util.Throw;


public abstract class Scanner implements Runnable {

    private static final int NONE = -1;
    
    private CharBuffer buf;
    protected char ch;
    private ScannerClient client;
    private int mark;
    
	protected final void backtrack() {
        buf.position(buf.position() - 1);
    }

    public Scanner client(ScannerClient client) {
        this.client = client;
        return this;
    }

    public ScannerClient getClient() {
        return client;
    }
	
	protected final void mark() {
        mark = buf.position();
    }

    protected final void next() throws BufferUnderflowException {
        ch = buf.get();
    }

    public Scanner onFile(File file) {
        try {
            FileInputStream input = new FileInputStream(file);
            FileChannel channel = input.getChannel();
            long filesize = channel.size();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            //Charset charset = Charset.forName("UTF-8");
            Charset charset = Charset.forName("ISO-8859-1");
            CharsetDecoder decoder = charset.newDecoder();
            this.buf = decoder.decode(buffer);	
            return this;
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
	}

    public Scanner onString(String string) {
		this.buf = CharBuffer.wrap(string);
		return this;
	}
    
    @Override
    public void run() {
        assert client != null && buf != null;
        try {
            mark = NONE;
            next();
            this.scan();
        } catch (BufferUnderflowException e) {
            if (mark == NONE) return;
            buf.position(mark - 1);
            client.yield(buf.subSequence(0, buf.limit() - mark + 1));
        }
    }
    
    protected abstract void scan() throws BufferUnderflowException;
    
    protected final void yank() {
        int pos = buf.position();
        buf.position(mark - 1);
        client.yield(buf.subSequence(0, pos - mark));
        buf.position(pos);
        mark = NONE;
    }

}

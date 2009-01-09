package ch.akuhn.hapax.corpus;

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

    private StringBuilder buf;
    private CharBuffer in;
    protected char ch;
    private ScannerClient client;
    private int mark;

    protected final void backtrack() {
        in.position(in.position() - 1);
        if (buf != null && buf.length() > 0) {
            ch = buf.charAt(buf.length() - 1);
            buf.setLength(buf.length() - 1);
        }
    }

    public Scanner client(ScannerClient client) {
        this.client = client;
        return this;
    }

    public ScannerClient getClient() {
        return client;
    }

    protected final void mark() {
        mark = in.position();
        buf = new StringBuilder();
    }

    protected final void next() throws BufferUnderflowException {
        if (buf != null) buf.append(ch);
        ch = in.get();
    }

    public Scanner onFile(File file) {
        try {
            FileInputStream input = new FileInputStream(file);
            FileChannel channel = input.getChannel();
            long filesize = channel.size();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, filesize);
            // Charset charset = Charset.forName("UTF-8");
            Charset charset = Charset.forName("ISO-8859-1");
            CharsetDecoder decoder = charset.newDecoder();
            this.in = decoder.decode(buffer);
            return this;
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

    public Scanner onString(String string) {
        this.in = CharBuffer.wrap(string);
        return this;
    }

    @Override
    public void run() {
        assert client != null && in != null;
        try {
            mark = NONE;
            next();
            this.scan();
        } catch (BufferUnderflowException e) {
            if (mark == NONE) return;
            this.yank();
        }
    }

    protected abstract void scan() throws BufferUnderflowException;

    protected final void yank() {
        assert mark != NONE;
        client.yield(buf.toString());
        buf = null;
        mark = NONE;
    }

}

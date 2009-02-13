package ch.deif.meander.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.akuhn.util.Assert;
import ch.akuhn.util.Throw;

public class TeeInputStream extends InputStream {

    private OutputStream recorder;
    private InputStream stream;
    private boolean closeRecorder = true;
    
    public TeeInputStream(InputStream stream, OutputStream recorder) {
        this.stream = Assert.notNull(stream);
        this.recorder = Assert.notNull(recorder);
    }
    
    public TeeInputStream(InputStream stream) {
        this(stream, System.out);
        closeRecorder = false;
    }
    
    public TeeInputStream(InputStream stream, String fname) {
        this(stream, open(fname));
    }
    
    private static OutputStream open(String fname) {
        try {
            return new FileOutputStream(fname);
        } catch (FileNotFoundException ex) {
            throw Throw.exception(ex);
        }
    }

    @Override
    public int available() throws IOException {
        return stream.available();
    }



    @Override
    public void close() throws IOException {
        if (closeRecorder) recorder.close();
        stream.close();
    }



    @Override
    public synchronized void mark(int readlimit) {
        stream.mark(readlimit);
    }



    @Override
    public boolean markSupported() {
        return stream.markSupported();
    }



    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int length = stream.read(b, off, len);
        if (length >= 0) recorder.write(b, off, length);
        recorder.flush();
        return length;
    }



    @Override
    public int read(byte[] b) throws IOException {
        int length = stream.read();
        if (length >= 0) recorder.write(b, 0, length);
        recorder.flush();
        return length;
    }



    @Override
    public synchronized void reset() throws IOException {
        stream.reset();
    }



    @Override
    public long skip(long n) throws IOException {
        return stream.skip(n); // TODO is this right?
    }



    @Override
    public int read() throws IOException {
        int b = stream.read();
        if (b >= 0) recorder.write(b);
        recorder.flush();
        return b;
    }

}

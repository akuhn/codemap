package ch.deif.meander.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ch.akuhn.util.Assert;
import ch.akuhn.util.Throw;

public class TeeOutputStream extends OutputStream {

    private OutputStream stream, recorder;
    private boolean closeRecorder = true;
    
    public TeeOutputStream(OutputStream stream, OutputStream recorder) {
        this.stream = Assert.notNull(stream);
        this.recorder = Assert.notNull(recorder);
    }
    
    public TeeOutputStream(OutputStream stream) {
        this(stream, System.out);
        closeRecorder = false;
    }
    
    public TeeOutputStream(OutputStream stream, String fname) {
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
    public void close() throws IOException {
        if (closeRecorder) recorder.close();
        stream.close();
    }

    @Override
    public void flush() throws IOException {
        recorder.flush();
        stream.flush();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        recorder.write(b, off, len);
        stream.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        recorder.write(b);
        stream.write(b);
    }

    @Override
    public void write(int b) throws IOException {
        recorder.write(b);
        stream.write(b);
    }

}

package ch.akuhn.hapax.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.akuhn.util.Throw;

public class FileResource implements Resource {

    private File file;

    public FileResource(File file) {
        if (file == null) throw new IllegalArgumentException();
        this.file = file;
    }
    
    public ResourceStream open() {
        return new ReadStream();
    }

    private class ReadStream implements ResourceStream {
        
        InputStream in;
        DataInputStream binary;
        
        public ReadStream() {
            try {
                this.in = new FileInputStream(file);
                this.binary = new DataInputStream(in);
            } catch (FileNotFoundException ex) {
                throw Throw.exception(ex);
            }
        }

        public int nextInt() {
            try {
                return binary.readInt();
            } catch (IOException ex) {
                throw Throw.exception(ex);
            }
        }

        public void close() {
            try {
                binary.close();
            } catch (IOException ex) {
                throw Throw.exception(ex);
            }
        }

        public double nextDouble() {
            try {
                return binary.readDouble();
            } catch (IOException ex) {
                throw Throw.exception(ex);
            }
        }

        public ResourceStream put(double value) {
            // TODO Auto-generated method stub
            return null;
        }

        public ResourceStream put(int value) {
            // TODO Auto-generated method stub
            return null;
        }
        
    }

    public ResourceStream writeStream() {
        return new WriteStream();
    }
    
    private class WriteStream implements ResourceStream {
        
        OutputStream in;
        DataOutputStream binary;
        
        public WriteStream() {
            try {
                this.in = new FileOutputStream(file);
                this.binary = new DataOutputStream(in);
            } catch (FileNotFoundException ex) {
                throw Throw.exception(ex);
            }
        }

        public ResourceStream put(int value) {
            try {
                binary.writeInt(value);
                return this;
            } catch (IOException ex) {
                throw Throw.exception(ex);
            }
        }

        public ResourceStream put(double value) {
            try {
                binary.writeDouble(value);
                return this;
            } catch (IOException ex) {
                throw Throw.exception(ex);
            }
        }
        
        public void close() {
            try {
                binary.close();
            } catch (IOException ex) {
                throw Throw.exception(ex);
            }
        }


        public int nextInt() {
            // TODO Auto-generated method stub
            return 0;
        }

        public double nextDouble() {
            // TODO Auto-generated method stub
            return 0;
        }
        
    }

}

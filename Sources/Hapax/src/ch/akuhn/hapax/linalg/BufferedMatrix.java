package ch.akuhn.hapax.linalg;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import ch.akuhn.util.Providable;


public class BufferedMatrix extends Matrix {

    private int rows, columns;
    private DoubleBuffer data;
    
    public BufferedMatrix(String filename) throws IOException {
        FileChannel channel = new FileInputStream(filename).getChannel();
        ByteBuffer header = ByteBuffer.allocate(8);
        channel.read(header);
        rows = header.getInt();
        columns = header.getInt();
        data = channel.map(MapMode.READ_ONLY, 8, channel.size() - 8).asDoubleBuffer();
    }
    
    public BufferedMatrix(int rows, int columns, DoubleBuffer data) {
        this.rows = rows;
        this.columns = columns;
        this.data = data;
        assert data.limit() == (rows * columns);
    }
    
    @Override
    public Iterable<Vector> rows() {
        return new RowProvider();
    }
    
    private class RowProvider extends Providable<Vector> {

        private DoubleBuffer buf;

        @Override
        public void initialize() {
            this.buf = data.duplicate();
            this.buf.rewind();
        }

        @Override
        public Vector provide() {
            if (!buf.hasRemaining()) return done();
            double[] values = new double[columns];
            buf.get(values);
            return new DenseVector(values);
        }
    }

    @Override
    public int columnSize() {
        return columns;
    }

    @Override
    public Iterable<Vector> columns() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double get(int row, int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public double put(int row, int column, double value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int rowSize() {
        return rows;
    }

    @Override
    public int used() {
        return rows * columns;
    }
 
}

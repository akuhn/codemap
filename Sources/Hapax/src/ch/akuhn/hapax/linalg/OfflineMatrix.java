package ch.akuhn.hapax.linalg;

import ch.akuhn.hapax.linalg.Vector.Entry;
import ch.akuhn.hapax.util.Resource;
import ch.akuhn.hapax.util.ResourceStream;
import ch.akuhn.hapax.util.RuntimeIOException;
import ch.akuhn.util.Providable;


public class OfflineMatrix extends Matrix {

    private int rows, columns;
    private Resource file;
    
    public OfflineMatrix(Resource file) {
        this.file = file; 
        ResourceStream in = file.open();
        rows = in.nextInt();
        columns = in.nextInt();
        in.close();
    }
    
    @Override
    public Iterable<Vector> rows() {
        return new RowProvider();
    }
    
    private class RowProvider extends Providable<Vector> {

        private ResourceStream in;

        @Override
        public void initialize() {
            in = file.open();
            if (rows != in.nextInt()) throw new AssertionError();
            if (columns != in.nextInt()) throw new AssertionError();
        }

        @Override
        public Vector provide() {
            try {
                double[] values = new double[columns];
                for (int n = 0; n < columns; n++) values[n] = in.nextDouble();
                return new DenseVector(values);
            } catch (RuntimeIOException ex) {
                if (ex.isEOF()) return done();
                throw ex;
            }
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
 
    public static final OfflineMatrix from(Matrix matrix, Resource file) {
        ResourceStream out = file.writeStream();
        out.put(matrix.rowSize());
        out.put(matrix.columnSize());
        for (Vector row: matrix.rows()) 
            for (Entry each: row.entries()) 
                out.put(each.value);
        out.close();
        return new OfflineMatrix(file);
    }
    
}

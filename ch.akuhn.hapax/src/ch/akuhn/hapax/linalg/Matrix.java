package ch.akuhn.hapax.linalg;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import ch.akuhn.hapax.linalg.Vector.Entry;
import ch.akuhn.util.Files;
import ch.akuhn.util.PrintOn;
import ch.akuhn.util.Throw;

public abstract class Matrix {

    public double add(int row, int column, double value) {
        return put(row, column, get(row, column) + value);
    }

    public Iterable<Vector> rows() {
    	return vecs(/*isRow*/ true);
    }
    
    private Iterable<Vector> vecs(final boolean isRow) {
    	return new Iterable<Vector>() {
			@Override
			public Iterator<Vector> iterator() {
				return new Iterator<Vector>() {

					private int count = 0;
					
					@Override
					public boolean hasNext() {
						return count < (isRow ? rowCount() : columnCount());
					}

					@Override
					public Vector next() {
						if (!hasNext()) throw new NoSuchElementException();
						return new Vec(count++, isRow);
					}

					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
    }
    
    
    public Iterable<Vector> columns() {
    	return vecs(/*isRow*/ false);
    }

    public abstract int columnCount();

    public double density() {
        return (double) used() / size();
    }
    
    public int size() {
    	return rowCount() * columnCount();
    }

    public abstract double get(int row, int column);

    public abstract double put(int row, int column, double value);

    public abstract int rowCount();

    public abstract int used();

    public void storeBinaryOn(DataOutput out) throws IOException {
        out.writeInt(this.rowCount());
        out.writeInt(this.columnCount());
        out.writeInt(this.used());
        for (Vector row: rows()) {
            out.writeInt(row.used());
            for (Entry each: row.entries()) {
                out.writeInt(each.index);
                out.writeFloat((float) each.value);
            }
        }
        Files.close(out);
    }

    public void storeBinaryOn(String fname) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(fname));
            storeBinaryOn(out);
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

    /** @see http://tedlab.mit.edu/~dr/svdlibc/SVD_F_ST.html */
    public void storeSparseOn(Appendable appendable) {
        // this stores the transposed matrix, but as we will transpose it again
        // when reading it, this can be done without loss of generality.
        PrintOn out = new PrintOn(appendable);
        out.print(this.columnCount()).space();
        out.print(this.rowCount()).space();
        out.print(this.used()).cr();
        for (Vector row: rows()) {
            out.print(row.used()).cr();
            for (Entry each: row.entries()) {
                out.print(each.index).space().print(each.value).space();
            }
            out.cr();
        }
        out.close();
    }

    public void storeSparseOn(String fname) {
        storeSparseOn(Files.openWrite(fname));
    }
    
    public Vector row(int row) {
    	return new Vec(row, /*isRow*/ true);
    }
    
    public Vector column(int column) {
    	return new Vec(column, /*isRow*/ false);
    }
    
    public double[][] asArray() {
		double[][] result = new double[rowCount()][columnCount()];
		for (int x = 0; x < result.length; x++) {
			for (int y = 0; y < result.length; y++) {
				result[x][y] = get(x,y);
			}
		}
		return result;
	}

	public static int indexOf(Vector vec) {
		return ((Vec) vec).index0;
	}

	private class Vec extends Vector {
		
		private int index0;
    	private boolean isRow;
    	
    	private Vec(int n, boolean isRow) {
    		this.isRow = isRow;
    		this.index0 = n;
    	}
    	
		@Override
		public int size() {
			return isRow ? columnCount() : rowCount();
		}
			
		@Override
		public double put(int index, double value) {
			return isRow ? Matrix.this.put(this.index0, index, value)
					: Matrix.this.put(index, this.index0, value);
		}
			
		@Override
		public double get(int index) {
			return isRow ? Matrix.this.get(this.index0, index)
					: Matrix.this.get(index, this.index0);
		}
		
    }

	public double minValue() {
		double min = Double.MAX_VALUE;
		for (Vector row: rows()) 
			for (Entry each: row.entries()) 
				if (each.value < min) min = each.value;
		return min;
	};

}

package ch.akuhn.hapax.linalg;

import static ch.akuhn.util.Interval.range;
import static java.lang.String.format;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import ch.akuhn.hapax.util.StreamGobbler;
import ch.akuhn.util.Throw;

public class SVD {

    class Gobbler extends StreamGobbler {

        public Gobbler(InputStream is) {
            super(is);
        }

        private void gobbleFooter() {
            consume("ELAPSED", "CPU", "TIME", "=");
            SVD.this.time = scan.nextFloat();
            consume("sec.");
            consume("MULTIPLICATIONS", "BY", "A", "=", null);
            consume("MULTIPLICATIONS", "BY", "A^T", "=", null);
        }

        private void gobbleHeader() {
            consume("Loading", "the", "matrix...");
            consume("Computing", "the", "SVD...");
            consume("SOLVING", "THE", "[A^TA]", "EIGENPROBLEM");
            consume("NO.", "OF", "ROWS", "=", null);
            consume("NO.", "OF", "COLUMNS", "=", null);
            consume("NO.", "OF", "NON-ZERO", "VALUES", "=", null);
            consume("MATRIX", "DENSITY", "=", null);
            consume("MAX.", "NO.", "OF", "LANCZOS", "STEPS", "=", null);
            consume("MAX.", "NO.", "OF", "EIGENPAIRS", "=", null);
            consume("LEFT", "END", "OF", "THE", "INTERVAL", "=", null);
            consume("RIGHT", "END", "OF", "THE", "INTERVAL", "=", null);
            consume("KAPPA", "=", null);
        }

        private void gobbleLeftSingularValues() {
            consume("LEFT", "SINGULAR", "VECTORS", "(transpose", "of", "U):");
            int rows = scan.nextInt();
            int columns = scan.nextInt();
            SVD.this.Ut = new float[rows][columns];
            for (int row: range(rows)) {
                for (int column: range(columns)) {
                    SVD.this.Ut[row][column] = scan.nextFloat();
                }
            }
        }

        private void gobbleRightSingularValues() {
            consume("RIGHT", "SINGULAR", "VECTORS", "(transpose", "of", "V):");
            int rows = scan.nextInt();
            int columns = scan.nextInt();
            SVD.this.Vt = new float[rows][columns];
            for (int row: range(rows)) {
                for (int column: range(columns)) {
                    SVD.this.Vt[row][column] = scan.nextFloat();
                }
            }
        }

        private void gobbleRitzValues() {
            String next = scan.next(); // either "NUMBER" or "TRANSPOSING"
            if (next.equals("TRANSPOSING")) {
                consume("THE", "MATRIX", "FOR", "SPEED", "NUMBER");
            } else assert next.equals("NUMBER");
            int skip = consumeInt("OF", "LANCZOS", "STEPS", "=");
            consumeInt("RITZ", "VALUES", "STABILIZED", "=");
            consume("COMPUTED", "RITZ", "VALUES", "(ERROR", "BNDS)");
            for (int n: range(skip)) {
                consume(Integer.toString(n + 1), null, null, null);
            }
        }

        private void gobbleSingularValues() {
            int len = consumeInt("SINGULAR", "VALUES:");
            SVD.this.s = new float[len];
            for (int n: range(len)) {
                SVD.this.s[n] = scan.nextFloat();
            }
        }

        @Override
        public void run() {
            gobbleHeader();
            gobbleRitzValues();
            gobbleSingularValues();
            gobbleLeftSingularValues();
            gobbleRightSingularValues();
            gobbleFooter();
            expectEOF();
        }

    }

    public SVD(float[] s, float[][] Ut, float[][] Vt) {
        assert s.length == Ut.length;
        assert s.length == Vt.length;
        this.s = s;
        this.Ut = Ut;
        this.Vt = Vt; 
    }

    private String command(int dimensions) {
        return format("%s -d %d -v 3 %s", fname(), dimensions, "-");
    }
    
    private SVD decompose(Matrix matrix, int dimensions) {
        try {
            StreamGobbler error, input;
            String command = command(dimensions);
            Process proc = Runtime.getRuntime().exec(command);
            error = new StreamGobbler(proc.getErrorStream());
            input = new Gobbler(proc.getInputStream());
            error.start();
            input.start();
            matrix.storeSparseOn(new OutputStreamWriter(proc.getOutputStream()));
            int exit = proc.waitFor();
            error.kill();
            input.kill();
            if (exit != 0) throw new Error(command);
            return this;
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }

    public float[] s;

    public float time;

    public float[][] Ut;

    public float[][] Vt;

    public double similarityUU(int a, int b) {
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n: range(dim)) {
            sim += Ut[n][a] * Ut[n][b] * (s[n] * s[n]);
            suma += Ut[n][a] * Ut[n][a] * (s[n] * s[n]);
            sumb += Ut[n][b] * Ut[n][b] * (s[n] * s[n]);
        }
        return (sim / (Math.sqrt(suma) * Math.sqrt(sumb)));
    }

    public double similarityUV(int a, int b) {
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n: range(dim)) {
            sim += Ut[n][a] * Vt[n][b] * s[n];
            suma += Ut[n][a] * Ut[n][a] * s[n];
            sumb += Vt[n][b] * Vt[n][b] * s[n];
        }
        return sim / (Math.sqrt(suma) * Math.sqrt(sumb));
    }

    public double similarityV(int a, double[] pseudo) {
        assert pseudo.length == s.length;
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n: range(dim)) {
            sim += Vt[n][a] * pseudo[n] * s[n] * s[n];
            suma += Vt[n][a] * Vt[n][a] * s[n] * s[n];
            sumb += pseudo[n] * pseudo[n] * s[n] * s[n];
        }
        return sim / (Math.sqrt(suma) * Math.sqrt(sumb));
    }

    public double similarityVV(int a, int b) {
        assert a < Vt[0].length : a + " < " + Vt[0].length;
        assert b < Vt[0].length : b + " < " + Vt[0].length;
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n: range(dim)) {
            sim += Vt[n][a] * Vt[n][b] * s[n] * s[n];
            suma += Vt[n][a] * Vt[n][a] * s[n] * s[n];
            sumb += Vt[n][b] * Vt[n][b] * s[n] * s[n];
        }
        return sim / (Math.sqrt(suma) * Math.sqrt(sumb));
    }
    
    private static String fname() {
        String fname = System.getenv("SVD");
        return fname != null ? fname : "svd";
    }

    public SVD(Matrix matrix, int dimensions) {
        this.decompose(matrix, dimensions);
    }

    public SVD transposed() {
        return new SVD(s, Vt, Ut); // swap Ut and Vt
    }

}
package ch.akuhn.hapax.svd;

import java.io.InputStream;
import java.io.OutputStreamWriter;

import ch.akuhn.hapax.SparseMatrix;
import ch.akuhn.util.Throw;


public class SVD {
    
    public float[] s;
    public float[][] Ut;
    public float[][] Vt;
    public float time;   

    public static void main(String[] args) {
        // (20000, 1000, 0.01d, 50) -> 26.157 
        // (200000, 10000, 0.01d, 50) -> 3784.06 = 01:03'04.06"
        SVD svd = SVD.fromRandomMatrix(2000, 100, 0.25d, 50);
        System.out.println("Elapsed time: " + svd.time);        
        System.out.println(svd.Ut.length + " x " + svd.Ut[0].length);        
        System.out.println(svd.Vt.length + " x " + svd.Vt[0].length); 
        System.out.println(svd.similarityUU(1000, 1500));
        System.out.println(svd.similarityVV(10, 15));
        System.out.println(svd.similarityUV(1500, 10));
    }
    
    public double similarityUU(int a, int b) {
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n = 0; n < dim; n++) {
            sim += Ut[n][a] * Ut[n][b] * s[n] * s[n];
            suma += Ut[n][a] * s[n] * Ut[n][a] * s[n];
            sumb += Ut[n][b] * s[n] * Ut[n][b] * s[n];   
        }
        return sim / (Math.sqrt(suma) * Math.sqrt(sumb));
    }
    
    public double similarityVV(int a, int b) {
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n = 0; n < dim; n++) {
            sim += Vt[n][a] * Vt[n][b] * s[n] * s[n];
            suma += Vt[n][a] * Vt[n][a] * s[n] * s[n];
            sumb += Vt[n][b] * Vt[n][b] * s[n] * s[n];   
        }
        return sim / (Math.sqrt(suma) * Math.sqrt(sumb));
    }
    
    public double similarityUV(int a, int b) {
        int dim = s.length;
        double sim = 0;
        double suma = 0;
        double sumb = 0;
        for (int n = 0; n < dim; n++) {
            sim += Ut[n][a] * Vt[n][b] * s[n];
            suma += Ut[n][a] * Ut[n][a] * s[n];
            sumb += Vt[n][b] * Vt[n][b] * s[n];   
        }
        return sim / (Math.sqrt(suma) * Math.sqrt(sumb));
    }
    
    public static SVD fromRandomMatrix(int rows, int columns, double density, int dimensions) {
        try {
            String command = String.format("lib\\svd -d %d -v 3 -", dimensions);
            Process proc = Runtime.getRuntime().exec(command);
            SVD svd = new SVD();
            new StreamGobbler(proc.getErrorStream()).start();            
            svd.new Gobbler(proc.getInputStream()).start();
            SparseMatrix.randomStoreSparseOn(rows,columns,density,new OutputStreamWriter(proc.getOutputStream()));
            int exit = proc.waitFor();
            if (exit != 0) throw new Error();
            return svd;
        }    
        catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }
    
    public static SVD fromMatrix(SparseMatrix matrix, int dimensions) {
        try {
            String command = String.format("lib\\svd -d %d -v 3 -", dimensions);
            Process proc = Runtime.getRuntime().exec(command);
            SVD svd = new SVD();
            new StreamGobbler(proc.getErrorStream()).start();            
            svd.new Gobbler(proc.getInputStream()).start();
            matrix.storeSparseOn(new OutputStreamWriter(proc.getOutputStream()));
            int exit = proc.waitFor();
            if (exit != 0) throw new Error();
            return svd;
        }    
        catch (Exception ex) {
            throw Throw.exception(ex);
        }
    }
    
    private class Gobbler extends StreamGobbler {
        
        public Gobbler(InputStream is) {
            super(is);
        }
        
        public void run() {
            gobbleHeader();
            gobbleRitzValues();
            gobbleSingularValues();
            gobbleLeftSingularValues();
            gobbleRightSingularValues();
            gobbleFooter();
            if ($.hasNext()) throw new Error();
        }

        private void gobbleFooter() {
            consume("ELAPSED","CPU","TIME","=");
            SVD.this.time = $.nextFloat();
            consume("sec.");
            consume("MULTIPLICATIONS","BY","A","=",null);
            consume("MULTIPLICATIONS","BY","A^T","=",null);
        }

        private void gobbleRightSingularValues() {
            consume("RIGHT","SINGULAR","VECTORS","(transpose","of","V):");
            int rows = $.nextInt();
            int columns = $.nextInt();
            SVD.this.Vt = new float[rows][columns];
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    SVD.this.Vt[row][column] = $.nextFloat();
                }
            }
        }

        private void gobbleLeftSingularValues() {
            consume("LEFT","SINGULAR","VECTORS","(transpose","of","U):");
            int rows = $.nextInt();
            int columns = $.nextInt();
            SVD.this.Ut = new float[rows][columns];
            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    SVD.this.Ut[row][column] = $.nextFloat();
                }
            }
        }
        
        private void gobbleSingularValues() {
            int len = consumeInt("SINGULAR","VALUES:");
            SVD.this.s = new float[len];
            for (int n = 0; n < len; n++) {
                SVD.this.s[n] = $.nextFloat();
            }
        }

        private void gobbleRitzValues() {
            int skip = consumeInt("NUMBER","OF","LANCZOS","STEPS","=");
            consumeInt("RITZ","VALUES","STABILIZED","=");
            consume("COMPUTED","RITZ","VALUES","(ERROR","BNDS)");
            for (int n = 0; n < skip; n++) {
                consume(Integer.toString(n + 1),null,null,null);
            }
        }

        private void gobbleHeader() {
            consume("Loading","the","matrix...");
            consume("Computing","the","SVD...");
            consume("SOLVING","THE","[A^TA]","EIGENPROBLEM");
            consume("NO.","OF","ROWS","=",null);
            consume("NO.","OF","COLUMNS","=",null);
            consume("NO.","OF","NON-ZERO","VALUES","=",null);
            consume("MATRIX","DENSITY","=",null);
            consume("MAX.","NO.","OF","LANCZOS","STEPS","=", null);
            consume("MAX.","NO.","OF","EIGENPAIRS","=", null);
            consume("LEFT","END","OF","THE","INTERVAL","=",null);
            consume("RIGHT","END","OF","THE","INTERVAL","=",null);
            consume("KAPPA","=",null);
        }
        
        private void consume(String... words) {
            for (int n = 0; n < words.length; n++) {
                String next = $.next();
                if (words[n] == null || words[n].equals(next)) continue;
                throw new Error("Expected " + words[n] + " but found " + next);
            }
        }
        private int consumeInt(String... words) {
            consume(words);
            return $.nextInt();
        }
        
    }
    
    
}
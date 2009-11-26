package sandbox;

import org.netlib.arpack.ARPACK;
import org.netlib.util.doubleW;
import org.netlib.util.intW;

import ch.akuhn.org.netlib.arpack.Eigenvalues;
import ch.akuhn.util.Stopwatch;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.EVD;
import no.uib.cipr.matrix.NotConvergedException;
import no.uib.cipr.matrix.Vector;

public class EVDExample {

    public static void main(String[] args) throws NotConvergedException {
        
        DenseMatrix m = new DenseMatrix(80,80);
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (Math.random() > 0.7) continue;
                double value = Math.random()*i-Math.random()*j;
                m.set(i, j, value);
                m.set(j, i, value);
            }
        }
        
        Stopwatch.p();
        EVD evd = EVD.factorize(m);
        Stopwatch.p("with LAPACK");
        
        double max = sandbox.Vector.from(evd.getRealEigenvalues()).sorted().reversed().p().val[0];
        
        DenseMatrix mm = evd.getRightEigenvectors();
        for (int i = 0; i < mm.numColumns(); i++) {
            System.out.print(evd.getRealEigenvalues()[i]);
            System.out.print("\t");
            for (int j = 0; j < mm.numRows(); j++) {
                System.out.print(mm.get(j, i)+", ");
            }
            System.out.println();
        }
        
        // ------------
        
        Stopwatch.p();

        new Eigenvalues(m, 2).run();
        
    }
    
}

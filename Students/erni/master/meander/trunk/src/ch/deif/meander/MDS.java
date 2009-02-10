package ch.deif.meander;

import java.io.IOException;
import java.io.PrintStream;

import ch.akuhn.hapax.linalg.SymetricMatrix;
import ch.akuhn.hapax.util.StreamGobbler;
import ch.akuhn.util.Throw;

public class MDS {

    private static String fname() {
        String fname = System.getenv("MDS");
        return fname != null ? fname : "hitmds2";
    }
    
    private void printMatrixOn(SymetricMatrix matrix, PrintStream out) throws IOException {
        out.print('#'); 
        out.print(' ');
        out.print(-matrix.columnSize());
        out.print(' ');
        out.print(-matrix.rowSize());
        out.print('\n');
        for (int n = 0; n < matrix.rowSize(); n++) {
            for (int m = 0; m < matrix.columnSize(); m++) {
                out.print(' ');
                out.print(matrix.get(n, m));
            }
            out.print('\n');
        }
        out.print("# -2"); 
        out.close();
    }
    
    public static MDS fromCorrelationMatrix(SymetricMatrix documentCorrelation) {
        return new MDS().compute(documentCorrelation);
    }

    private MDS compute(SymetricMatrix matrix) {
        try {
            String command = fname();
            Process proc = Runtime.getRuntime().exec(command);
            new StreamGobbler(proc.getErrorStream()).start();
            new StreamGobbler(proc.getInputStream()).start();
            printMatrixOn(matrix, new PrintStream(proc.getOutputStream()));
            int exit = proc.waitFor();
            if (exit != 0) throw new Error(command);
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
        return this;
    }
    
}

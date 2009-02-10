package ch.deif.meander;

import java.io.OutputStreamWriter;

import ch.akuhn.util.Throw;

public class MDS {

    public static void main(String[] args) {
        
        String matrix = "# 4 2\n1 1\n1 0\n0 0\n0 1\n# -2\n";
        
        try {
            String command = "lib/hitmds2.exe";
            Process proc = Runtime.getRuntime().exec(command);
            new StreamGobbler(proc.getErrorStream()).start();
            new StreamGobbler(proc.getInputStream()).start();
            OutputStreamWriter osw = new OutputStreamWriter(proc.getOutputStream());
            osw.append(matrix);
            osw.flush();
            int exit = proc.waitFor();
            if (exit != 0) throw new Error(command);
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }

        
    }
}

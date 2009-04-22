package ch.deif.meander;

import static ch.akuhn.util.Interval.range;
import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.util.StreamGobbler;
import ch.akuhn.util.As;
import ch.akuhn.util.TeeInputStream;
import ch.akuhn.util.TeeOutputStream;
import ch.akuhn.util.Throw;
import ch.deif.meander.util.Delimiter;

public class MDS {

    class Gobbler extends StreamGobbler {

        public boolean done = false;

        public Gobbler(InputStream is) {
            super(is);
        }

        @Override
        public void run() {
            r0 = consumeDouble("#", "corr(D,d):");
            r = consumeDouble("#", "corr(D,d):");
            for (int n : range(x.length)) {
                x[n] = scan.nextDouble();
                y[n] = scan.nextDouble();
            }
            done = true;
            expectEOF();
        }
    }

    public double[] x, y;
    public double r0;
    public double r;

    private static String fname() {
        String fname = System.getenv("MDS");
        return fname != null ? fname : "hitmds2";
    }

    private void printMatrixOn(LatentSemanticIndex index,
            Iterable<Location> locations, PrintStream out) throws IOException {
        out.append('#');
        out.append(' ');
        // TODO should be negative numbers but results are nicer this way :-)
        out.print(index.documents.size());
        out.append(' ');
        out.print(index.documents.size());
        out.append('\n');
        Delimiter delim = new Delimiter(".", 10000, 120);
        for (double value : index.documentCorrelations()) {
            out.append(' ');
            out.print((float) value);
        }
        out.append('\n');
        if (locations == null) {
            out.print("# -2\n");
        } else {
            out.print("# 2\n");
            for (Location each : locations) {
                float x = (float) each.x;
                out.print(x);
                out.append(' ');
                float y = (float) each.y;
                out.print(y);
                out.append('\n');
            }
            out.flush();
        }
        out.close();
    }

    public static MDS fromCorrelationMatrix(LatentSemanticIndex index) {
        return new MDS().compute(index, null);
    }

    public static MDS fromCorrelationMatrix(LatentSemanticIndex index,
            Iterable<Location> matchingLocations) {
        return new MDS().compute(index, matchingLocations);
    }

    private MDS compute(LatentSemanticIndex index,
            Iterable<Location> matchingLocations) {
        assert matchingLocations == null
                || index.documents.size() == As.list(matchingLocations).size();
        boolean tee = true;
        try {
            x = new double[index.documents.size()];
            y = new double[index.documents.size()];
            String command = format("%s 30 1 0 1:8", fname()); // TODO configure
            // this settings
            Process proc = Runtime.getRuntime().exec(command);
            InputStream err = proc.getErrorStream();
            InputStream in = proc.getInputStream();
            if (tee) err = new TeeInputStream(err, "error.log");
            if (tee) in = new TeeInputStream(in, "input.log");

            StreamGobbler error = new StreamGobbler(err);
            Gobbler input = new Gobbler(in);
            error.verbose().start();
            input.start();

            OutputStream out = proc.getOutputStream();
            if (tee) out = new TeeOutputStream(out, "output.log");
            printMatrixOn(index, matchingLocations, new PrintStream(out));

            int exit = proc.waitFor();
            while (!input.done)
                Thread.sleep(20);
            error.kill();
            input.kill();
            if (exit != 0) throw new Error(command);
        } catch (Exception ex) {
            throw Throw.exception(ex);
        }
        return this;
    }

}
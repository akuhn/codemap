package ch.deif.meander;

import static ch.akuhn.util.Interval.range;
import static java.lang.String.format;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import ch.akuhn.hapax.linalg.SymetricMatrix;
import ch.akuhn.hapax.util.StreamGobbler;
import ch.akuhn.util.Throw;

public class MDS {

	class Gobbler extends StreamGobbler {

		public Gobbler(InputStream is) {
			super(is);
		}

		@Override
		public void run() {
			r0 = consumeDouble("#", "corr(D,d):");
			r = consumeDouble("#", "corr(D,d):");
			for (int n : range(x.length)) {
				x[n] = $.nextDouble();
				y[n] = $.nextDouble();
			}
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


	private void printMatrixOn(SymetricMatrix matrix,
			Iterable<Location> locations, PrintStream out) throws IOException {
		out.print('#');
		out.print(' ');
		// TODO: should be negative numbers but results are nicer this way :-)
		out.print(matrix.columnSize());
		out.print(' ');
		out.print(matrix.rowSize());
		out.print('\n');
		for (int n = 0; n < matrix.rowSize(); n++) {
			for (int m = 0; m < matrix.columnSize(); m++) {
				out.print(' ');
				out.print(matrix.get(n, m));
			}
			out.print('\n');
		}
		if (locations == null) {
			out.print("# -2");
			out.close();			
		} else {
			out.print("# 2");
			for (Location each : locations) {
				out.println(each.x + ' ' + each.y);
			}
			out.close();
		}
	}

	public static MDS fromCorrelationMatrix(SymetricMatrix documentCorrelation) {
		return new MDS().compute(documentCorrelation, null);
	}

	public static MDS fromCorrelationMatrix(SymetricMatrix documentCorrelation,
			Iterable<Location> matchingLocations) {
		return new MDS().compute(documentCorrelation, matchingLocations);
	}

	private MDS compute(SymetricMatrix matrix,
			Iterable<Location> matchingLocations) {
		try {
			x = new double[matrix.columnSize()];
			y = new double[matrix.columnSize()];
			// String command = format("%s %d %f", fname(), 10, 0.1);
			String command = format("%s 50 1 0 1:8", fname());
			Process proc = Runtime.getRuntime().exec(command);
			new StreamGobbler(proc.getErrorStream()).start();
			new Gobbler(proc.getInputStream()).start();
			printMatrixOn(matrix, matchingLocations, new PrintStream(proc
					.getOutputStream()));

			int exit = fixBrokenWaitFor(proc);
			if (exit != 0)
				throw new Error(command);
		} catch (Exception ex) {
			throw Throw.exception(ex);
		}
		return this;
	}

	private int fixBrokenWaitFor(Process proc) {
		// int exit = proc.waitFor();
		int exit = -1;
		boolean done = false;
		while (!done) {
			try {
				exit = proc.exitValue();
				done = true;
			} catch (IllegalThreadStateException e) {
				// Thread.sleep(20);
			}
		}
		return exit;
	}
}

package ch.akuhn.org.ggobi.plugins.ggvis;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.linalg.Matrix;

public class Main {

	private static final boolean VIZ = !true;

	public static void main(String[] args) {
		
		Ggobid gg = new Ggobid();
		Ggvisd ggv = new Ggvisd();
		ggv.ggvis_init(gg);
		
		Mds mds = new Mds();
		Hapax hapax = Hapax.newCorpus()
			.useTFIDF()
			.useCamelCaseScanner()
			.addFiles("..", ".java")
			.build();
		
		System.out.println("done");

		Matrix corr = hapax.getIndex().documentCorrelation();
		double[][] dissimilarities = corr.asArray();
		for (int n = 0; n < dissimilarities.length; n++) {
			for (int m = 0; m < dissimilarities.length; m++) {
				dissimilarities[n][m] = Math.pow(Math.max(0, 1 - dissimilarities[n][m]), 0.5);
				//dissimilarities[n][m] = n == m ? 0.0 : 1.0 ;
			}
		}
		
		mds.init(ggv, dissimilarities);
		Viz viz  = VIZ ? new Viz(ggv.pos.vals).open() : null;
		while (true) {
			long t = System.nanoTime();
			for (int n = 0; n < 10; n++)	{
				mds.mds_once(true, ggv, gg);
				if (VIZ) viz.points = ggv.pos.vals;
			}
			System.out.printf("%d\n", (int) (1e12 / (System.nanoTime() - t)) * 10);
		}
	}

	static void print(Mds mds, Ggvisd ggv, Ggobid gg) {
		for (int i = 0; i < ggv.pos.nrows; i++) {
			for (int j = 0; j < ggv.pos.ncols; j++) {
				System.out.print(ggv.pos.vals[i][j]);
				System.out.print(' ');
			}
			System.out.println();
		}
	}
	
}

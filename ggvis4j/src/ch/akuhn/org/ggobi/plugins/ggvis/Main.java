package ch.akuhn.org.ggobi.plugins.ggvis;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.linalg.Matrix;

public class Main {

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

		Matrix corr = hapax.getIndex().documentCorrelation();
		double[][] dissimilarities = corr.asArray();
		for (int n = 0; n < dissimilarities.length; n++) {
			for (int m = 0; m < dissimilarities.length; m++) {
				dissimilarities[n][m] = Math.pow(Math.max(0, 1 - dissimilarities[n][m]), 0.5);
				//dissimilarities[n][m] = n == m ? 0.0 : 1.0 ;
			}
		}
		
		mds.run(ggv, dissimilarities);
		
		//Viz viz = new Viz(ggv.pos.vals).open();
		while (true) {
			mds.mds_once(true, ggv, gg);
			//viz.points = ggv.pos.vals;
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

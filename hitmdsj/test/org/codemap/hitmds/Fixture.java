package org.codemap.hitmds;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Fixture {

	public static String ZIPFILE = "lib/hitmds2.zip";
	public static final int NOF_LINES = 4824;

	
	public static double[][] genesEndo(int maxLines) {
		try {
			ZipFile zip = new ZipFile(ZIPFILE);
			ZipEntry entry = zip.getEntry("hitmds-2/genes_endo_4824.dat");
			InputStream in = zip.getInputStream(entry);
			Scanner scanner = new Scanner(in);
			scanner.useDelimiter("[#\\s]+");
			int lines = scanner.nextInt();
			int dim = scanner.nextInt();
			assert maxLines <= lines;
			double[][] data = new double[maxLines][dim];
			for (int line = 0; line < maxLines; line++) {
				for (int n = 0; n < dim; n++) {
					data[line][n] = scanner.nextDouble();
				}
			}
			zip.close();
			return data;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
}

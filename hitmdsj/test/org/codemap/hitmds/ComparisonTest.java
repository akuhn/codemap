package org.codemap.hitmds;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.codemap.hitmds.random.PrimitiveRandom;
import org.junit.Test;


public class ComparisonTest {
	
	private static final int TARGET_DIM = 2;
	private static final double EPSILON = 0.3;	
	
	/**
	 * Tests that the c implementation ant he java implementation of hitmds yield
	 * about the same output.
	 * 
	 * To run this test, first compile the c implementation (folder primitive_rand-hitmds)
	 * and execute run.sh within that folder. 
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testSameOutput() throws IOException {
		File file = new File("primitive_rand-hitmds/genes_endo_4824_result.dat");
		assertTrue(file.exists());
		Scanner scanner = new Scanner(file);
		assertTrue(scanner.nextLine().startsWith("#"));
		assertTrue(scanner.nextLine().startsWith("#"));
		
		double[][] result_c = new double[Fixture.NOF_LINES][TARGET_DIM];
		
		for (int i = 0; i < Fixture.NOF_LINES; i++) {
			result_c[i][0] = scanner.nextDouble();
			result_c[i][1] = scanner.nextDouble();
		}
		
		assertFalse(scanner.hasNext());
		scanner.close();
		
		double[][] result_java = null;
		File java = new File("primitive_rand-hitmds/genes_endo_4824_result_java.dat");
		if (!java.exists()) {
			result_java = new Revision37().useRandom(new PrimitiveRandom()).run(Fixture.genesEndo(Fixture.NOF_LINES));
			assertTrue(result_java.length == Fixture.NOF_LINES);
			
			FileWriter out = new FileWriter("primitive_rand-hitmds/genes_endo_4824_result_java.dat");
			for(double[] each: result_java) {
				out.write(Double.toString(each[0]));
				out.write(" ");
				out.write(Double.toString(each[1]));
				out.write("\n");
			} 
			out.close();
		} else {
			result_java = new double[Fixture.NOF_LINES][2];
			assertTrue(java.exists());
			Scanner in = new Scanner(java);
			
			for (int i = 0; i < Fixture.NOF_LINES; i++) {
				result_java[i][0] = in.nextDouble();
				result_java[i][1] = in.nextDouble();
			}			
			
		}	
		
		assertEquals(result_c.length, result_java.length);
		assertTrue(result_c.length == Fixture.NOF_LINES);
		assertEquals(result_c[0].length, result_java[0].length);
		assertTrue(result_c[0].length == TARGET_DIM);
		
		for (int line = 0; line < result_c.length; line++) {
			assertEquals(result_c[line][0], result_java[line][0], EPSILON);
			assertEquals(result_c[line][1], result_java[line][1], EPSILON);
		}		
		
	}

}

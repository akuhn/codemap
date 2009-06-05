package hitmds;

public class Pyramid {

	public static void main(String... args) {
		for (double[] point: new Hitmds2().run(new double[][] {
				{1,0,0},
				{-1,0,0},
				{0,1,0},
				{0,-1,0},
				{0,0,2}
		})) {
			for (double each: point) System.out.print(each+" ");
			System.out.println();
		}
	}
	
}

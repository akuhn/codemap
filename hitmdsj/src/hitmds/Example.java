package hitmds;

public class Example {

	public static void main(String... args) {
		
		double data[][] = new double[100][4];
		for (int n = 0; n < data.length; n++) {
			for (int m = 0; m < data[n].length; m++) {
				data[n][m] = Math.random() * Math.random() * 100;
			}
		}
		
		new Hitmds2().run(data);
		
	}
	
}

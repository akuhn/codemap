package ch.deif.meander.mds; 

/**
 * @author spupyrev
 * 20.11.2008
 */
public class HitMDSTest
{
	private static void check(double[][] res, int i, int j, String expected)
	{
		if (!String.valueOf(res[i][j]).equals(expected))
			System.err.println("Error! (Answer=" + String.valueOf(res[i][j]) + ", expected=" + expected + ")");
	}

	public static void main(String argc[])
	{
		String s = "0.000 0.122 0.125 0.120 0.117 0.813 0.797 0.817 " + "0.122 0.000 0.115 0.120 0.115 0.810 0.747 0.813 " + "0.125 0.115 0.000 0.105 0.117 0.833 0.803 0.800 "
				+ "0.120 0.120 0.105 0.000 0.112 0.823 0.780 0.803 " + "0.117 0.115 0.117 0.112 0.000 0.797 0.807 0.823 " + "0.813 0.810 0.833 0.823 0.797 0.000 0.022 0.031 "
				+ "0.797 0.747 0.803 0.780 0.807 0.022 0.000 0.022 " + "0.817 0.813 0.800 0.803 0.823 0.031 0.022 0.000 ";

		double[][] a = new double[8][8];
		String[] sp = s.split(" ");
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a.length; j++)
			{
				a[i][j] = Double.parseDouble(sp[i * a.length + j]);
				System.out.print("d[" + i + "][" + j + "] = " + sp[i * a.length + j] + ";");
				if (j == a.length - 1)
					System.out.println("");
				else
					System.out.print(" ");
			}

		double[][] res = new HitMDS().evaluate(a, -2);

		/*check(res, 0, 0, "-0.2617254159369086");
		check(res, 1, 1, "0.686928469480733");
		check(res, 3, 1, "0.6586771608545832");
		check(res, 7, 0, "0.6041796685845634");

		s = "0.000 1.000 1.000 1.000 0.000 1.000 1.000 1.000 0.000";

		a = new double[3][3];
		sp = s.split(" ");
		for (int i = 0; i < a.length; i++)
			for (int j = 0; j < a.length; j++)
				a[i][j] = Double.parseDouble(sp[i * a.length + j]);

		try
		{
			res = new HitMDS().evaluate(a, -2);
			for (int i = 0; i < res.length; i++)
				for (int j = 0; j < res[i].length; j++)
					if (Double.isNaN(res[i][j]) || Double.isInfinite(res[i][j]))
						throw new NumberFormatException();

			System.err.println("Exception expected!!!");
		}
		catch (NumberFormatException ignored)
		{
		}*/
	}
}

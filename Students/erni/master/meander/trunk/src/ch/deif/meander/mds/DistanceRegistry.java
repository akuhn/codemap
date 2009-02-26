package ch.deif.meander.mds;


import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.Arrays;

/**
 * @author spupyrev
 * 22.11.2008
 */
public class DistanceRegistry
{
	private static final double EPS = 1e-16;

	public static IDistance createEuclideanDistance()
	{
		return new IDistance()
		{
			public double distance(int dimension, double[] d1, double[] d2)
			{
				double sum = 0.0;
				for (int u = 0; u < dimension; u++)
				{
					double tmp = d1[u] - d2[u];
					sum += tmp * tmp;
				}

				return sqrt(sum);
			}
		};
	}

	public static IDistance createQuadraticEuclideanDistance()
	{
		return new IDistance()
		{
			public double distance(int dimension, double[] d1, double[] d2)
			{
				double sum = 0.0;
				for (int u = 0; u < dimension; u++)
				{
					double tmp = d1[u] - d2[u];
					tmp *= tmp;
					sum += tmp * tmp;
				}
				return sum;
			}
		};
	}

	public static IDistance createMinkowskiDistance()
	{
		final double mexponent = 1.0;

		return new IDistance()
		{
			public double distance(int dimension, double[] d1, double[] d2)
			{
				double sum = 0.0;
				for (int u = 0; u < dimension; u++)
				{
					double tmp = d1[u]++ - d2[u];
					tmp = abs(tmp);
					sum += pow(tmp, mexponent);
				}
				return pow(sum, 1. / mexponent);
			}
		};
	}

	public static IDistance createSpearmanDistance()
	{
		return new IDistance()
		{
			double f = 0.0;
			int idx1[], idx2[];
			int dim2;

			public double distance(final int dimension, double[] d1, double[] d2)
			{
				double s1 = 0.0;
				double s2 = 0.0;

				if (f == 0.0)
				{
					f = 1.0 / (dimension * (dimension * dimension - 1.0));

					dim2 = dimension * 2;

					idx1 = new int[dim2];
					idx2 = new int[dim2];
					Arrays.fill(idx1, 0);
					Arrays.fill(idx2, 0);
				}

				//multiplicity 
				int i = 0;
				for (int u = 0; u < dimension; u++)
				{
					if (++idx1[(int) (2.0 * (d1[u] - 1.0))] > 1)
						i++;
					if (++idx2[(int) (2.0 * (d2[u] - 1.0))] > 1)
						i++;
				}

				if (i > 0)//   only if necessary 
					for (int u = 0; u < dim2; u++)
					{
						if ((i = idx1[u]) > 1)
							s1 += i * (i * i - 1);
						if ((i = idx2[u]) > 1)
							s2 += i * (i * i - 1);
					}

				double sum = 0;
				for (int u = 0; u < dimension; u++)
				{
					double tmp = d1[u] - d2[u];
					sum += tmp * tmp;
					idx1[u] = idx2[u] = idx1[u + dimension] = idx2[u + dimension] = 0;
				}

				if (s1 == 0. && s2 == 0.)
				{
					return 6.0 * f * sum;
				}
				else
				{
					s2 *= f;
					if ((s1 *= f) > 1 - EPS)
						return (s2 > 1 - EPS) ? 0 : 1.;
					else if (s2 > 1 - EPS)
						return 1.0;
				}

				return 1.0 - (1.0 - 6.0 * (f * sum + (s1 + s2) / 12.)) / Math.sqrt((1. - s1) * (1. - s2));
			}
		};
	}

	/* (1 - Pearson)^expo correlation */
	public static IDistance createPearsonCorrelationDistance(final double mexponent)
	{
		return new IDistance()
		{
			public double distance(int dimension, double[] d1, double[] d2)
			{
				double mono1 = 0., mono2 = 0., mixed = 0., tmp, md1 = mean(dimension, d1), md2 = mean(dimension, d2);

				for (int u = 0; u < dimension; u++)
				{
					tmp = d1[u] - md1;
					mono1 += tmp * tmp;
					tmp = d2[u] - md2;
					mono2 += tmp * tmp;
					mixed += tmp * (d1[u] - md1);
				}

				tmp = Math.sqrt(mono1 * mono2);

				mixed /= (tmp < EPS) ? EPS : tmp;

				boolean u;
				/* allow correlated and anti-correlated patterns to be similar */
				if ((u = (mexponent < 0.0)) && mixed < 0.)
					mixed = -mixed;

				mono1 = u ? -mexponent : mexponent;

				return (mono1 == 1.) ? 1. - mixed : pow(1. - mixed, mexponent);
			}
		};
	}

	/* derivative of Pearson correlation w.r.t d2 or absolute to both directions (symmetric) */
	public static IDistance createCorr_Deriv_Vec()
	{
		return new IDistance()
		{
			public double distance(int dimension, double[] d1, double[] d2)
			{
				double mono1 = 0., mono2 = 0., mixed = 0., sum = 0., tmp, tmx, f, md1 = mean(dimension, d1), md2 = mean(dimension, d2);

				for (int u = 0; u < dimension; u++)
				{

					tmx = d1[u] - md1;
					mono1 += tmx * tmx;
					tmp = d2[u] - md2;
					mono2 += tmp * tmp;
					mixed += tmp * tmx;
				}

				tmp = sqrt(mono1 * mono2);

				if (tmp < EPS)
					tmp = EPS;
				tmp = 1. / tmp;

				if (mono1 < EPS)
					mono1 = EPS;
				mono1 = mixed / mono1;

				if (mono2 < EPS)
					mono2 = EPS;
				mono2 = mixed / mono2;

				for (int u = 0; u < dimension; u++)
				{
					f = ((d1[u] - md1) - mono2 * (d2[u] - md2)) * tmp;

					tmx = ((d2[u] - md2) - mono1 * (d1[u] - md1)) * tmp;
					f *= tmx;
					f = abs(f);

					sum += f;
				}

				/* average (absolute) contribution */
				return sum / dimension;
			}
		};

	}

	/* just a mean */
	private static double mean(int dimension, double[] p)
	{
		double sum = 0.;

		for (int i = 0; i < dimension; i++)
			sum += p[i];

		return sum / dimension;
	}

}

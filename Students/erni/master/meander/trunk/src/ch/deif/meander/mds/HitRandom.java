package ch.deif.meander.mds;


/**
 * @author spupyrev
 * 20.11.2008
 */
public class HitRandom implements IRandom
{
	int A = 1234567;
	int B = 7654321;
	int cur = 31415;

	public double nextDouble()
	{
		double tec = nextInt(100000) / 100000.0;
		return tec;
	}

	public int nextInt(int upper)
	{
		cur = (cur * A + B);
		int ret = cur % upper;
		if (ret < 0)
			ret += upper;
		return ret;
	}
}

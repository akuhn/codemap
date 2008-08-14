package magic;

public class All {

	public static boolean notNull(Object[] values) {
		for (Object o : values) {
			if (o == null) return false;
		}
		return true;
	}

}

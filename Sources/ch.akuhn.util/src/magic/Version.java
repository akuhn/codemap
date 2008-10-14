package magic;

public class Version {

	public static final String AUTHOR = "$author$";
	public static final String DATE = "$date$";
	public static final String REVISION = "$revision$";
	
	public static void main(String[] args) {
		System.out.println(number());
	}

	private static String number() {
		return REVISION;
	}
	
}

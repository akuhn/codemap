package magic;

/**
 * Indents lines while printing them.
 * 
 * @author Adrian Kuhn, 2008/07/21
 * @author Thanks to Yossi Gil for suggesting better class and method names.
 */

public class Tab {

	private String s;
	private final String tab;

	public Tab() {
		this("\t");
	}

	public Tab(String tab) {
		this.tab = tab;
		this.s = "";
	}

	public Tab more() {
		s += tab;
		return this;
	}

	public Tab less() {
		if (isEmpty())
			throw new IllegalStateException();
		s = s.substring(0, s.length() - tab.length());
		return this;
	}

	public String toString() {
		return s;
	}

	public boolean isEmpty() {
		return s.length() == 0;
	}

}
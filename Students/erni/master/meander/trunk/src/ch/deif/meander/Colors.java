package ch.deif.meander;

public final class Colors {

	public static final Colors HILLGREEN = new Colors(196, 236, 0);
	public static final Colors SHORE = new Colors(92, 142, 255);
	public static final Colors WATER = new Colors(0, 68, 255);

	private final int red, green, blue, rgb;

	public Colors(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.rgb = 0xFF000000 | red << 16 | green << 8 | blue;
	}
	
	public int scaledRGB(double factor) {
		if (factor < 0.0) return 0xFF000000;
		if (factor == 1.0) return rgb;
		return 0xFF000000
				| ((int) (red * factor) & 0xFF) << 16
				| ((int) (green * factor) & 0xFF) << 8 
				| ((int) (blue * factor) & 0xFF);
	}
	
	public Iterable<Colors> upto(Colors end, int steps) {
		// TODO
		throw null;
	}
	
	public int asRGB() {
		return rgb;
	}
	
}

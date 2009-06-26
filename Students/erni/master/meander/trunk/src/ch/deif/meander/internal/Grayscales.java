package ch.deif.meander.internal;

public final class Grayscales{

	public static final Grayscales HILLGREEN = new Grayscales(204);
	public static final Grayscales SHORE = new Grayscales(204);
	public static final Grayscales WATER = new Grayscales(255);

	private final int gray, rgb;

	public Grayscales(int gray) {
		this.gray = gray;
		this.rgb = 0xFF000000 | gray << 16 | gray << 8 | gray;
	}

	public Grayscales(double value) {
		this((int)(255*value));
	}

	public int scaledRGB(double factor) {
		if (factor < 0.0) return 0xFF000000;
		if (factor == 1.0) return rgb;
		int gray255 = ((int) (gray * factor) & 0xFF);
		return 0xFF000000
				| gray255 << 16
				| gray255 << 8 
				| gray255;
	}
	
	public int asRGB() {
		return rgb;
	}
	
}

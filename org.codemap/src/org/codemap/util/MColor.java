package org.codemap.util;

import static java.lang.Math.max;
import static java.lang.Math.min;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;

public final class MColor {

	public static final MColor HILLGREEN = new MColor(196, 236, 0);
	public static final MColor SHORE = new MColor(116, 169, 207);	
	public static final MColor WATER = new MColor(5, 112, 176);
	public static final MColor GRAY_204 = new MColor(204, 204, 204);
	public static final MColor GRAY_HILL = new MColor(185, 185, 185);
	public static final MColor GRAY_SHORE = new MColor(213, 213, 213);
    public static final MColor WHITE = new MColor(255, 255, 255);
    public static final MColor BLACK = new MColor(0, 0, 0);

	private final int red, green, blue, rgb;

	public MColor(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.rgb = 0xFF000000 | red << 16 | green << 8 | blue;
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public int scaledRGB(double factor) {
		if (factor < 0.0) return 0xFF000000;
		if (factor == 1.0) return rgb;
		return 0xFF000000
		| ((int) (red * factor) & 0xFF) << 16
		| ((int) (green * factor) & 0xFF) << 8 
		| ((int) (blue * factor) & 0xFF);
	}

	public int asRGB() {
		return rgb;
	}

	public Color asSWTColor(Device device) {
		return new Color(device, red, green, blue);
	}

	public byte[] asByte() {
		return new byte[]{(byte) red, (byte) green, (byte) blue};
	}

	public MColor blendWith(MColor other, double factor) {
		return new MColor(
				blendWith(this.red, other.red, factor),
				blendWith(this.green, other.green, factor),
				blendWith(this.blue, other.blue, factor));
	}

	private static int blendWith(int a, int b, double f) {
		return (int) min(255, max(0, a * (1-f) + b * f));
	}

}

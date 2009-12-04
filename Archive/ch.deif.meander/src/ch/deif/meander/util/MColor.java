package ch.deif.meander.util;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;

public final class MColor {

	public static final MColor HILLGREEN = new MColor(196, 236, 0);
//	public static final MColor SHORE = new MColor(92, 142, 255);
	public static final MColor SHORE = new MColor(116, 169, 207);	
//	public static final MColor WATER = new MColor(0, 68, 255);
        public static final MColor WATER = new MColor(5, 112, 176);      	
	public static final MColor GRAY_204 = new MColor(204, 204, 204);

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
	
}

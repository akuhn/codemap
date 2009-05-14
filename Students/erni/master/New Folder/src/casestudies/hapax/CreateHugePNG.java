package casestudies.hapax;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;

public class CreateHugePNG {

	public static void main(String... args) {
		
		PApplet pa = new PApplet();
		pa.init();
		pa.size(1200 * JunitMSR.VERSIONS.length, 1200, PConstants.JAVA2D);
		draw(pa);
		pa.save("huge.png");
		pa.destroy();
		
	}

	private static void draw(PApplet pa) {
		int n = 0;
		pa.background(0xFFFFFFFF);
		for (String each: JunitMSR.VERSIONS) {
			PImage img = pa.loadImage(each + ".png");
			int tx0 = n * 1100;
			int ty0 = 0;
			int sx0 = 0;
			int sy0 = 200;
			for (int x = 0; x < 1600; x++) {
				for (int y = 0; y < 1200; y++) {
					int color = img.get(sx0 + x, sy0 + y);
					if (color == 0xFFFFFFFF) continue;
					pa.set(tx0 + x, ty0 + y, color);
				}
			}
			n++;
		}
	}
	
}

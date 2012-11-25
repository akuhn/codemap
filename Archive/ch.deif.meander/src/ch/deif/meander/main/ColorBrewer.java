package ch.deif.meander.main;

import java.util.ArrayList;
import java.util.TreeMap;

import ch.deif.meander.util.MColor;

public class ColorBrewer {

	private ArrayList<MColor> colors;
	private int currentPosition;
	private TreeMap<String,MColor> forPackage;

	/**
	 * Color codes according to http://www.personal.psu.edu/cab38/ColorBrewer/ColorBrewer.html
	 */
	public ColorBrewer() {
		currentPosition = 0;
		forPackage = new TreeMap<String,MColor>();
		initColors();
	}

	private void initColors() {
		colors = new ArrayList<MColor>();
//		disabled for scheme with higher contrast
//		int[][] rgb = { { 141, 211, 199 },
//				{ 255, 255, 179 },
//				{ 190, 186, 218 },
//				{ 254, 128, 114 },
//				{ 128, 177, 211 },
//				{ 253, 180, 98 },
//				{ 179, 222, 105 },
//				{ 252, 205, 229 },
//				{ 217, 217, 217 },
//				{ 188, 128, 189 },
//				{ 204, 235, 197 },
//				{ 255, 237, 111 } };
		
		int[][] rgb = {
		        { 106, 61, 154 },
		        { 255, 255, 153 },
		        { 166, 206, 227 },
		        { 31, 120, 180 },
		        { 178, 223, 138 },
		        { 51, 160, 44 },
		        { 251, 154, 153 },
		        { 227, 26, 28 },
		        { 253, 191, 111 },
		        { 255, 127, 0 },
		        { 202, 178, 214 }
		};		
		
		for (int[] color: rgb) {
			addColor(color);
		}
	}

	private void addColor(int[] color) {
		colors.add(new MColor(color[0], color[1], color[2]));
	}

	private MColor nextColor() {
		if (currentPosition >= colors.size()) {
			currentPosition = 0;
		}
		return colors.get(currentPosition++);
	}

	public MColor forPackage(String packageName) {
		MColor packageColor = forPackage.get(packageName);
		if (packageColor == null) {
			packageColor = nextColor();
			forPackage.put(packageName, packageColor);
		}
		return packageColor;
	}

}

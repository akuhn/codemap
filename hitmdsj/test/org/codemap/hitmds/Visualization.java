package org.codemap.hitmds;

import processing.core.PApplet;

@SuppressWarnings("serial")
public class Visualization extends PApplet {

	private double[][] points;

	@Override
	public void setup() {
		Fixture.ZIPFILE = "../lib/hitmds2.zip";
		setSize(500,500);
		points = new Hitmds2().seed(42L).run(Fixture.genesEndo(250));
	}
	
	@Override
	public void draw() {
		for (double[] each: points) {
			float x = (float) each[0];
			float y = (float) each[1];
			ellipse(x * 100 + 250, y * 100 + 250, 5, 5);
		}
	}
	
}

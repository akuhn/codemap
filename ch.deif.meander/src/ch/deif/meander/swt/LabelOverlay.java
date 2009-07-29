package ch.deif.meander.swt;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import ch.akuhn.util.Get;
import ch.deif.meander.Location;
import ch.deif.meander.MapInstance;
import ch.deif.meander.util.MapScheme;

public class LabelOverlay extends SWTLayer {
	
	// TODO cache the labels

	private static final String ARIAL_NARROW = "Arial Narrow";
	private MapScheme<String> labelScheme;
	
	public LabelOverlay() {
		labelScheme = new MapScheme<String>() {
			@Override
			public String forLocation(ch.deif.meander.Point location) {
				String name = location.getDocument();
				int lastPathSeparator = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
				int lastDot = name.lastIndexOf('.');
				if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
				return name;
			}
		};
	}
	
	public LabelOverlay(MapScheme<String> scheme) {
		labelScheme = scheme;
	}

	@Override
	public void paintMap(MapInstance map, GC gc) {		
		String fname = ARIAL_NARROW;
		Font basefont = new Font(gc.getDevice(), fname, 12, SWT.NORMAL);
		Color white = new Color(gc.getDevice(), 255, 255, 255);
		Color black = new Color(gc.getDevice(), 0, 0, 0);
		gc.setFont(basefont); // required by labelsFor
		for (Label each: this.labelsFor(map, gc)) {
			FontData[] fontData = basefont.getFontData();
//			int height = (int) (Math.sqrt(each.getElevation()) * 2);
			for (FontData fd: fontData) fd.setHeight(each.fontHeight);
			Font font = new Font(gc.getDevice(), fontData);
			gc.setFont(font);
			gc.setAlpha(128);
			gc.setForeground(black);
			gc.drawText(each.text, each.bounds.x + 1, each.bounds.y + 1, SWT.DRAW_TRANSPARENT);
			gc.setAlpha(255);
			gc.setForeground(white);
			gc.drawText(each.text, each.bounds.x, each.bounds.y, SWT.DRAW_TRANSPARENT);
			font.dispose();
		}
		basefont.dispose();
		black.dispose();
		white.dispose();	
	}
	
	private Iterable<Label> labelsFor(MapInstance map, GC gc) {
		Iterable<Label> labels = makeLabels(map, gc);
		return new LayoutAlgorithm().layout(labels);
	}
	
	private Iterable<Label> makeLabels(MapInstance map, GC gc) {
		Collection<Label> labels = new ArrayList<Label>();
		Font basefont = new Font(gc.getDevice(), ARIAL_NARROW, 12, SWT.NORMAL);
		gc.setFont(basefont); 
		for (Location each: map.locations()) {
			FontData[] fontData = basefont.getFontData();
			int height = (int) (Math.sqrt(each.getElevation()) * 2);
			for (FontData fd: fontData) fd.setHeight(height);
			Font font = new Font(gc.getDevice(), fontData);
			gc.setFont(font);
			String text = labelScheme.forLocation(each.getPoint());
			Point extent = gc.stringExtent(text);
			labels.add(new Label(each.px, each.py, extent, height, text));
			font.dispose();
		}
		basefont.dispose();
		return labels;
	}

	private class LayoutAlgorithm {

		Collection<Label> layout;
		
		public Iterable<Label> layout(Iterable<Label> labels) {
			layout = new ArrayList<Label>();
			for (Label each: Get.sorted(labels)) mayebAddToLayout(each);
			return layout;
		}

		private void mayebAddToLayout(Label label) {
			for (double[] each: ORIENTATION) {
				label.changeOrientation(each[0], each[1]);
				if (intersectsLabels(label)) continue;
				layout.add(label);
				return;
			}
		}

		private boolean intersectsLabels(Label label) {
			for (Label each: layout) if (label.intersects(each)) return true;
			return false;
		}
		
	}

	private static final double[][] ORIENTATION = new double[][] {
			{-.5d,-1d}, // north
			{-.5d,-.5d}, // center
			{-.5d,0d}, // south
			{-.25d,-1d}, 
			{-.25d,-.5d}, 
			{-.25d,0d}, 
			{-.75d,-1d}, 
			{-.75d,-.5d}, 
			{-.75d,0d},
			{0d,-.5d}, 
			{-1d,-.5d}}; 
	
	private static class Label implements  Comparable<Label> {
		
		public int fontHeight;
		public Rectangle bounds;
		private int px, py;
		private String text;
		
		public Label(int px, int py, Point extent, int height, String text) {
			this.px = px;
			this.py = py;
			this.bounds = new Rectangle(px, py, extent.x, extent.y);
			this.fontHeight = height;
			this.text = text;
		}

		public boolean intersects(Label each) {
			return this.bounds.intersects(each.bounds);
		}
		
		public void changeOrientation(double dx, double dy) {
			bounds.x = (int) (px + dx * bounds.width);
			bounds.y = (int) (py + dy * bounds.height);
		}

		@Override
		public int compareTo(Label each) {
			return this.getArea() - each.getArea();
		}

		private int getArea() {
			return bounds.height * bounds.width;
		}
		
	}

}

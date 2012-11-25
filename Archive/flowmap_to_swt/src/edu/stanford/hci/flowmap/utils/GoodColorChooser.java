package edu.stanford.hci.flowmap.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class GoodColorChooser {
	
	// gotten from online
	public static final Color seashell = new Color(238,229,222);
	public static final Color cornsilk =  new Color(238,232,205);
	public static final Color azure = new Color(224,238,238);
	public static final Color beige = new Color(245,245,220);
	public static final Color papaya = new Color(255,239,213);
	public static final Color honeydew = new Color(224,238,224);
	public static final Color lemonchiffon = new Color(255,250,205);
	public static final Color lace = new Color(253,245,230);
	public static final Color gray = new Color(220,220,220);
	public static final Color babyblue = new Color(109,110,192);
	public static final Color peachpink = new Color(254,144,143);
	public static final Color orange = new Color(228,107,33);
	public static final Color green = new Color(149,196,58);
	public static final Color indigo = new Color(58,69,146);
	public static final Color rose = new Color(171,47,79);
	public static final Color darkolive = new Color(128,131,86);
	public static final Color olive = new Color(175,176,72);
	public static final Color brown = new Color(162,136,99);
	public static final Color bluegray = new Color(88,99,99);
	
	// colors stolen from Minard's Maps  :)
	public static final Color mgreen = new Color(129,145,93);
	public static final Color mgray = new Color(93,76,56);
	public static final Color mpink = new Color(198,141,113);
	public static final Color mblue = new Color(93, 109, 93);
	public static final Color myellow = new Color(186, 141, 72);


	// colors from ColorBrewer
	public static final Color cb_cyan = new Color(141,211,199);
	public static final Color cb_yellow = new Color(255,255,179);
	public static final Color cb_lavender = new Color(190,186,218);
	public static final Color cb_red = new Color(251,128,114);
	public static final Color cb_blue = new Color(128,177,211);
	public static final Color cb_orange = new Color(253,180,98);


	public static final Color[] goodBackgroundColors = {  
														  seashell,
														  cornsilk,
														  azure,
														  beige,
														  papaya,
														  honeydew,
														  lemonchiffon,
														  lace,
														  gray};
	public static final Color[] goodEdgeColors = { 
		 Color.BLACK,
												    green,
//													cb_cyan,
//													cb_yellow,
//													cb_lavender,
//													cb_red,
//													cb_blue,
//													cb_orange,
												    indigo,
												    rose /* munsell site */,
													babyblue,
												    darkolive,
												    mblue /* a few minard colors*/,
												    mgray,
												    mgreen,
												    mpink,
												    orange,
													Color.BLACK,
												    myellow};

	public static final Color[] goodNodeColors = { 
												   olive /* munsell olive*/,
												   brown /* munsell brown */,
												   bluegray};

	// 16 good colors... 
	public Color chooseBackgroundColor(Frame parent) {
		JDialog d = new JDialog(parent, "Select a Background Color", true);
		Container c = d.getContentPane();
		c.setLayout(new GridLayout(3,2));
		
		for (int i=0; i<goodBackgroundColors.length; i++){
			ColorIcon ci = new ColorIcon(60,40,goodBackgroundColors[i]);
			JButton b = new JButton(ci);
			c.add(b);
		}
		d.pack();
		d.setVisible(true);
		return null;
	}
	
	public Color chooseNodeColor(Frame parent) {
		JDialog d = new JDialog(parent, "Select Node Color", true);
		Container c = d.getContentPane();
		c.setLayout(new GridLayout(3,2));
		
		for (int i=0; i<goodNodeColors.length; i++){
			ColorIcon ci = new ColorIcon(60,40,goodNodeColors[i]);
			JButton b = new JButton(ci);
			c.add(b);
		}
		d.pack();
		d.setVisible(true);
		return null;
	}
	
	public Color chooseEdgeColor(Frame parent) {
		JDialog d = new JDialog(parent, "Select Edge Color", true);
		Container c = d.getContentPane();
		c.setLayout(new GridLayout(3,4));
		
		for (int i=0; i<goodEdgeColors.length; i++){
			ColorIcon ci = new ColorIcon(60,40,goodEdgeColors[i]);
			JButton b = new JButton(ci);
			c.add(b);
		}
		d.pack();
		d.setVisible(true);
		return null;
	}
	
	public static void main(String[] args) {
		
		GoodColorChooser gcc = new GoodColorChooser();
		gcc.chooseBackgroundColor(null);
		gcc.chooseEdgeColor(null);
		gcc.chooseNodeColor(null);
	}
	
	
	
	
	private class ColorIcon implements Icon {

		private int width;
		private int height;
		public Color color;
		
		ColorIcon(int w, int h, Color c) {
			this.width = w;
			this.height = h;
			this.color = c;
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
		 */
		public void paintIcon(Component c, Graphics g, int x, int y) {
			g.setColor(color);
			g.fillRect(x, y, width, height);
		}

		/* (non-Javadoc)
		 * @see javax.swing.Icon#getIconWidth()
		 */
		public int getIconWidth() {
			return width;
		}

		/* (non-Javadoc)
		 * @see javax.swing.Icon#getIconHeight()
		 */
		public int getIconHeight() {
			return height;
		}
		
	}
}

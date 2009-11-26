package edu.berkeley.guir.prefuse.render;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;

/**
 * A text renderer that allows the text to be split among multiple lines.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class MultiLineTextItemRenderer extends TextItemRenderer {

	public static final int DEFAULT_MAXLINES = 1;

	protected class TextEntry {
		public TextEntry(String n, int m, Font f) {
			name = n;
			maxlines = m;
			font = f;
		}
		String name;
		int maxlines;
		Font font;
	} //
	protected ArrayList m_attrList = new ArrayList();

	public void addTextAttribute(String attrName) {
		addTextAttribute(attrName, DEFAULT_MAXLINES, null);
	} //

	public void addTextAttribute(String attrName, int maxlines) {
		m_attrList.add(new TextEntry(attrName, maxlines, null));
	} //
	
	public void addTextAttribute(String attrName, int maxlines, Font font) {
		m_attrList.add(new TextEntry(attrName, maxlines, font));
	} //

	/**
	 * This method is not applicable in this class. Calling it
	 * causes an exception to be generated.
	 * @throws UnsupportedOperationException if called.
	 */
	protected String getText(VisualItem item) {
		throw new UnsupportedOperationException();
	} //
	
	protected String getText(VisualItem item, int entry) {
		String name = ((TextEntry)m_attrList.get(entry)).name;
		return item.getAttribute(name);
	} //
	
	public int getNumEntries() {
		return m_attrList.size();
	} //
	
	protected int getMaxLines(int entry) {
		return ((TextEntry)m_attrList.get(entry)).maxlines;
	} //
	
	protected Font getFont(VisualItem item, int entry) {
		Font f = ((TextEntry)m_attrList.get(entry)).font;
		if ( f == null ) { f = item.getFont();	}
		if ( f == null ) { f = m_font; }
		return f;
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.ShapeRenderer#getRawShape(edu.berkeley.guir.prefuse.VisualItem)
	 */
	protected Shape getRawShape(VisualItem item) {
		
		int w = 2*m_horizBorder;
		int h = 2*m_vertBorder;
		
		for ( int i = 0; i < getNumEntries(); i++ ) {
			Font font = getFont(item, i);
			FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(font);
			String  text = getText(item, i);
			int maxlines = getMaxLines(i); 	
			if ( text != null ) {
				h += fm.getHeight();
				w = Math.max(w, fm.stringWidth(text) + 2*m_horizBorder);
			}
		}
		getAlignedPoint(m_tmpPoint, item, w, h, m_xAlign, m_yAlign);
		m_textBox.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY(),w,h);
		return m_textBox;
	} //
	
	public Rectangle getEntryBounds(VisualItem item, int entry) {
		
		int dy = m_vertBorder, ew = 0, eh = 0;
		int w = 2*m_horizBorder;
		int h = 2*m_vertBorder;

		for ( int i = 0; i <= entry; i++ ) {
			Font font = getFont(item, i);
			FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(font);
			String  text = getText(item, i);
			int maxlines = getMaxLines(i);
			if ( text != null ) {
				h += fm.getHeight();
				w = Math.max(w, fm.stringWidth(text) + 2*m_horizBorder);
				if ( i < entry ) {
					dy += fm.getHeight();
				} else if ( i == entry ) {
					ew = fm.stringWidth(text) + 2*m_horizBorder;
					eh = fm.getHeight();
				}
			}
		}
		getAlignedPoint(m_tmpPoint, item, w, h, m_xAlign, m_yAlign);
		m_textBox.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY()+dy,ew,eh);
		return m_textBox.getBounds();
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.Renderer#render(java.awt.Graphics2D, edu.berkeley.guir.prefuse.VisualItem)
	 */
	public void render(Graphics2D g, VisualItem item) {
		Paint fillColor = item.getFillColor();
		Paint itemColor = item.getColor();
		Shape shape = getShape(item);
		if (shape != null) {
			switch (getRenderType(item)) {
				case RENDER_TYPE_DRAW :
					g.setPaint(itemColor);
					g.draw(shape);
					break;
				case RENDER_TYPE_FILL :
					g.setPaint(fillColor);
					g.fill(shape);
					break;
				case RENDER_TYPE_DRAW_AND_FILL :
					g.setPaint(fillColor);
					g.fill(shape);
					g.setPaint(itemColor);
					g.draw(shape);
					break;
			}

			Rectangle r = shape.getBounds();
			
			int h = r.y + m_vertBorder;
			for ( int i = 0; i < getNumEntries(); i++ ) {
				Font font = getFont(item, i);
				FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(font);
				String  text = getText(item, i);
				int maxlines = getMaxLines(i);
				if ( text != null ) {
					Color overlay = (Color)item.getVizAttribute("overlay_"+i);
					if ( overlay != null ) {
						g.setColor(overlay);
						Rectangle or = new Rectangle(r.x+m_horizBorder, h, 
							fm.stringWidth(text), fm.getHeight());
						g.fill(or);
					}
					
					g.setPaint(itemColor);
					g.setFont(font);
					g.drawString(text, r.x+m_horizBorder, h+fm.getAscent());
					h += fm.getHeight();
				}
			}
		}
	} //

} // end of class MultiLineTextItemRenderer

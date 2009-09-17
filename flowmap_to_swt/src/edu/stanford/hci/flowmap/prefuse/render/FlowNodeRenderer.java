package edu.stanford.hci.flowmap.prefuse.render;

import java.awt.FontMetrics;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.render.TextItemRenderer;
import edu.berkeley.guir.prefuse.util.FontLib;
import edu.stanford.hci.flowmap.main.Globals;
import edu.stanford.hci.flowmap.prefuse.item.FlowNodeItem;
import edu.stanford.hci.flowmap.prefuse.item.FlowRealNodeItem;

/**
 * The renderer for a FlowNodeItem
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class FlowNodeRenderer extends TextItemRenderer {

	protected RectangularShape rectBox  = new Rectangle2D.Float();
	protected RoundRectangle2D roundRectBox = new RoundRectangle2D.Float();
	protected Ellipse2D ellipseShape = new Ellipse2D.Float();
	
	protected Shape getRawShape(VisualItem item) {
		String s = getText(item);
		if ( s == null ) { s = ""; }
        	m_font = item.getFont();
        
        // make renderer size-aware
        double size = item.getSize();
        if ( size != 1 )
            m_font = FontLib.getFont(m_font.getName(), m_font.getStyle(),
                    (int)Math.round(size*m_font.getSize()));
        
        
		FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);
		double h = fm.getHeight() + size*2*m_vertBorder;
		double w = fm.stringWidth(s) + size*2*m_horizBorder;
		getAlignedPoint(m_tmpPoint, item, w, h, m_xAlign, m_yAlign);
		
		FlowRealNodeItem nodeItem = (FlowRealNodeItem)item;
		if(!nodeItem.isDummy()){
			if (Globals.showDestinationLabels)
				rectBox.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY(),w,h);
			else 
				rectBox.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY(),0,0);
			return rectBox;
		} else {
			
			if (Globals.showDummyNodeLabels) {
				rectBox.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY(),w,h);
				return rectBox;
			} else if (Globals.showDummyNodeCircles) {
				ellipseShape.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY(),4,4);
				return ellipseShape;
			} else {
				ellipseShape.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY(),0,0);
				return ellipseShape;
			}
		}
	} //
	
	public void render(java.awt.Graphics2D g,
            VisualItem item) {
//	    do not render the nodes
//		super.render(g, item);
	}
	
	protected String getText(VisualItem item) {
		FlowNodeItem fItem = (FlowNodeItem) item;
		
		String nodeName = fItem.getName();
		
		if (fItem.isDummy()) {
			if (Globals.showDummyNodeLabels) {
				return nodeName;
			} else {
				return null;
			}
		} else {
			if (Globals.showDestinationLabels)
				return nodeName;
			else
				return null;
		}
	}
}

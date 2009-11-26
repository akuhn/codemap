package edu.berkeley.guir.prefuse.render;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;

import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.util.FontLib;

/**
 * Renders an item as an image and a text string.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TextImageItemRenderer extends ShapeRenderer {

	public static final int ALIGNMENT_LEFT   = 0;
	public static final int ALIGNMENT_RIGHT  = 1;
	public static final int ALIGNMENT_CENTER = 2;
	public static final int ALIGNMENT_BOTTOM = 1;
	public static final int ALIGNMENT_TOP    = 0;
	
	protected ImageFactory m_images = new ImageFactory();
	
	protected String m_labelName = "label";
	protected String m_imageName = "image";
	
	protected int m_xAlign = ALIGNMENT_CENTER;
	protected int m_yAlign = ALIGNMENT_CENTER;
	protected int m_horizBorder = 3;
	protected int m_vertBorder  = 0;
	protected int m_imageMargin = 4;
    
    protected double m_imageSize = 1.0;
	
	protected Font m_font = new Font("SansSerif", Font.PLAIN, 10);
    protected RectangularShape m_imageBox  = new Rectangle2D.Float();
	protected Point2D     m_tmpPoint = new Point2D.Double();
    protected AffineTransform m_transform = new AffineTransform();

    /**
     * Rounds the corners of the bounding rectangle in which the text
     * string is rendered.
     * @param arcWidth the width of the curved corner
     * @param arcHeight the height of the curved corner
     */
    public void setRoundedCorner(int arcWidth, int arcHeight) {
        if ( (arcWidth == 0 || arcHeight == 0) && 
                !(m_imageBox instanceof Rectangle2D) ) {
            m_imageBox = new Rectangle2D.Float();
        } else {
            if ( !(m_imageBox instanceof RoundRectangle2D) )
                m_imageBox = new RoundRectangle2D.Float();
            ((RoundRectangle2D)m_imageBox)
                .setRoundRect(0,0,10,10,arcWidth,arcHeight);                    
        }
    } //
    
    /**
     * Sets the display-time scaling factor for images. This scaling
     * is applied at rendering time, to scale the image immediately upon
     * loading instead, refer to the {@link #setMaxImageDimensions(int,int)
     * setMaxImageDimensions} method.
     * @param size the scaling factor for displaying images
     */
    public void setImageSize(double size) {
        m_imageSize = size;
    } //
    
	/**
	 * Sets maximum image dimensions, used to control scaling of loaded images
     * This scaling is enforced immediately upon loading of the image, to 
     * scale the image at rendering time instead, refer to the 
     * {@link #setImageSize(double) setImageSize} method.
	 * @param width the max width of images (-1 for no limit)
	 * @param height the max height of images (-1 for no limit)
	 */
	public void setMaxImageDimensions(int width, int height) {
		m_images.setMaxImageDimensions(width, height);
	} //

	/**
	 * Get the attribute name of the text to draw.
	 * @return the text attribute name
	 */
	public String getTextAttributeName() {
		return m_labelName;
	} //
	
	/**
	 * Set the attribute name for the text to draw.
	 * @param name the text attribute name
	 */
	public void setTextAttributeName(String name) {
		m_labelName = name;
	} //

	/**
	 * Returns the text to draw. Subclasses can override this class to
	 * perform custom text rendering.
	 * @param item the item to represent as a <code>String</code>
	 * @return a <code>String</code> to draw
	 */
	protected String getText(VisualItem item) {
		return (String)item.getAttribute(m_labelName);
	} //
	
	/**
	 * Get the attribute name of the image to draw.
	 * @return the image attribute name
	 */
	public String getImageAttributeName() {
		return m_imageName;
	} //
	
	/**
	 * Set the attribute name for the image to draw.
	 * @param name the image attribute name
	 */
	public void setImageAttributeName(String name) {
		m_imageName = name;
	} //	
	
	/**
	 * Returns a URL for the image to draw. Subclasses can override 
	 * this class to perform custom image selection.
	 * @param item the item for which to select an image to draw
	 * @return an <code>Image</code> to draw
	 */
	protected String getImageLocation(VisualItem item) {
		return item.getAttribute(m_imageName);
	} //
	
	protected Image getImage(VisualItem item) {
		String imageLoc = getImageLocation(item);
		return ( imageLoc == null ? null : m_images.getImage(imageLoc) );
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.ShapeRenderer#getRawShape(edu.berkeley.guir.prefuse.VisualItem)
	 */
	protected Shape getRawShape(VisualItem item) {
        double size = item.getSize();
        
		// get image dimensions
		Image img = getImage(item);
        double is = size*m_imageSize;
		double ih = (img==null ? 0 : is*img.getHeight(null));
		double iw = (img==null ? 0 : is*img.getWidth(null));
		
		// get text dimensions
		m_font = item.getFont();
        if ( size != 1 )
          m_font = FontLib.getFont(m_font.getName(), m_font.getStyle(),
                      (int)Math.round(size*m_font.getSize()));
        
		String s = getText(item);
		if ( s == null ) { s = ""; }
		
        FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);
		int th = fm.getHeight();
		int tw = fm.stringWidth(s);
		
		double w = tw + iw + 
                size*(2*m_horizBorder + (tw>0 && iw>0 ? m_imageMargin : 0));
		double h = Math.max(th, ih) + size*2*m_vertBorder;
		
		getAlignedPoint(m_tmpPoint, item, w, h, m_xAlign, m_yAlign);
		m_imageBox.setFrame(m_tmpPoint.getX(),m_tmpPoint.getY(),w,h);
		return m_imageBox;
	} //
	
	/**
	 * Helper method, which calculates the top-left co-ordinate of a node
	 * given the node's alignment.
	 */
	protected static void getAlignedPoint(Point2D p, VisualItem item, 
            double w, double h, int xAlign, int yAlign)
    {
		double x = item.getX(), y = item.getY();
		if ( xAlign == ALIGNMENT_CENTER ) {
			x = x-(w/2);
		} else if ( xAlign == ALIGNMENT_RIGHT ) {
			x = x-w;
		}
		if ( yAlign == ALIGNMENT_CENTER ) {
			y = y-(h/2);
		} else if ( yAlign == ALIGNMENT_BOTTOM ) {
			y = y-h;
		}
		p.setLocation(x,y);
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.Renderer#render(java.awt.Graphics2D, edu.berkeley.guir.prefuse.VisualItem)
	 */
	public void render(Graphics2D g, VisualItem item) {
        Shape shape = getShape(item);
        if ( shape == null ) return;
        
        Paint itemColor = item.getColor();
        Paint fillColor = item.getFillColor();
        
        // render the fill
        int type = getRenderType(item);
        if ( type==RENDER_TYPE_FILL || type==RENDER_TYPE_DRAW_AND_FILL ) {
            g.setPaint(fillColor);
            g.fill(shape);
        }

        // render image and text next
        String s = getText(item);
		Image img = getImage(item);
		if ( s == null && img == null )
			return;
						
		Rectangle2D r = shape.getBounds2D();
        double size = item.getSize();
		double x = r.getMinX() + size*m_horizBorder;
			
        // render image
		if ( img != null ) {
			Composite comp = g.getComposite();
               // enable alpha blending for image, if needed
			if ( fillColor instanceof Color) {
				int alpha = ((Color)fillColor).getAlpha();
				if ( alpha < 255 ) {
					AlphaComposite alphaComp = 
						AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 
							((float)alpha)/255);
					g.setComposite(alphaComp);
				}
			}
            
            double is = m_imageSize*size;
            double w = is*img.getWidth(null);
            double h = is*img.getHeight(null);
            double y = r.getMinY() + (r.getHeight()-h)/2;
            
            m_transform.setTransform(is,0,0,is,x,y);
            g.drawImage(img, m_transform, null);

			x += w + (s!=null ? size*m_imageMargin : 0);
			g.setComposite(comp);
		}
        
        // render text
		if ( s != null ) {
			g.setPaint(itemColor);
			g.setFont(m_font);
			FontMetrics fm = DEFAULT_GRAPHICS.getFontMetrics(m_font);
			double y = r.getY() + (r.getHeight()-fm.getHeight())/2+fm.getAscent();
			g.drawString(s, (float)x, (float)y);
		}
	
        // now draw border
		if (type==RENDER_TYPE_DRAW || type==RENDER_TYPE_DRAW_AND_FILL) {
		    Stroke st = g.getStroke();
		    Stroke ist = getStroke(item);
		    if ( ist != null ) g.setStroke(ist);
            g.setPaint(itemColor);
            g.draw(shape);
            g.setStroke(st);
        }
	} //
	
    /**
     * Returns the image factory used by this renderer.
     * @return the image factory
     */
    public ImageFactory getImageFactory() {
        return m_images;
    } //
    
    /**
     * Sets the image factory used by this renderer.
     * @param ifact the image factory
     */
    public void setImageFactory(ImageFactory ifact) {
        m_images = ifact;
    } //
    
	/**
	 * Get the horizontal alignment of this node with respect to it's
	 * location co-ordinate.
	 * @return the horizontal alignment
	 */
	public int getHorizontalAlignment() {
		return m_xAlign;
	} //
	
	/**
	 * Get the vertical alignment of this node with respect to it's
	 * location co-ordinate.
	 * @return the vertical alignment
	 */
	public int getVerticalAlignment() {
		return m_yAlign;
	} //
	
	/**
	 * Set the horizontal alignment of this node with respect to it's
	 * location co-ordinate.
	 * @param align the horizontal alignment
	 */	
	public void setHorizontalAlignment(int align) {
		m_xAlign = align;
	} //
	
	/**
	 * Set the vertical alignment of this node with respect to it's
	 * location co-ordinate.
	 * @param align the vertical alignment
	 */	
	public void setVerticalAlignment(int align) {
		m_yAlign = align;
	} //
	
    /**
     * Returns the amount of padding in pixels between text 
     * and the border of this item along the horizontal dimension.
     * @return the horizontal padding
     */
    public int getHorizontalPadding() {
        return m_horizBorder;
    } //
    
    /**
     * Sets the amount of padding in pixels between text 
     * and the border of this item along the horizontal dimension.
     * @param xpad the horizontal padding to set
     */
    public void setHorizontalPadding(int xpad) {
        m_horizBorder = xpad;
    } //
    
    /**
     * Returns the amount of padding in pixels between text 
     * and the border of this item along the vertical dimension.
     * @return the vertical padding
     */
    public int getVerticalPadding() {
        return m_vertBorder;
    } //
    
    /**
     * Sets the amount of padding in pixels between text 
     * and the border of this item along the vertical dimension.
     * @param ypad the vertical padding
     */
    public void setVerticalPadding(int ypad) {
        m_vertBorder = ypad;
    } //
    
    /**
     * Returns the amount of spacing in pixels between image and text.
     * @return the space in pixels between image and text
     */
    public int getImageSpacing() {
        return m_imageMargin;
    } //
    
    /**
     * Sets the amount of padding in pixels between image and text.
     * @param s the space in pixels between image and text
     */
    public void setImageSpacing(int s) {
        m_imageMargin = s;
    } //
    
} // end of class TextImageItemRenderer

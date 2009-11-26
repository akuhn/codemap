package edu.berkeley.guir.prefuse;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;

import edu.berkeley.guir.prefuse.activity.Activity;
import edu.berkeley.guir.prefuse.activity.SlowInSlowOutPacer;
import edu.berkeley.guir.prefuse.event.ControlEventMulticaster;
import edu.berkeley.guir.prefuse.event.ControlListener;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.berkeley.guir.prefuse.util.ColorLib;
import edu.berkeley.guir.prefuse.util.FontLib;
import edu.berkeley.guir.prefuse.util.display.Clip;
import edu.berkeley.guir.prefuse.util.display.ExportDisplayAction;
import edu.berkeley.guir.prefuse.util.display.ToolTipManager;

/**
 * <p>User interface component that provides an interactive visualization 
 * of a graph. The Display is responsible for drawing items to the
 * screen and providing callbacks for user interface actions such as
 * mouse and keyboard events. A Display must be associated with an
 * {@link edu.berkeley.guir.prefuse.ItemRegistry ItemRegistry} from 
 * which it pulls the items to visualize.</p>
 * 
 * <p>The {@link edu.berkeley.guir.prefuse.event.ControlListener ControlListener}
 * interface provides the various available user interface callbacks. The
 * {@link edu.berkeley.guir.prefusex.controls} package contains a number
 * of pre-built <code>ControlListener</code> implementations for common
 * interactions.</p>
 * 
 * <p>The Display class also supports arbitrary graphics transforms through
 * the <code>java.awt.geom.AffineTransform</code> class. The 
 * {@link #setTransform(java.awt.geom.AffineTransform) setTransform} method
 * allows arbitrary transforms to be applied, while the 
 * {@link #pan(double,double) pan} and 
 * {@link #zoom(java.awt.geom.Point2D,double) zoom}
 * methods provide convenience methods that appropriately update the current
 * transform to achieve panning and zooming on the presentation space.</p>
 * 
 * <p>Additionally, each Display instance also supports use of a text editor
 * to facilitate direct editing of text. See the various
 * {@link #editText(edu.berkeley.guir.prefuse.VisualItem, String) editItem}
 * methods.</p>
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 * @see ItemRegistry
 * @see edu.berkeley.guir.prefuse.event.ControlListener
 * @see edu.berkeley.guir.prefusex.controls
 */
public class Display extends JComponent {

	protected ItemRegistry    m_registry;
	protected ControlListener m_listener;
	protected BufferedImage   m_offscreen;
    protected Clip            m_clip = new Clip();
    protected boolean         m_showDebug = false;
    protected boolean         m_repaint = false;
    protected boolean         m_highQuality = false;
    
    // transform variables
    protected AffineTransform   m_transform  = new AffineTransform();
    protected AffineTransform   m_itransform = new AffineTransform();
    protected TransformActivity m_transact = new TransformActivity();
    protected Point2D m_tmpPoint = new Point2D.Double();
    
    // frame count and debugging output
    protected double frameRate;
    protected int  nframes = 0;
    private int  sampleInterval = 10;
    private long mark = -1L;
    
    // text editing variables
    private JTextComponent m_editor;
    private boolean        m_editing;
    private VisualItem     m_editItem;
    private String         m_editAttribute;
    
    private ToolTipManager m_ttipManager;
	
    /**
	 * Constructor. Creates a new display instance. You will need to
	 * associate this Display with an ItemRegistry for it to display
	 * anything.
	 */
    public Display() {
    	this(null);
    } //
    
	/**
	 * Creates a new display instance associated with the given
	 * ItemRegistry.
	 * @param registry the ItemRegistry from which this Display
	 *  should get the items to visualize.
	 */
	public Display(ItemRegistry registry) {
        setDoubleBuffered(false);
        setBackground(Color.WHITE);
        
        // initialize text editor
        m_editing = false;
        m_editor = new JTextField();
        m_editor.setBorder(null);
        m_editor.setVisible(false);
        this.add(m_editor);
        
        // register input event capturer
		InputEventCapturer iec = new InputEventCapturer();
		addMouseListener(iec);
		addMouseMotionListener(iec);
		addMouseWheelListener(iec);
		addKeyListener(iec);
        
        // add debugging output control
        registerKeyboardAction(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        m_showDebug = !m_showDebug;
                    }
                },
                "debug info", KeyStroke.getKeyStroke("ctrl D"), WHEN_FOCUSED);
        // add image output control
        registerKeyboardAction(
                new ExportDisplayAction(this),
                "export display", KeyStroke.getKeyStroke("ctrl E"), WHEN_FOCUSED);
        
        setItemRegistry(registry);
        setSize(400,400); // set a default size
	} //

    /**
     * Determines if a debugging string is printed on the Display.
     * @param d true to show debugging info, false otherwise
     */
    public void setDebug(boolean d) {
        m_showDebug = d;
    } //
    
    /**
     * Indicates if a debugging string is being printed on the Display.
     * @return true if debugging info is shown, false otherwise
     */
    public boolean getDebug() {
        return m_showDebug;
    } //
    
    public void setUseCustomTooltips(boolean s) {
        if ( s && m_ttipManager == null ) {
            m_ttipManager = new ToolTipManager(this);
            String text = super.getToolTipText();
            super.setToolTipText(null);
            m_ttipManager.setToolTipText(text);
            this.addMouseMotionListener(m_ttipManager);
        } else if ( !s && m_ttipManager != null ) {
            this.removeMouseMotionListener(m_ttipManager);
            String text = m_ttipManager.getToolTipText();
            m_ttipManager.setToolTipText(null);
            super.setToolTipText(text);
            m_ttipManager = null;
        }
    } //
    
    public ToolTipManager getToolTipManager() {
        return m_ttipManager;
    } //
    
    public void setToolTipText(String text) {
        if ( m_ttipManager != null ) {
            m_ttipManager.setToolTipText(text);
        } else {
            super.setToolTipText(text);
        }
    } //
    
	/**
	 * Set the size of the Display.
	 * @see java.awt.Component#setSize(int, int)
	 */
	public void setSize(int width, int height) {
		m_offscreen = null;
        setPreferredSize(new Dimension(width,height));
		super.setSize(width, height);
	} //
	
	/**
	 * Set the size of the Display.
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	public void setSize(Dimension d) {
		m_offscreen = null;
        setPreferredSize(d);
		super.setSize(d);
	} //

    /**
     * Reshapes (moves and resizes) this component.
     */
    public void reshape(int x, int y, int w, int h) {
        m_offscreen = null;
        super.reshape(x,y,w,h);
    } //
    
    /**
     * Sets the font used by this Display. This determines the font used
     * by this Display's text editor.
     */
    public void setFont(Font f) {
        super.setFont(f);
        m_editor.setFont(f);
    } //
    
    /**
     * Determines if the Display uses a higher quality rendering, using
     * anti-aliasing. This causes drawing to be much slower, however, and
     * so is disabled by default.
     * @param on true to enable anti-aliased rendering, false to disable it
     */
    public void setHighQuality(boolean on) {
        m_highQuality = on;
    } //
    
    /**
     * Indicates if the Display is using high quality (return value true) or
     * regular quality (return value false) rendering.
     * @return true if high quality rendering is enabled, false otherwise
     */
    public boolean isHighQuality() {
        return m_highQuality;
    } //
    
    /**
     * Returns the item registry used by this display.
     * @return this Display's ItemRegistry
     */
    public ItemRegistry getRegistry() {
        return m_registry;
    } //
    
    /**
     * Set the ItemRegistry associated with this Display. This Display
     * will render the items contained in the provided registry. If this
     * Display is already associated with a different ItemRegistry, the
     * Display unregisters itself with the previous registry.
     * @param registry the ItemRegistry to associate with this Display.
     *  A value of null associates this Display with no ItemRegistry
     *  at all.
     */
    public void setItemRegistry(ItemRegistry registry) {
        if ( m_registry == registry ) {
            // nothing need be done
            return;
        } else if ( m_registry != null ) {
            // remove this display from it's previous registry
            m_registry.removeDisplay(this);
        }
        m_registry = registry;
        if ( registry != null )
            m_registry.addDisplay(this);
    } //

    // ========================================================================
    // == TRANSFORM METHODS ===================================================
    
    /**
     * Set the 2D AffineTransform (e.g., scale, shear, pan, rotate) used by
     * this display before rendering graph items. The provided transform
     * must be invertible, otherwise an expection will be thrown. For simple
     * panning and zooming transforms, you can instead use the provided
     * pan() and zoom() methods.
     */
    public void setTransform(AffineTransform transform) 
        throws NoninvertibleTransformException
    {
        m_transform = transform;
        m_itransform = m_transform.createInverse();
    } //
    
    /**
     * Returns a reference to the AffineTransformation used by this Display.
     * Changes made to this reference will likely corrupt the state of 
     * this display. Use setTransform() to safely update the transform state.
     * @return the AffineTransform
     */
    public AffineTransform getTransform() {
        return m_transform;
    } //
    
    /**
     * Returns a reference to the inverse of the AffineTransformation used by
     * this display. Changes made to this reference will likely corrupt the
     * state of this display.
     * @return the inverse AffineTransform
     */
    public AffineTransform getInverseTransform() {
        return m_itransform;
    } //
    
    /**
     * Gets the absolute co-ordinate corresponding to the given screen
     * co-ordinate.
     * @param screen the screen co-ordinate to transform
     * @param abs a reference to put the result in. If this is the same
     *  object as the screen co-ordinate, it will be overridden safely. If
     *  this value is null, a new Point2D instance will be created and 
     *  returned.
     * @return the point in absolute co-ordinates
     */
    public Point2D getAbsoluteCoordinate(Point2D screen, Point2D abs) {
        return m_itransform.transform(screen, abs);
    } //
    
    /**
     * Returns the current scale (i.e. zoom value).
     * @return the current scale. This is the
     *  scaling factor along the x-dimension, so be careful when
     *  using this value in non-uniform scaling cases.
     */
    public double getScale() {
        return m_transform.getScaleX();
    } //
    
    /**
     * Returns the x-coordinate of the top-left of the display, 
     * in absolute co-ordinates
     * @return the x co-ord of the top-left corner, in absolute coordinates
     */
    public double getDisplayX() {
        return -m_transform.getTranslateX();
    } //
    
    /**
     * Returns the y-coordinate of the top-left of the display, 
     * in absolute co-ordinates
     * @return the y co-ord of the top-left corner, in absolute coordinates
     */
    public double getDisplayY() {
        return -m_transform.getTranslateY();
    } //
    
    /**
     * Pans the view provided by this display in screen coordinates.
     * @param dx the amount to pan along the x-dimension, in pixel units
     * @param dy the amount to pan along the y-dimension, in pixel units
     */
    public void pan(double dx, double dy) {
        double panx = ((double)dx) / m_transform.getScaleX();
        double pany = ((double)dy) / m_transform.getScaleY();
        panAbs(panx,pany);
    } //
    
    /**
     * Pans the view provided by this display in absolute (i.e. non-screen)
     * coordinates.
     * @param dx the amount to pan along the x-dimension, in absolute co-ords
     * @param dy the amount to pan along the y-dimension, in absolute co-ords
     */
    public void panAbs(double dx, double dy) {
        m_transform.translate(dx, dy);
        try {
            m_itransform = m_transform.createInverse();
        } catch ( Exception e ) { /*will never happen here*/ }
    } //
    
    /**
     * Pans the display view to center on the provided point in 
     * screen (pixel) coordinates.
     * @param x the x-point to center on, in screen co-ords
     * @param y the y-point to center on, in screen co-ords
     */
    public void panTo(Point2D p) {
        m_itransform.transform(p, m_tmpPoint);
        panToAbs(m_tmpPoint);
    } //
    
    /**
     * Pans the display view to center on the provided point in 
     * absolute (i.e. non-screen) coordinates.
     * @param x the x-point to center on, in absolute co-ords
     * @param y the y-point to center on, in absolute co-ords
     */
    public void panToAbs(Point2D p) {
        double x = p.getX(); x = (Double.isNaN(x) ? 0 : x);
        double y = p.getY(); y = (Double.isNaN(y) ? 0 : y);
        double w = getWidth() /(2*m_transform.getScaleX());
        double h = getHeight()/(2*m_transform.getScaleY());
        
        double dx = w-x-m_transform.getTranslateX();
        double dy = h-y-m_transform.getTranslateY();
        m_transform.translate(dx, dy);
        try {
            m_itransform = m_transform.createInverse();
        } catch ( Exception e ) { /*will never happen here*/ }
    } //

    /**
     * Zooms the view provided by this display by the given scale,
     * anchoring the zoom at the specified point in screen coordinates.
     * @param p the anchor point for the zoom, in screen coordinates
     * @param scale the amount to zoom by
     */
    public void zoom(final Point2D p, double scale) {
        m_itransform.transform(p, m_tmpPoint);
        zoomAbs(m_tmpPoint, scale);
    } //    
    
    /**
     * Zooms the view provided by this display by the given scale,
     * anchoring the zoom at the specified point in absolute coordinates.
     * @param p the anchor point for the zoom, in absolute
     *  (i.e. non-screen) co-ordinates
     * @param scale the amount to zoom by
     */
    public void zoomAbs(final Point2D p, double scale) {;
        double zx = p.getX(), zy = p.getY();
        m_transform.translate(zx, zy);
        m_transform.scale(scale,scale);
        m_transform.translate(-zx, -zy);
        try {
            m_itransform = m_transform.createInverse();
        } catch ( Exception e ) { /*will never happen here*/ }
    } //

    public void animatePan(double dx, double dy, long duration) {
        double panx = dx / m_transform.getScaleX();
        double pany = dy / m_transform.getScaleY();
        animatePanAbs(panx,pany,duration);
    } //
    
    public void animatePanAbs(double dx, double dy, long duration) {
        m_transact.pan(dx,dy,duration);
    } //
    
    public void animatePanTo(Point2D p, long duration) {
        Point2D pp = new Point2D.Double();
        m_itransform.transform(p,pp);
        animatePanToAbs(pp,duration);
    } //
    
    public void animatePanToAbs(Point2D p, long duration) {
        m_tmpPoint.setLocation(0,0);
        m_itransform.transform(m_tmpPoint,m_tmpPoint);
        double x = p.getX(); x = (Double.isNaN(x) ? 0 : x);
        double y = p.getY(); y = (Double.isNaN(y) ? 0 : y);
        double w = ((double)getWidth()) /(2*m_transform.getScaleX());
        double h = ((double)getHeight())/(2*m_transform.getScaleY());
        double dx = w-x+m_tmpPoint.getX();
        double dy = h-y+m_tmpPoint.getY();
        animatePanAbs(dx,dy,duration);
    } //
    
    public void animateZoom(final Point2D p, double scale, long duration) {
        Point2D pp = new Point2D.Double();
        m_itransform.transform(p,pp);
        animateZoomAbs(pp,scale,duration);
    } //
    
    public void animateZoomAbs(final Point2D p, double scale, long duration) {
        m_transact.zoom(p,scale,duration);
    } //
    
    /**
     * TODO: clean this up to be more general...
     * TODO: change mechanism so that multiple transform
     *        activities can be running at once?
     */
    private class TransformActivity extends Activity {
        private double[] src, dst;
        private AffineTransform m_at;
        public TransformActivity() {
            super(2000,20,0);
            src = new double[6];
            dst = new double[6];
            m_at = new AffineTransform();
            setPacingFunction(new SlowInSlowOutPacer());
        } //
        private AffineTransform getTransform() {
            if ( this.isScheduled() )
                m_at.setTransform(dst[0],dst[1],dst[2],dst[3],dst[4],dst[5]);
            else
                m_at.setTransform(m_transform);
            return m_at;
        } //
        public void pan(double dx, double dy, long duration) {
            AffineTransform at = getTransform();
            this.cancel();
            setDuration(duration);
            at.translate(dx,dy);
            at.getMatrix(dst);
            m_transform.getMatrix(src);
            this.runNow();
        } //
        public void zoom(final Point2D p, double scale, long duration) {
            AffineTransform at = getTransform();
            this.cancel();
            setDuration(duration);
            double zx = p.getX(), zy = p.getY();
            at.translate(zx, zy);
            at.scale(scale,scale);
            at.translate(-zx, -zy);
            at.getMatrix(dst);
            m_transform.getMatrix(src);
            this.runNow();
        } //
        protected void run(long elapsedTime) {
            double f = getPace(elapsedTime);
            m_transform.setTransform(
                src[0] + f*(dst[0]-src[0]),
                src[1] + f*(dst[1]-src[1]),
                src[2] + f*(dst[2]-src[2]),
                src[3] + f*(dst[3]-src[3]),
                src[4] + f*(dst[4]-src[4]),
                src[5] + f*(dst[5]-src[5])
            );
            try {
                m_itransform = m_transform.createInverse();
            } catch ( Exception e ) { /* won't happen */ }
            repaint();
        } //
    } // end of inner class TransformActivity
    
    // ========================================================================
    // == RENDERING METHODS ===================================================
    
	/**
	 * Returns the offscreen buffer used by this component for 
	 *  double-buffering.
	 * @return the offscreen buffer
	 */
	public BufferedImage getOffscreenBuffer() {
		return m_offscreen;
	} //
	
    /**
     * Creates a new buffered image to use as an offscreen buffer.
     */
	protected BufferedImage getNewOffscreenBuffer() {
        return (BufferedImage)createImage(getSize().width, getSize().height);
	} //
	
	/**
	 * Saves a copy of this display as an image to the specified output stream.
	 * @param output the output stream to write to.
	 * @param format the image format (e.g., "JPG", "PNG").
	 * @param scale how much to scale the image by.
	 * @return true if image was successfully saved, false if an error occurred.
	 */
	public boolean saveImage(OutputStream output, String format, double scale) {
	    try {
	        Dimension d = new Dimension((int)(scale*getWidth()),(int)(scale*getHeight()));
	        BufferedImage img = (BufferedImage) createImage(d.width, d.height);
	        Graphics2D g = (Graphics2D)img.getGraphics();
	        Point2D p = new Point2D.Double(0,0);
	        zoom(p, scale);
	        //boolean q = isHighQuality();
	        //setHighQuality(true);
	        paintDisplay(g,d);
	        //setHighQuality(q);
	        zoom(p, 1/scale);
	        ImageIO.write(img,format,output);
	        return true;
	    } catch ( Exception e ) {
	        e.printStackTrace();
	        return false;
	    }
	} //
	
    /**
     * Updates this display
     */
	public void update(Graphics g) {
		paint(g);
	} //

    public void repaint() {
        if ( !m_repaint ) {
            m_repaint = true;
            super.repaint();
        }
    } //
    
    /**
     * Paints the offscreen buffer to the provided graphics context.
     * @param g the Graphics context to paint to
     */
	protected void paintBufferToScreen(Graphics g) {
        int x = 0, y = 0;
        BufferedImage img = m_offscreen;
        //if ( m_clip != null ) {
        //    x = m_clip.getX();
        //    y = m_clip.getY();
        //    img = m_offscreen.getSubimage(x,y,m_clip.getWidth(),m_clip.getHeight());
        //}
		g.drawImage(img, x, y, null);
	} //

	/**
	 * Immediately repaints the contents of the offscreen buffer
	 * to the screen. This bypasses the usual rendering loop.
	 */
	public void repaintImmediate() {
		Graphics g = this.getGraphics();
		if (g != null && m_offscreen != null) {
			paintBufferToScreen(g);
		}
	} //

    /**
     * Sets the transform of the provided Graphics context to be the
     * transform of this Display and sets the desired rendering hints.
     * @param g the Graphics context to prepare.
     */
    protected void prepareGraphics(Graphics2D g) {
        if ( m_transform != null )
            g.setTransform(m_transform);
        setRenderingHints(g);
    } //
    
	/**
	 * Sets the rendering hints that should be used while drawing
	 * the visualization to the screen. Subclasses can override
     * this method to set hints as desired.
	 * @param g the Graphics context on which to set the rendering hints
	 */
	protected void setRenderingHints(Graphics2D g) {
		if ( m_highQuality ) {
		    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		            RenderingHints.VALUE_ANTIALIAS_ON);
		} else {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}
        g.setRenderingHint(
            RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(
			RenderingHints.KEY_INTERPOLATION,
			RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	} //
	
    /**
     * Returns a string showing debugging info such as number of visualized
     * items and the current frame rate.
     * @return the debug string
     */
    protected String getDebugString() {
        float fr = Math.round(frameRate*100f)/100f;
        Runtime rt = Runtime.getRuntime();
        long tm = rt.totalMemory()/1000000L;
        long mm = rt.maxMemory()/1000000L;
        
        StringBuffer sb = new StringBuffer();
        sb.append("frame rate: ").append(fr).append("fps - ");
        sb.append(m_registry.size()).append(" items (");
        sb.append(m_registry.size(ItemRegistry.DEFAULT_NODE_CLASS));
        sb.append(" nodes, ");
        sb.append(m_registry.size(ItemRegistry.DEFAULT_EDGE_CLASS));
        sb.append(" edges) fonts(").append(FontLib.getCacheMissCount());
        sb.append(") colors(");
        sb.append(ColorLib.getCacheMissCount()).append(')');
        sb.append(" mem(");
        sb.append(tm).append("M / ");
        sb.append(mm).append("M)");
        return sb.toString();
    } //
    
	/**
	 * Paint routine called <i>before</i> items are drawn. Subclasses should
	 * override this method to perform custom drawing.
	 * @param g the Graphics context to draw into
	 */
	protected void prePaint(Graphics2D g) {
	} //

	/**
	 * Paint routine called <i>after</i> items are drawn. Subclasses should
	 * override this method to perform custom drawing.
	 * @param g the Graphics context to draw into
	 */
	protected void postPaint(Graphics2D g) {
	} //

	/**
	 * Draws the visualization to the screen. Draws each visible item to the
	 * screen in a rendering loop. Rendering order can be controlled by adding
	 * the desired Comparator to the Display's ItemRegistry.
	 */
	public void paintComponent(Graphics g) {
		if  (m_offscreen == null) {
			m_offscreen = getNewOffscreenBuffer();
        }
	    Graphics2D g2D = (Graphics2D) m_offscreen.getGraphics();
        //Graphics2D g2D = (Graphics2D)g;
        
	    // paint the visualization
		paintDisplay(g2D, getSize());

		paintBufferToScreen(g);		
		g2D.dispose();
        m_repaint = false;
        
        // compute frame rate
        nframes++;
        if ( mark < 0 ) {
            mark = System.currentTimeMillis();
            nframes = 0;
        } else if ( nframes == sampleInterval ){
            long t = System.currentTimeMillis();
            frameRate = (1000.0*nframes)/(t-mark);
            mark = t;
            nframes = 0;
            //System.out.println("frameRate: " + frameRate);
        }
	} //
    
	public void paintDisplay(Graphics2D g2D, Dimension d) {
	    // paint background
		g2D.setColor(getBackground());
		g2D.fillRect(0, 0, d.width, d.height);
        
        // show debugging info?
		if ( m_showDebug ) {
            g2D.setFont(getFont());
            g2D.setColor(getForeground());
            g2D.drawString(getDebugString(), 5, 15);     
        }

		prepareGraphics(g2D);
		prePaint(g2D);
        
		g2D.setColor(Color.BLACK);
		synchronized (m_registry) {
            m_clip.setClip(0,0,d.width,d.height);
            m_clip.transform(m_itransform);
            Iterator items = m_registry.getItems();
            while (items.hasNext()) {
                VisualItem vi = (VisualItem) items.next();
                Renderer renderer = vi.getRenderer();
                Rectangle2D b = renderer.getBoundsRef(vi);
                
                if ( m_clip.intersects(b) )
                    renderer.render(g2D, vi);
            }
		}

		postPaint(g2D);
	} //
	
    /**
     * Clears the specified region of the display (in screen co-ordinates)
     * in the display's offscreen buffer. The cleared region is replaced 
     * with the background color. Call the repaintImmediate() method to
     * have this change directly propagate to the screen.
     * @param r a Rectangle specifying the region to clear, in screen co-ords
     */
	public void clearRegion(Rectangle r) {
		Graphics2D g2D = (Graphics2D) m_offscreen.getGraphics();
		if (g2D != null) {
			g2D.setColor(this.getBackground());
			g2D.fillRect(r.x, r.y, r.width, r.height);
		}
	} //

	/**
	 * Draws a single item to the <i>offscreen</i> display
	 * buffer. Useful for incremental drawing. Call the repaintImmediate()
	 * method to have these changes directly propagate to the screen.
	 * @param item
	 */
	public void drawItem(VisualItem item) {
		Graphics2D g2D = (Graphics2D) m_offscreen.getGraphics();
		if (g2D != null) {
            prepareGraphics(g2D);
			item.getRenderer().render(g2D, item);
		}
	} //

    // ========================================================================
    // == CONTROL LISTENER METHODS ============================================
    
	/**
	 * Adds a ControlListener to receive all input events on VisualItems.
	 * @param cl the listener to add.
	 */
	public void addControlListener(ControlListener cl) {
		m_listener = ControlEventMulticaster.add(m_listener, cl);
	} //

	/**
	 * Removes a registered ControlListener.
	 * @param cl the listener to remove.
	 */
	public void removeControlListener(ControlListener cl) {
		m_listener = ControlEventMulticaster.remove(m_listener, cl);
	} //
    
	/**
	 * Returns the VisualItem located at the given point.
	 * @param p the Point at which to look
	 * @return the VisualItem located at the given point, if any
	 */
	public VisualItem findItem(Point p) {
        Point2D p2 = (m_itransform==null ? p : 
                        m_itransform.transform(p, m_tmpPoint));
		synchronized (m_registry) {
			Iterator items = m_registry.getItemsReversed();
			while (items.hasNext()) {
				VisualItem vi = (VisualItem) items.next();
				Renderer r = vi.getRenderer();
				if (r != null && r.locatePoint(p2, vi)) {
					return vi;
				}
			}
		}
		return null;
	} //
    
	/**
	 * Captures all mouse and key events on the display, detects relevant 
	 * VisualItems, and informs ControlListeners.
	 */
	public class InputEventCapturer
		implements MouseMotionListener, MouseWheelListener, MouseListener, KeyListener {

		private VisualItem activeVI = null;
		private boolean mouseDown = false;
        private boolean itemDrag = false;

		public void mouseDragged(MouseEvent e) {
            if (m_listener != null && activeVI != null) {
				m_listener.itemDragged(activeVI, e);
			} else if ( m_listener != null ) {
				m_listener.mouseDragged(e);
			}
		} //

		public void mouseMoved(MouseEvent e) {
			boolean earlyReturn = false;
			//check if we've gone over any item
			VisualItem vi = findItem(e.getPoint());
			if (m_listener != null && activeVI != null && activeVI != vi) {
				m_listener.itemExited(activeVI, e);
				earlyReturn = true;
			}
			if (m_listener != null && vi != null && vi != activeVI) {
				m_listener.itemEntered(vi, e);
				earlyReturn = true;
			}
			activeVI = vi;
			if ( earlyReturn ) return;
			
			if ( m_listener != null && vi != null && vi == activeVI ) {
				m_listener.itemMoved(vi, e);
			}
			if ( m_listener != null && vi == null ) {
				m_listener.mouseMoved(e);
			}
		} //

		public void mouseWheelMoved(MouseWheelEvent e) {
			if (m_listener != null && activeVI != null) {
				m_listener.itemWheelMoved(activeVI, e);
			} else if ( m_listener != null ) {
				m_listener.mouseWheelMoved(e);
			}
		} //

		public void mouseClicked(MouseEvent e) {
			if (m_listener != null && activeVI != null) {
				m_listener.itemClicked(activeVI, e);
			} else if ( m_listener != null ) {
				m_listener.mouseClicked(e);
			}
		} //

		public void mousePressed(MouseEvent e) {
		    mouseDown = true;
			if (m_listener != null && activeVI != null) {
				m_listener.itemPressed(activeVI, e);
			} else if ( m_listener != null ) {
				m_listener.mousePressed(e);
			}
		} //

		public void mouseReleased(MouseEvent e) {
			if (m_listener != null && activeVI != null) {
				m_listener.itemReleased(activeVI, e);
			} else if ( m_listener != null ) {
				m_listener.mouseReleased(e);
			}
            if ( m_listener != null && activeVI != null 
                    && mouseDown && isOffComponent(e) )
            {
                // mouse was dragged off of the component, 
                // then released, so register an exit
                m_listener.itemExited(activeVI, e);
                activeVI = null;
            }
            mouseDown = false;
		} //

		public void mouseEntered(MouseEvent e) {
			if ( m_listener != null ) {
				m_listener.mouseEntered(e);	
			}
		} //

		public void mouseExited(MouseEvent e) {
			if (m_listener != null && !mouseDown && activeVI != null) {
                // we've left the component and an item 
                // is active but not being dragged, deactivate it
                m_listener.itemExited(activeVI, e);
                activeVI = null;
			}
			if ( m_listener != null ) {
				m_listener.mouseExited(e);
			}
		} //

		public void keyPressed(KeyEvent e) {
			if (m_listener != null && activeVI != null) {
				m_listener.itemKeyPressed(activeVI, e);
			} else if ( m_listener != null ) {
				m_listener.keyPressed(e);
			}
		} //

		public void keyReleased(KeyEvent e) {
			if (m_listener != null && activeVI != null) {
				m_listener.itemKeyReleased(activeVI, e);
			} else if ( m_listener != null ) {
				m_listener.keyReleased(e);
			}
		} //

		public void keyTyped(KeyEvent e) {
			if (m_listener != null && activeVI != null) {
				m_listener.itemKeyTyped(activeVI, e);
			} else if ( m_listener != null ) {
				m_listener.keyTyped(e);
			}
		} //
        
        private boolean isOffComponent(MouseEvent e) {
            int x = e.getX(), y = e.getY();
            return ( x<0 || x>getWidth() || y<0 || y>getHeight() );
        } //
	} // end of inner class MouseEventCapturer
    
    
    // ========================================================================
    // == TEXT EDITING CONTROL ================================================
    
    /**
     * Returns the TextComponent used for on-screen text editing.
     * @return the TextComponent used for text editing
     */
    public JTextComponent getTextEditor() {
        return m_editor;
    } //
    
    /**
     * Sets the TextComponent used for on-screen text editing.
     * @param tc the TextComponent to use for text editing
     */
    public void setTextEditor(JTextComponent tc) {
        this.remove(m_editor);
        m_editor = tc;
        this.add(m_editor, 1);
    } //
    
    /**
     * Edit text for the given VisualItem and attribute. Presents a text
     * editing widget spaning the item's bounding box. Use stopEditing()
     * to hide the text widget. When stopEditing() is called, the attribute
     * will automatically be updated with the VisualItem.
     * @param item the VisualItem to edit
     * @param attribute the attribute to edit
     */
    public void editText(VisualItem item, String attribute) {
        if ( m_editing ) { stopEditing(); }
        Rectangle2D b = item.getBounds();
        Rectangle r = m_transform.createTransformedShape(b).getBounds();
        
        // hacky placement code that attempts to keep text in same place
        // configured under Windows XP and Java 1.4.2b
        if ( m_editor instanceof JTextArea ) {
            r.y -= 2; r.width += 22; r.height += 2;
        } else {
            r.x += 3; r.y += 1; r.width -= 5; r.height -= 2;
        }
        
        Font f = getFont();
        int size = (int)Math.round(f.getSize()*m_transform.getScaleX());
        Font nf = new Font(f.getFontName(), f.getStyle(), size);
        m_editor.setFont(nf);
        
        editText(item, attribute, r);
    } //
    
    /**
     * Edit text for the given VisualItem and attribute. Presents a text
     * editing widget spaning the given bounding box. Use stopEditing()
     * to hide the text widget. When stopEditing() is called, the attribute
     * will automatically be updated with the VisualItem.
     * @param item the VisualItem to edit
     * @param attribute the attribute to edit
     * @param r Rectangle representing the desired bounding box of the text
     *  editing widget
     */
    public void editText(VisualItem item, String attribute, Rectangle r) {
        if ( m_editing ) { stopEditing(); }
        String txt = item.getAttribute(attribute);
        m_editItem = item;
        m_editAttribute = attribute;
        Paint c = item.getColor(), fc = item.getFillColor();
        if ( c instanceof Color )
            m_editor.setForeground((Color)c);
        if ( fc instanceof Color )
            m_editor.setBackground((Color)fc);
        editText(txt, r);
    } //
    
    /**
     * Show a text editing widget containing the given text and spanning the
     * specified bounding box. Use stopEditing() to hide the text widget. Use
     * the method calls getTextEditor().getText() to get the resulting edited
     * text.
     * @param txt the text string to display in the text widget
     * @param r Rectangle representing the desired bounding box of the text
     *  editing widget
     */
    public void editText(String txt, Rectangle r) {
        if ( m_editing ) { stopEditing(); }
        m_editing = true;
        m_editor.setBounds(r.x,r.y,r.width,r.height);
        m_editor.setText(txt);
        m_editor.setVisible(true);
        m_editor.setCaretPosition(txt.length());
        m_editor.requestFocus();
    } //
    
    /**
     * Stops text editing on the display, hiding the text editing widget. If
     * the text editor was associated with a specific VisualItem (ie one of the
     * editText() methods which include a VisualItem as an argument was called),
     * the item is updated with the edited text.
     */
    public void stopEditing() {
        m_editor.setVisible(false);
        if ( m_editItem != null ) {
            String txt = m_editor.getText();
            m_editItem.setAttribute(m_editAttribute, txt);
            m_editItem = null;
            m_editAttribute = null;
            m_editor.setBackground(null);
            m_editor.setForeground(null);
        }
        m_editing = false;
    } //
    
} // end of class Display

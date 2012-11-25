package edu.berkeley.guir.prefuse;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.render.Renderer;
import edu.berkeley.guir.prefuse.util.FontLib;

/**
 * <p>Top-level class for visual representations of graph elements, providing
 * numerous visual attributes (e.g. getLocation, getColor, getFont) as well
 * as access to the original abstract data. All interactive objects displayed
 * by a prefuse application descend from this class. An active VisualItem is
 * associated with one, and only one, {@link ItemRegistry ItemRegistry}.</p>
 * 
 * <p>Visual items implement the
 * {@link edu.berkeley.guir.prefuse.graph.Entity Entity} interface, but act as
 * a proxy to the original abstract data. The {@link #getAttribute(String)
 * getAttribute} method returns the attributes from the abstract Entity this
 * VisualItem represents. The {@link #getEntity() getEntity} method will
 * return this original abstract data element.</p>
 * 
 * <p>In addition, any number of arbitrary attributes specific to a
 * particular VisualItem can be set or retrieved using the 
 * {@link #setVizAttribute(String,Object) setVizAttribute} and
 * {@link #getVizAttribute(String) getVizAttribute} methods. These can be
 * useful for applying attributes beyond the basics provided by the VisualItem
 * API (e.g. getLocation, getColor, getSize). For example, many of the provided
 * layout algorithms set their own visualization attributes as a means of
 * managing various layout parameters.</p>
 * 
 * <p>Subclasses of VisualItem include {@link NodeItem NodeItem}, 
 * {@link EdgeItem EdgeItem}, and {@link AggregateItem AggregateItem}.
 * Custom VisualItem types can also be created by subclasses. These should
 * be registered as their own item class with the {@link ItemRegistry
 * ItemRegistry}.</p>
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 * @see NodeItem
 * @see EdgeItem
 * @see AggregateItem
 */
public abstract class VisualItem implements Entity {
	
	protected ItemRegistry m_registry;  // the item registry this item is associated with
	protected String       m_itemClass; // the item class this item belongs to
	protected Entity       m_entity;    // the graph entity this item maps to
	protected int          m_dirty;     // used to keep track of dirty status

	protected boolean m_visible;
	protected boolean m_newlyVisible;
	
    // all non-standard viz attributes go here
	protected Map m_attrs;
    // degree-of-interest of ths item
	protected double  m_doi;
    // location attributes
	protected Point2D m_location;
	protected Point2D m_startLocation;
	protected Point2D m_endLocation;
    // color attributes
	protected Paint m_color;
	protected Paint m_startColor;
	protected Paint m_endColor;
	protected Paint m_fillColor;
	protected Paint m_startFillColor;
	protected Paint m_endFillColor;
    // size attributes
    protected double m_size;
	protected double m_startSize;
	protected double m_endSize;
    // font attributes
    protected Font m_startFont;
	protected Font m_font;
    protected Font m_endFont;
    // fix the position of this item?
    protected boolean m_fixed = false;
    protected boolean m_highlight = false;
    
	/**
	 * Default constructor.
	 */
	public VisualItem() {
	    m_attrs         = new HashMap(5,0.9f);
		m_location      = new Point2D.Float(Float.NaN, Float.NaN);
		m_startLocation = new Point2D.Float(Float.NaN, Float.NaN);
		m_endLocation   = new Point2D.Float(Float.NaN, Float.NaN);
	} //
	
    /**
     * Returns a String describing this VisualItem and its contents.
     * @see java.lang.Object#toString()
     */
	public String toString() {
		return "VisualItem{"+m_entity+"}";
	} //
	
	/**
	 * Initialize this VisualItem, binding it to the given
	 * ItemRegistry and Entity.
	 * @param registry the ItemRegistry monitoring this VisualItem
	 * @param entity the Entity represented by this VisualItem
	 */
	public void init(ItemRegistry registry, String itemClass, Entity entity) {
		m_itemClass = itemClass;
		m_registry = registry;
		m_entity   = entity;
		m_dirty    = 0;
		m_visible  = false;
		m_newlyVisible = false;
		m_doi = Integer.MIN_VALUE;
		
		initAttributes();
	} //
	
    protected void initAttributes() {
        // general viz attributes
        m_attrs.clear();
        // location
        m_location.setLocation(Float.NaN, Float.NaN);
        m_startLocation.setLocation(Float.NaN, Float.NaN);
        m_endLocation.setLocation(Float.NaN, Float.NaN);
        // colors
        m_color          = Color.BLACK;
        m_startColor     = Color.BLACK;
        m_endColor       = Color.BLACK;
        m_fillColor      = Color.LIGHT_GRAY;
        m_startFillColor = Color.LIGHT_GRAY;
        m_endFillColor   = Color.LIGHT_GRAY;
        // sizes
        m_size      = 1;
        m_startSize = 1;
        m_endSize   = 1;
        // fonts
        m_startFont = FontLib.getFont("SansSerif",Font.PLAIN,10);
        m_font      = m_startFont;
        m_endFont   = m_startFont;
    }
    
	/**
	 * Clears the state of this VisualItem.
	 */
	public void clear() {
		m_registry = null;
		m_entity   = null;
        initAttributes();
	} //

	/**
	 * Returns the ItemRegistry associated with this VisualItem.
	 * @return the ItemRegistry this VisualItem is contained in
	 */
	public ItemRegistry getItemRegistry() {
		return m_registry;
	} //

	/**
	 * Return the item class this item belongs to. Item class labels are used
     * by the {@link ItemRegistry ItemRegistry} to organize various VisualItem
     * types.
	 * @return String label of this item's item class
	 */
	public String getItemClass() {
		return m_itemClass;
	} //

	/**
	 * Return the Entity this VisualItem represents.
	 * @return the Entity
	 */
	public Entity getEntity() {
		return m_entity;
	} //

	/**
     * Retrieves an attribute from this item's backing Entity. Equivalent to
     * <code>getEntity().getAttributes(name)</code>.
	 * @param name the name of the attribute
	 * @return String the value of the attribute, or null if none
	 */
	public String getAttribute(String name) {
		if ( m_entity == null ) {
			throw new IllegalStateException("This item has no assigned entity.");
		} else {
			return m_entity.getAttribute(name);
		}		
	} //

	/**
	 * Sets an attribute on this item's backing Entity. Equivalent to
     * <code>getEntity().setAttribute(name,value)</code>.
	 * @param name the name of the attribute to set
	 * @param value the value of the attribute to set
	 */
	public void setAttribute(String name, String value) {
		if ( m_entity == null ) {
			throw new IllegalStateException("This item has no assigned entity.");
		} else {
			m_entity.setAttribute(name, value);
		}		
	} //
    
    /**
     * Retrieves the attribute map of this item's backing Entity. Equivalent to
     * <code>getEntity().getAttributes()</code>.
     * @see edu.berkeley.guir.prefuse.graph.Entity#getAttributes()
     */
    public Map getAttributes() {
        return m_entity.getAttributes();
    } //

    /**
     * Sets the attribute map on this item's backing Entity. Equivalent to
     * <code>getEntity().setAttributes(attrMap)</code>.
     * @see edu.berkeley.guir.prefuse.graph.Entity#setAttributes(java.util.Map)
     */
    public void setAttributes(Map attrMap) {
        m_entity.setAttributes(attrMap);
    } //

    /**
     * Clears the attributes on this item's backing Entity. Equivalent to
     * <code>getEntity().clearAttributes()</code>.
     * @see edu.berkeley.guir.prefuse.graph.Entity#clearAttributes()
     */
    public void clearAttributes() {
        m_entity.clearAttributes();
    } //
	
	/**
	 * Get a visualization attribute for this item.
	 * @param name the name of the attribute
	 * @return String
	 */
	public Object getVizAttribute(String name) {
		return m_attrs.get(name);
	} //
	
	/**
	 * Set a visualization attribute for this item.
	 * @param name the name of the attribute
	 * @param value
	 */
	public void setVizAttribute(String name, Object value) {
		m_attrs.put(name, value);
	} //
	
	/**
	 * Remove a visualization attribute for this item.
	 * @param name the name of the attribute
	 */
	public void removeVizAttribute(String name) {
		m_attrs.remove(name);
	} //
	
	/**
	 * Updates an interpolated attribute, setting the current value of the attribute as the
	 * start value, and setting the specified value as the ending value. The current value is
	 * left unchanged.
	 * @param name - the name of the attribute (e.g. color)
	 * @param startName - the name of the start value for the attribute (e.g. startColor)
	 * @param endName - the name of the end value for the attribute (e.g. endColor)
	 * @param value - the new ending value of the attribute.
	 */
	public void updateVizAttribute(String name, String startName, String endName, Object value) {
		Object curVal = getVizAttribute(name);
		setVizAttribute(startName, curVal);
		setVizAttribute(endName, value);
	} //

	/**
	 * "Touching" an item tells the system that the item is to be used
	 * in the visualization, and so resets any garbage collecting data
	 * to its freshest state. If an item has been retrieved using
	 * the <code>ItemRegistry.getNodeItem(node, true)</code> method it is
	 * touched automatically.
	 */
	public void touch() {
		m_dirty = 0;
	} //

	/**
	 * Gets the dirty counter for this item. This counter is used
	 * by the ItemRegistry to control garbage collection. An item will be
     * garbage collected (removed from the registry) when a filter has
     * been run the number of times indicated by the dirty counter without
     * re-filtering this item.
	 * @return the dirty counter
	 */
	public int getDirty() {
		return m_dirty;
	} //
	
	/**
	 * Sets the dirty counter for this item. This counter is used
	 * by the ItemRegistry to control garbage collection. An item will be
     * garbage collected (removed from the registry) when a filter has
     * been run the number of times indicated by the dirty counter without
     * re-filtering this item.
	 * @param dirty the new value of the dirty counter
	 */
	public void setDirty(int dirty) {
		m_dirty = dirty;
	} //

	/**
	 * Returns true if this item became visible within the last
	 * processing cycle. Processing cycles are determined whenever
     * garbage collection is run on the ItemRegistry. This usually
     * occurs whenever a {@link edu.berkeley.guir.prefuse.action.Filter
     * Filter} is run.
	 * @return true if newly visible, false otherwise
	 */
	public boolean isNewlyVisible() {
		return m_newlyVisible;
	} //

	/**
	 * Indicates if this VisualItem is currently visible in the visualization.
	 * @return true if visible, false otherwise
	 */
	public boolean isVisible() {
		return m_visible;
	} //

	/**
	 * Sets the visible status of this VisualItem. If set false, this item will
	 * not be visited by the rendering loop.
	 * @param s the new visibility status of this item.
	 */
	public void setVisible(boolean s) {
		m_newlyVisible = ( !m_visible && s );
		m_visible = s;
	} //

    /**
     * Indicates if the abstract data entity associated with this VisualItem
     * is currently a focus. Focus determination is achieved by querying the
     * {@link FocusManager FocusManager}
     * associated with this item's {@link ItemRegistry ItemRegistry}.
     * @return true if this item's backing Entity is a focus, false otherwise.
     */
    public boolean isFocus() {
        FocusManager fmanager = m_registry.getFocusManager();
        return fmanager.isFocus(m_entity);
    } //
    
	/**
	 * Get the renderer for drawing this VisualItem.
	 * @return this item's Renderer
	 */
	public Renderer getRenderer() {
		try {
			return m_registry.getRendererFactory().getRenderer(this);
		} catch ( Exception e ) {
			System.out.println("processing reclaimed item!!! -- " + this.getClass().getName());
			//e.printStackTrace();
		}
		return null;
	} //
	
	/**
	 * Returns true if the given point is contained within this VisualItem.
	 * @param point the point to test for containment
	 * @return true if the point is within this VisualItem
	 */
	public boolean locatePoint(Point2D point) {
		return getRenderer().locatePoint(point, this);
	} //
	
	/**
	 * Returns the bounding box of this VisualItem, determined by it's renderer.
	 * @return a Rectangle representing the bounding box for this VisualItem
	 */
	public Rectangle2D getBounds() {
		return getRenderer().getBoundsRef(this);
	} //


	// == convenience methods for viz attributes ==============================

    public boolean isFixed() {
        return m_fixed;
    } //
    
    public void setFixed(boolean s) {
        m_fixed = s;
    } //
    
    public boolean isHighlighted() {
        return m_highlight;
    } //
    
    public void setHighlighted(boolean s) {
        m_highlight = s;
    } //
    
	public double getDOI() { return m_doi; } //
	
	public void setDOI(double doi) { m_doi = doi; } //

	public Point2D getStartLocation() {
		return m_startLocation;
	} //

	public Point2D getEndLocation() {
		return m_endLocation;
	} //
	
	public Point2D getLocation() {
		return m_location;
	} //

	public void setLocation(Point2D loc) {
		m_location.setLocation(loc);
	} //

	public void setLocation(double x, double y) {
		m_location.setLocation(x,y);
	} //

	public void setStartLocation(Point2D loc) {
		m_startLocation.setLocation(loc);
	} //

	public void setStartLocation(double x, double y) {
		m_startLocation.setLocation(x,y);
	} //

	public void setEndLocation(Point2D loc) {
		m_endLocation.setLocation(loc);
	} //

	public void setEndLocation(double x, double y) {
		m_endLocation.setLocation(x,y);
	} //

	public void updateLocation(Point2D loc) {
		m_startLocation.setLocation(m_location);
		m_endLocation.setLocation(loc);
	} //

	public void updateLocation(double x, double y) {
		m_startLocation.setLocation(m_location);
		m_endLocation.setLocation(x,y);
	} //
	
	public double getX() {
		return m_location.getX();
	} //
	
	public double getY() {
		return m_location.getY();
	} //
	
	public Paint getStartColor() {
		return m_startColor;
	} //
	
	public Paint getEndColor() {
		return m_endColor;
	} //
	
	public Paint getColor() {
		return m_color;
	} //	
	
	public void setColor(Paint color) {
		m_color = color;
	} //
	
	public void updateColor(Paint color) {
		m_startColor = m_color;
		m_endColor = color;
	} //
	
	public Paint getStartFillColor() {
		return m_startFillColor;
	} //
	
	public Paint getEndFillColor() {
		return m_endFillColor;
	} //
	
	public Paint getFillColor() {
		return m_fillColor;
	} //	
	
	public void setFillColor(Paint color) {
		m_fillColor = color;
	} //
	
	public void updateFillColor(Paint color) {
		m_startFillColor = m_fillColor;
		m_endFillColor = color;
	} //
	
	public double getStartSize() {
		return m_startSize;
	} //
	
	public double getEndSize() {
		return m_endSize;
	} //
	
	public double getSize() {
		return m_size;
	} //
	
	public void setSize(double size) {
		m_size = size;
	} //
	
	public void setStartSize(double size) {
		m_startSize = size;
	} //

	public void setEndSize(double size) {
		m_endSize = size;
	} //
			
	public void updateSize(double size) {
		m_startSize = m_size;
		m_endSize = size;
	} //
	
	public Font getStartFont() {
		return m_startFont;
	} //
	
	public void setStartFont(Font f) {
		m_startFont = f;
	} //
    
    public Font getFont() {
        return m_font;
    } //
    
    public void setFont(Font f) {
        m_font = f;
    } //
    
    public Font getEndFont() {
        return m_endFont;
    } //
    
    public void setEndFont(Font f) {
        m_endFont = f;
    } //
    
    public void updateFont(Font f) {
        m_startFont = m_font;
        m_endFont = f;
    } //

} // end of abstract class VisualItem

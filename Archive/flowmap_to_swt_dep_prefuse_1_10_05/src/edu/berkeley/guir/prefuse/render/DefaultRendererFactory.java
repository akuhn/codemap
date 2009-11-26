package edu.berkeley.guir.prefuse.render;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.EdgeItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.NodeItem;

/**
 * Default factory from which to retrieve VisualItem renderers. Assumes only one
 * type of renderer each for NodeItems, EdgeItems, and AggregateItems.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DefaultRendererFactory implements RendererFactory {

	private Renderer m_nodeRenderer;
	private Renderer m_edgeRenderer;
	private Renderer m_aggrRenderer;

	/**
	 * Default constructor. Assumes default renderers for each VisualItem type.
	 */
	public DefaultRendererFactory() {
		this(new DefaultNodeRenderer(),
		     new DefaultEdgeRenderer(),
		     null);
	} //
	
	/**
	 * Constructor.
	 * @param nodeRenderer the Renderer to use for NodeItems
	 * @param edgeRenderer the Renderer to use for EdgeItems
	 * @param aggrRenderer the Renderer to use for AggregateItems
	 */
	public DefaultRendererFactory(Renderer nodeRenderer, 
								  Renderer edgeRenderer, 
								  Renderer aggrRenderer)
	{
		m_nodeRenderer = nodeRenderer;
		m_edgeRenderer = edgeRenderer;
		m_aggrRenderer = aggrRenderer;
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.render.RendererFactory#getRenderer(edu.berkeley.guir.prefuse.VisualItem)
	 */
	public Renderer getRenderer(VisualItem item) {
        if ( item instanceof AggregateItem ) {
            return m_aggrRenderer;
        } else if ( item instanceof NodeItem ) {
			return m_nodeRenderer;
		} else if ( item instanceof EdgeItem ) {			
			return m_edgeRenderer;
		} else {
			return null;
		}
	} //
	
	/**
     * Returns the Renderer for AggregateItems
	 * @return the Renderer for AggregateItems
	 */
	public Renderer getAggregateRenderer() {
		return m_aggrRenderer;
	} //

	/**
     * Returns the Renderer for EdgeItems
	 * @return the Renderer for EdgeItems
	 */
	public Renderer getEdgeRenderer() {
		return m_edgeRenderer;
	} //

	/**
     * Returns the Renderer for NodeItems
	 * @return the Renderer for NodeItems
	 */
	public Renderer getNodeRenderer() {
		return m_nodeRenderer;
	} //

	/**
     * Sets the Renderer for AggregateItems
	 * @param renderer the new Renderer for AggregateItems
	 */
	public void setAggregateRenderer(Renderer renderer) {
		m_aggrRenderer = renderer;
	} //

	/**
     * Sets the Renderer for EdgeItems
	 * @param renderer the new Renderer for EdgeItems
	 */
	public void setEdgeRenderer(Renderer renderer) {
		m_edgeRenderer = renderer;
	} //

	/**
     * Sets the Renderer for NodeItems
	 * @param renderer the new Renderer for NodeItems
	 */
	public void setNodeRenderer(Renderer renderer) {
		m_nodeRenderer = renderer;
	} //

} // end of class DefaultRendererFactory

package edu.berkeley.guir.prefuse.render;

import edu.berkeley.guir.prefuse.VisualItem;

/**
 * The RendererFactory is responsible for providing the proper Renderer
 * instance for drawing a given VisualItem.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface RendererFactory {

	/**
	 * Return the appropriate renderer to draw the given VisualItem.
	 * @param item the item for which to retrieve the renderer
	 * @return the Renderer for the given VisualItem
	 */
	public Renderer getRenderer(VisualItem item);

} // end of interface RendererFactory

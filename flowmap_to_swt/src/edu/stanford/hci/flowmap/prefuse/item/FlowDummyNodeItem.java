
package edu.stanford.hci.flowmap.prefuse.item;


/**
 * Represents a NodeItem without an underlying node. That is, it's one of the filler
 * nodes between the source and the destinations in the flow map. It also
 * cannot be a root node, unlike a real node item.
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class FlowDummyNodeItem extends FlowNodeItem{

	public boolean isRootNode(){
		return false;
	}
}

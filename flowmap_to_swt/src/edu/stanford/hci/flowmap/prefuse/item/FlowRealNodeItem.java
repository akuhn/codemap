package edu.stanford.hci.flowmap.prefuse.item;


/**
 * Represents a FlowNodeItem that has a REAL underlying flowNode
 *
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class FlowRealNodeItem extends FlowNodeItem implements Comparable {

	public boolean isRootNode(){
		return getSourceNode().isRootNode();
	}
	public boolean equals(Object o) {
		FlowNodeItem n = (FlowNodeItem)o;
		return getName().equals(n.getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		FlowNodeItem n = (FlowNodeItem)o;
		return getName().compareTo(n.getName());
	}
		
}

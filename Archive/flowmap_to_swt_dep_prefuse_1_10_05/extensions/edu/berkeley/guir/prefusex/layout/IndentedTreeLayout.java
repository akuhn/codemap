package edu.berkeley.guir.prefusex.layout;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import edu.berkeley.guir.prefuse.AggregateItem;
import edu.berkeley.guir.prefuse.VisualItem;
import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.NodeItem;
import edu.berkeley.guir.prefuse.action.assignment.TreeLayout;
import edu.berkeley.guir.prefuse.collections.DOIItemComparator;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Performs a simple indented hierarchical layout of a tree. This is the
 * layout most people are used to seeing in their file managers.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> - prefuse(AT)jheer.org
 */
public class IndentedTreeLayout extends TreeLayout {
	public static final String ATTR_EXPANDED = "expanded";

	private class LayoutEntry {
		public LayoutEntry(NodeItem i, int d) {
			nodeItem = i;
			aggrItem = null;
			elided   = false;
			hidden   = false;
			index    = -1;
			depth    = d;
		} //
		NodeItem nodeItem, aggrItem;
		boolean elided, hidden;
		int index, depth;
	} //

    private ItemRegistry m_registry;
    
	private List m_entryList = new ArrayList();
	private int m_verticalInc = 15;
	private int m_indent = 16;

	private boolean    m_elide = false;            // controls elision
	private List       m_tlist = new LinkedList(); // temporary list
	private Comparator m_comp  = new Comparator() {
		Comparator comp = new DOIItemComparator();
		public int compare(Object o1, Object o2) {
			VisualItem item1 = ((LayoutEntry)o1).nodeItem;
			VisualItem item2 = ((LayoutEntry)o2).nodeItem;
			return comp.compare(item1, item2);
		} //
	};
	
	private AggregateItem m_tmpAggr = null;
	
	public int getIndent() {
		return m_indent;
	} //
	
	public void setIndent(int indent) {
		m_indent = indent;
	} //
	
	public boolean isEliding() {
		return m_elide;
	} //
	
	public void setEliding(boolean s) {
		m_elide = s;
	} //
    
    public Point2D getLayoutAnchor(ItemRegistry registry) {
        Point2D anchor = super.getLayoutAnchor();
        if ( anchor != null )
            return anchor;
        
        Rectangle2D b = getLayoutBounds(registry);
        double x = 0, y = 0;
        if ( b != null ) {
            x = b.getX();
            y = b.getY();
        }
        return new Point2D.Double(x,y);
    } //
    
	/**
	 * @see edu.berkeley.guir.prefuse.action.Action#run(edu.berkeley.guir.prefuse.ItemRegistry, double)
	 */
	public void run(ItemRegistry registry, double frac) {
        m_registry = registry;
		m_tmpAggr = null;
		NodeItem n = getLayoutRoot(registry);
		if ( n != null && n.isVisible() ) {
            Rectangle2D b = getLayoutBounds(registry);
            Point2D anchor = getLayoutAnchor(registry);
			int availHeight = (int)Math.ceil(b.getHeight() - anchor.getY());
			int treeHeight  = calcTreeHeight(m_entryList, n, 0, 0);
			updateStartLocations(m_entryList);
			if ( m_elide && treeHeight > availHeight )
				elide(treeHeight, availHeight);
			layout(m_entryList,
                    (int)Math.ceil(anchor.getY()+n.getBounds().getHeight()/2),
                    (int)Math.ceil(anchor.getX()));
		} else {
			System.err.println("IndentedTreeLayout: Tree root not visible!");
		}
		m_entryList.clear();
	} //
	
	/**
	 * Calculates the full height of the tree while constructing an
	 * in-order list of all visible entries. 
	 */
	protected int calcTreeHeight(List entryList, NodeItem n, int height, int depth) {
		if ( n != null && n.isVisible() ) {
			// add entry to entry list
			LayoutEntry entry = new LayoutEntry(n, depth);
			entry.index = entryList.size();
			entryList.add(entry);
			
			// increment height and recurse as necessary
			height += n.getBounds().getHeight();
			if ( isExpanded(n) ) {
				Iterator childIter = n.getChildren();
				while ( childIter.hasNext() ) {
					NodeItem c = (NodeItem)childIter.next();
					height = calcTreeHeight(entryList, c, height, depth+1);
				}
			}
		}
		return height;
	} //
	
	/**
	 * Elides nodes of lower interest until the structure fits within
	 * its display bounds.
	 * @param treeHeight the current height of the tree structure
	 * @param availHeight the available height for displaying the structure
	 */
	protected void elide(int treeHeight, int availHeight) {
		//// allocate auxiliary data structures
//		Map       pmap     = new HashMap();
		List list = new ArrayList(m_entryList);
		boolean elided[]   = new boolean[list.size()];
		
		//// sort all NodeItems in increasing order by doi
		Collections.sort(list, m_comp);
		
		//// iterate through the sorted NodeItems
		Iterator nodeIter = list.iterator();
		while ( nodeIter.hasNext() && treeHeight > availHeight ) {
			// get the next node, set node item as elided, calculate space savings
			LayoutEntry entry = (LayoutEntry)nodeIter.next();
			VisualItem nitem = entry.nodeItem;
			int run, idx = entry.index;
			elided[idx] = true;
			if ( (run=elisionRun(elided, idx)) > 0 ) {
				for ( int j = 0; j < run; j++ ) {
					VisualItem item = ((LayoutEntry)m_entryList.get(idx+j)).nodeItem;
					treeHeight -= item.getBounds().getHeight();
					
					// if all children are elided, don't bother with aggregate
//					DefaultTreeNode p = (DefaultTreeNode)item.getEntity();
//					if ( p != null ) {
//						Integer ecount = (Integer)pmap.get(p);
//						ecount = new Integer((ecount == null ? 1 : ecount.intValue()+1));
//						if ( ecount.intValue() == p.getNumChildren() ) {
//							treeHeight -= item.getBounds().height;
//						}
//					}
					
					//System.out.println("elided: " + item.getAttribute("FullName"));
				}
			}
		}
		
		//// update nodes and aggregates to reflect elided status
		AggregateItem aitem = null;
		for ( int i = 0, size = 0; i < elided.length; i++ ) {
			if ( (aitem != null && elided[i]) || 
				 (i < elided.length-1 && elided[i] && elided[i+1]) ) {
				LayoutEntry entry = ((LayoutEntry)m_entryList.get(i));
				VisualItem item = entry.nodeItem;
				TreeNode n = (TreeNode)item.getEntity();
				if ( aitem == null ) {
					// get the new aggregate item when needed
					aitem = m_registry.getAggregateItem(n, false);
					if ( aitem != null )
						m_registry.removeMappings(aitem);
					aitem = m_registry.getAggregateItem(n, true);
					copyAttributes(item, aitem);
				} else {
					// otherwise add a mapping
					m_registry.addMapping(n, aitem);
				}
				aitem.setAggregateSize(++size);
				item.setVisible(false);
				entry.elided = true;
				entry.aggrItem = aitem;
			} else if ( aitem != null && !elided[i] ) {
				aitem = null; size = 0;
			}
		}
	} //

	private int elisionRun(boolean[] elided, int idx) {
		int len = elided.length;
		if ( idx == 0 ) {
			return ( len > 1 && elided[1] ? 1 : 0 );
		} else if ( idx == len-1 ) {
			return ( idx > 0 && elided[idx-1] ? 1 : 0 );
		} else {
			if ( len >= 2 && elided[idx-1] && elided[idx+1] ) {
				return 2;
			} else if ( (idx > 0 && elided[idx-1]) || (idx < len-1 && elided[idx+1]) ) {
				return 1;
			} else {
				return 0;
			}
		}
	} //
	
	/**
	 * Copy attributes from one item to another. Used for initializing
	 * aggregate items.
	 */
	private void copyAttributes(VisualItem item1, VisualItem item2) {
		item2.setLocation(item1.getLocation());
		item2.setEndLocation(item1.getEndLocation());
		item2.setSize(item1.getSize());
		item2.setEndSize(item1.getEndSize());		
	} //

	/**
	 * Updates the starting locations of newly visible nodes to ensure
	 * that they animate from their intuitive sources. Must
	 * be run before elision is performed, so that old aggregate
	 * positions are retrieved correctly.
	 * @param entryList
	 */
	protected void updateStartLocations(List entryList) {
		for ( int i = 0; i < entryList.size(); i++ ) {
			LayoutEntry entry = (LayoutEntry)entryList.get(i);
			VisualItem item;
			item = entry.nodeItem;
					
			// added set start position for newly visible nodes -- jheer
			if ( item.isNewlyVisible() ) {
				TreeNode node = (TreeNode)item.getEntity();
				AggregateItem aitem = m_registry.getAggregateItem(node);
				if ( aitem != null && aitem.isVisible() ) {
					item.setLocation(aitem.getEndLocation());
				} else {
					TreeNode p = node.getParent();
					if ( p != null ) {
						VisualItem pitem = m_registry.getNodeItem(p);
						item.setLocation(pitem.getEndLocation());
					}
				}
			}
		}		
	} //

	/**
	 * Compute the layout.
	 */
	protected int layout(List entryList, int xAnchor, int height) {
		VisualItem tmpAggr = null;
		for ( int i = 0; i < entryList.size(); i++ ) {
			LayoutEntry entry = (LayoutEntry)entryList.get(i);
			NodeItem item;
			if ( entry.hidden ) {
				continue;
			} else if ( entry.elided ) {
				item = entry.aggrItem;
				if ( item == tmpAggr ) {
					continue;
				} else {
					tmpAggr = item;
				}
			} else {
				item = entry.nodeItem;
			}			
			setLocation(item,(NodeItem)item.getParent(), 
                    entry.depth*m_indent+xAnchor, height);
			height += item.getBounds().getHeight();
		}
		return height;
	} //
		
	/**
	 * Set the (x,y) co-ordinates of the given node. Updates aggregated
	 * items as well as visible items.
	 * @param item the item to set the position for
	 * @param x the x-coordinate of the node
	 * @param y the y-coordinate of the node
	 */
	protected void setLocation(VisualItem item, VisualItem referer, 
            double x, double y)
    {
        super.setLocation(item,referer,x,y);
		List entities = null;
		if ( item instanceof AggregateItem ) {
			entities = ((AggregateItem)item).getEntities();
		}
		if ( entities != null ) {
			Iterator iter = entities.iterator();
			while ( iter.hasNext() ) {
				NodeItem nitem = m_registry.getNodeItem((TreeNode)iter.next());
				super.setLocation(nitem,item,x,y);
			}
		}
	} //

	/**
	 * Indicates whether or not a node has been manually expanded.
	 */
	private boolean isExpanded(VisualItem item) {
		Boolean b = ((Boolean)item.getVizAttribute(ATTR_EXPANDED));
		return ( b == null ? false : b.booleanValue() );
	} //

} // end of class IndentedTreeLayout

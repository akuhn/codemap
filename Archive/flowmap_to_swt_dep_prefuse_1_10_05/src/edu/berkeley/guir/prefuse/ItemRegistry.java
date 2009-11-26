package edu.berkeley.guir.prefuse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.berkeley.guir.prefuse.collections.CompositeItemIterator;
import edu.berkeley.guir.prefuse.collections.DefaultItemComparator;
import edu.berkeley.guir.prefuse.collections.VisibleItemIterator;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.event.ItemRegistryListener;
import edu.berkeley.guir.prefuse.event.RegistryEventMulticaster;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;
import edu.berkeley.guir.prefuse.render.DefaultRendererFactory;
import edu.berkeley.guir.prefuse.render.RendererFactory;

/**
 * <p>
 * The ItemRegistry is the central data structure for a prefuse visualization.
 * The registry maintains mappings between abstract graph data (e.g., 
 * {@link edu.berkeley.guir.prefuse.graph.Node Nodes} and 
 * {@link edu.berkeley.guir.prefuse.graph.Edge Edges}) and their visual 
 * representations (e.g., {@link NodeItem NodeItems} and 
 * {@link EdgeItem EdgeItems}). The ItemRegistry maintains rendering queues 
 * of all visualized {@link VisualItem VisualItems}, a comparator for ordering
 * these queues (and thus controlling rendering order), references to all
 * displays that render the contents of this registry, and a focus manager
 * keeping track of focus sets of {@link edu.berkeley.guir.prefuse.graph.Entity
 * Entity} instances. In addition, the ItemRegistry supports garbage 
 * collection of <code>VisualItems</code> across interaction
 * cycles of a visualization, allowing visual representations of graph
 * elements to pass in and out of existence as necessary.
 * </p>
 *
 * <p>
 * <code>VisualItems</code> are not instantiated directly, instead they are 
 * created by the <code>ItemRegistry</code> as visual representations for 
 * abstract graph data. To create a new <code>VisualItem</code> or retrieve 
 * an existing one, use the provided <code>ItemRegistry</code> methods 
 * (e.g., <code>getItem()</code>, <code>getNodeItem</code>, etc). These are the
 * methods used by the various filters in the 
 * {@link edu.berkeley.guir.prefuse.action edu.berkeley.guir.prefuse.action}
 * package to determine which graph elements are visualized and which are not.
 * </p>
 * 
 * <p>
 * For convenience, the <code>ItemRegistry</code> creates entries for three types 
 * of <code>VisualItems</code>: {@link NodeItem NodeItems}, {@link EdgeItem 
 * EdgeItems}, and {@link AggregateItem AggregateItems}. The mappings and
 * rendering queues for these entries can be accessed through convenience
 * methods such as <code>getNodeItem()</code>, <code>getEdgeItems()</code>, 
 * etc. More generally, separate entries with their own mappings and 
 * rendering queue can be made for any type of <code>VisualItem</code> by 
 * using the {@link #addItemClass(String,Class) addItemClass()} methods. For 
 * example, if there are more than two different types of aggregates used 
 * (e.g., subtree aggregates and aggregates of other nodes) it may facilitate
 * design to separate these into their own item classes.
 * </p>
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ItemRegistry {

	public static final String DEFAULT_NODE_CLASS = "node";
	public static final String DEFAULT_EDGE_CLASS = "edge";
	public static final String DEFAULT_AGGR_CLASS = "aggregate";
	public static final int    DEFAULT_MAX_ITEMS  = 10000;
	public static final int    DEFAULT_MAX_DIRTY  = 1;
	
	/**
	 * Wrapper class that holds all the data structures for managing
	 * a class of VisualItems.
	 */
	public class ItemEntry {
		ItemEntry(String itemClass, Class classType, int dirty) {
			try {
				name     = itemClass;
				type     = classType;
				itemList = new LinkedList();
				itemMap  = new HashMap();
				modified = false;
				maxDirty = dirty;
			} catch ( Exception e) {
				e.printStackTrace();
			}
		} //
		public List getItemList() {	return itemList; } //
		
		public boolean modified;
		public int     maxDirty;
		public Class   type;
		public String  name;
		public List    itemList;
		public Map     itemMap;
	} // end of inner class ItemEntry
	
    private Graph           m_backingGraph;
    private Graph           m_filteredGraph;
    
    private List            m_displays;
    private FocusManager    m_fmanager;
	private ItemFactory     m_ifactory;
	private RendererFactory m_rfactory;
	
	private List m_entryList; // list of ItemEntry instances
	private Map  m_entryMap;  // maps from item class names to ItemEntry instances
	private Map  m_entityMap; // maps from items back to their entities
	private int  m_size;      // the number of items in this registry
    
	private Comparator m_comparator;
  
  	private ItemRegistryListener m_registryListener;
  	private FocusListener        m_focusListener;
	
	/**
	 * Constructor. Creates an empty ItemRegistry and corresponding ItemFactory.
	 * By default, creates queues and <code>ItemFactory</code> entries for
	 * handling NodeItems, EdgeItems, and AggregateItems, respectively. All
	 * are given default settings, including a maxDirty value of 1.
     * @param g the Graph instance this ItemRegistry will be used to visualize.
	 */
	public ItemRegistry(Graph g) {
		this(g, true);
	} //
	
    /**
     * Constructor. Creates an empty ItemRegistry and optionally performs the
     * default initialization, createing queues and <code>ItemFactory</code>
     * entries for handling NodeItems, EdgeItems, and AggregateItems, 
     * respectively. All are given default settings, including a maxDirty 
     * value of 1. If default initialization is disabled, callers will have
     * to manually add their own {@link #addItemClass(String,Class) item 
     * classes} to the registry.
     * @param g the Graph instance this ItemRegistry will be used to visualize.
     * @param initDefault indicates whether or not default initialization is
     *  performed.
     */
	public ItemRegistry(Graph g, boolean initDefault) {
        m_backingGraph = g;
        m_displays = new ArrayList();
        m_fmanager = new FocusManager();
		try {
			m_ifactory  = new ItemFactory();
			m_rfactory  = new DefaultRendererFactory();
			m_entryList = new LinkedList();
			m_entryMap  = new HashMap();
			m_entityMap = new HashMap();
			m_comparator = new DefaultItemComparator();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		if ( initDefault ) {
			defaultInitialization();		
		}
	} //
	
	private synchronized void defaultInitialization() {
		addItemClass(DEFAULT_NODE_CLASS, NodeItem.class);
		addItemClass(DEFAULT_EDGE_CLASS, EdgeItem.class);
		addItemClass(DEFAULT_AGGR_CLASS, AggregateItem.class);
	} //
	
	public synchronized void addItemClass(String itemClass, Class itemType) {
		addItemClass(itemClass, itemType, DEFAULT_MAX_DIRTY, DEFAULT_MAX_ITEMS);
	} //
	
	public synchronized void addItemClass(String itemClass, Class itemType, int maxDirty) {
		addItemClass(itemClass, itemType, maxDirty, DEFAULT_MAX_ITEMS);
	} //
	
	public synchronized void addItemClass(String itemClass, Class itemType, 
								int maxDirty, int maxItems) {
		ItemEntry entry = new ItemEntry(itemClass, itemType, maxDirty);
		m_entryList.add(entry);
		m_entryMap.put(itemClass, entry);
		m_ifactory.addItemClass(itemClass, itemType, maxItems);
	} //
	
    /**
     * Returns the Graph visualized by this ItemRegistry.
     * @return this ItemRegistry's backing Graph.
     */
    public synchronized Graph getGraph() {
        return m_backingGraph;
    } //
    
    /**
     * Sets the Graph visualized by this ItemRegistry.
     * @param g the backing Graph for this ItemRegistry
     */
    public synchronized void setGraph(Graph g) {
        m_backingGraph = g;
    } //
    
    /**
     * Returns the filtered Graph of VisualItems.
     * @return the filtered graph
     */
    public synchronized Graph getFilteredGraph() {
        return m_filteredGraph;
    } //
    
    /**
     * Sets the filtered Graph of VisualItems.
     * @param g the filtered Graph to set
     */
    public synchronized void setFilteredGraph(Graph g) {
        m_filteredGraph = g;
    } //
    
    /**
     * Associates a Display with this ItemRegistry. Applications shouldn't
     * need to call this method. Use the 
     * {@link Display#setItemRegistry(ItemRegistry) setItemRegistry} method in
     * the Display class instead.
     * @param d the Display to associate with this
     *  ItemRegistry.
     */
    public synchronized void addDisplay(Display d) {
        if ( !m_displays.contains(d) )
            m_displays.add(d);
    } //
    
    /**
     * Removes a Display from this ItemRegistry
     * @param d the Display to remove
     * @return true if the Display was found and removed, false otherwise
     */
    public synchronized boolean removeDisplay(Display d) {
        boolean rv = m_displays.remove(d);
        if ( rv ) d.setItemRegistry(null);
        return rv;
    } //
    
    /**
     * Returns the Display at the given index. Displays are given sequentially
     * larger indices as they are added to an ItemRegistry.
     * @param i the index of the requested Display
     * @return the requested Display
     */
    public synchronized Display getDisplay(int i) {
        return (Display)m_displays.get(i);
    } //
    
    /**
     * Returns the number of Displays associated with this ItemRegistry.
     * @return the number of Displays
     */
    public synchronized int getDisplayCount() {
        return m_displays.size();
    } //
    
    /**
     * Issues repaint requests to all Displays associated with this
     * ItemRegistry.
     */
    public synchronized void repaint() {
        Iterator iter = m_displays.iterator();
        while ( iter.hasNext() )
            ((Display)iter.next()).repaint();
    } //
    
    /**
     * Returns a list of the Displays registered with this ItemRegistry.
     * @return a list of the Displays registered with this ItemRegistry
     */
    public synchronized List getDisplays() {
        ArrayList copy = new ArrayList(m_displays.size());
        copy.addAll(m_displays);
        return copy;
    } //
    
    /**
     * Returns the FocusManager associated with this ItemRegistry. The
     * FocusManager is used to keep track of various focus sets of
     * Entity instances, such as selected nodes and search results.
     * @return this ItemRegistry's FocusManager
     */
    public synchronized FocusManager getFocusManager() {
        return m_fmanager;
    } //
    
    /**
     * Returns the default FocusSet overseen by this ItemRegistry's
     * FocusManager. This FocusSet is typically used to keep track
     * of clicked/selected elements of a visualization.
     * @return the default FocusSet for this ItemRegistry.
     */
    public synchronized FocusSet getDefaultFocusSet() {
        return m_fmanager.getDefaultFocusSet();
    } //
    
	/**
	 * Return the renderer factory for this registry's items. The
	 * renderer factory determines which renderer components should be
	 * used to draw VisualItems in the visualization.
	 * @return the current renderer factory
	 */
	public synchronized RendererFactory getRendererFactory() {
		return m_rfactory;
	} //
	
	/**
	 * Set the renderer factory for this registry's items. The
	 * renderer factory determines which renderer components should be
	 * used to draw VisualItems in the visualization. By using this method,
	 * one can set custom renderer factories to control the rendering 
	 * behavior of all visualized items.
	 * @param factory the renderer factory to use
	 */
	public synchronized void setRendererFactory(RendererFactory factory) {
		m_rfactory = factory;
	} //

	/**
	 * Return the item comparator used to determine rendering order.
	 * @return the item comparator
	 */
	public synchronized Comparator getItemComparator() {
		return m_comparator;
	} //
	
	/**
	 * Sets the item comparator used to determine rendering order. This
	 * method can be used to install custom comparators for VisualItems,
	 * allowing fine grained control over the order items are processed
	 * in the rendering loop.
	 * 
	 * Items drawn later will appear on top of earlier-drawn items, and the
	 * registry sorts items in <i>increasing</i> order, so the the greater
	 * the item is according to the comparator, the later it will be drawn
	 * in the rendering cycle.
	 * 
	 * @return the item comparator
	 */
	public synchronized void setItemComparator(Comparator comparator) {
		m_comparator = comparator;
	} //

    /**
     * Returns the total number of VisualItems in the given item class.
     * @param itemClass the item class to look up the size for
     * @return the total number of VisualItems in the given item class
     */
    public synchronized int size(String itemClass) {
        ItemEntry ie = (ItemEntry)m_entryMap.get(itemClass);
        return (ie==null ? -1 : ie.itemList.size());
    } //
    
    /**
     * Returns the total number of VisualItems in the ItemRegistry.
     * @return the total number of VisualItems
     */
    public synchronized int size() {
        return m_size;
    } //
    
	// ========================================================================
	// == REGISTRY METHODS ====================================================
    
    /**
     * Runs the garbage collector on items of the specified itemClass. This 
     * method is typically invoked by an appropriate {@link 
     * edu.berkeley.guir.prefuse.action.Filter filter} action.
     * @param itemClass the item class to garbage collect
     */
	public synchronized void garbageCollect(String itemClass) {
		ItemEntry entry = (ItemEntry)m_entryMap.get(itemClass);
		if ( entry != null ) {
			garbageCollect(entry);
		} else {
			throw new IllegalArgumentException("The input string must be a" 
				+ " recognized item class!");
		}
	} //
	
    /**
     * Runs the garbage collector on the given ItemEntry, which contains
     * the current state of a specific item class.
     * @param entry the ItemEntry from which to garbage collect
     */
	public synchronized void garbageCollect(ItemEntry entry) {
		entry.modified = true;
        Iterator iter = entry.itemList.iterator();
        while ( iter.hasNext() ) {
            VisualItem item = (VisualItem)iter.next();
            int dirty = item.getDirty()+1;
            item.setDirty(dirty);
            if ( entry.maxDirty > -1 && dirty > entry.maxDirty ) {
                iter.remove();
                removeItem(entry, item, false);
            } else if ( dirty > 1 ) {
                item.setVisible(false);
            }
        }	
	} //
		
	/**
	 * Perform garbage collection of NodeItems. Use carefully.
	 */
	public synchronized void garbageCollectNodes() {
		garbageCollect(DEFAULT_NODE_CLASS);
	} //
	
	/**
	 * Perform garbage collection of EdgeItems. Use carefully.
	 */
	public synchronized void garbageCollectEdges() {
		garbageCollect(DEFAULT_EDGE_CLASS);
	} //
	
	/**
	 * Perform garbage collection of AggregateItems. Use carefully.
	 */
	public synchronized void garbageCollectAggregates() {
		garbageCollect(DEFAULT_AGGR_CLASS);
	} //

    /**
     * Clears the ItemRegistry, removing all visualized VisualItems.
     */
	public synchronized void clear() {
		Iterator iter = m_entryList.iterator();
		while ( iter.hasNext() ) {
			clear((ItemEntry)iter.next());
		}
	} //

    /**
     * Clears the given ItemEntry, removing all visualized VisualItems.
     * @param entry the ItemEntry to clear
     */
	private synchronized void clear(ItemEntry entry) {
		entry.modified = true;
		while ( entry.itemList.size() > 0 ) {
			VisualItem item = (VisualItem)entry.itemList.get(0);
			this.removeItem(entry, item, true);
		}
	} //

    private void sortAll() {
        Iterator entryIter = m_entryList.iterator();
        while ( entryIter.hasNext() ) {
            ItemEntry entry = (ItemEntry)entryIter.next();
            if ( entry.modified ) {
                Collections.sort(entry.itemList, m_comparator);
                entry.modified = false;
            }
        }
    } //
    
    /**
     * Returns all the VisualItems in the registry. Items are returned 
     * in their rendering order. This order is determined by the item 
     * comparator. The setItemComparator() method can
     * be used to control this ordering.
     * @param visibleOnly determines if only currently visible items should
     *  be included in the iteration.
     * @return iterator over all VisualItems, in rendering order
     */
    public synchronized Iterator getItems(boolean visibleOnly) {
        sortAll();
        return new CompositeItemIterator(m_entryList,
                        m_comparator,visibleOnly,false);
    } //
    
	/**
	 * Returns all the visible VisualItems in the registry. The order items 
	 * are returned will determine their rendering order. This order is 
	 * determined by the item comparator. The setItemComparator() method can
	 * be used to control this ordering.
	 * @return iterator over all visible VisualItems, in rendering order
	 */
	public synchronized Iterator getItems() {
	    sortAll();
		return new CompositeItemIterator(m_entryList,m_comparator,true,false);
	} //

	/**
	 * Returns all the visible VisualItems in the registry in <i>reversed</i>
	 * rendering order. This is used by Display instances to determine
     * which items are being manipulated during user interface events.
	 * @return iterator over all visible VisualItems, in reverse rendering order
	 */
	public synchronized Iterator getItemsReversed() {
		sortAll();
		return new CompositeItemIterator(m_entryList,m_comparator,true,true);
	} //

    /**
     * Returns all VisualItems in the specified item class, optionally screening
     * for only currently visible items. Items are returned in rendering order.
     * @param itemClass the item class for which to return an iterator 
     *  of VisualItems
     * @param visibleOnly indicates whether or not only currently visible items
     *  should be included in the iteration.
     * @return an Iterator over the requested VisualItems, in rendering order
     */
	public synchronized Iterator getItems(String itemClass, boolean visibleOnly) {
		ItemEntry entry = (ItemEntry)m_entryMap.get(itemClass);
		if ( entry != null ) {
			if ( entry.modified ) {
				Collections.sort(entry.itemList, m_comparator);
				entry.modified = false;
			}
			if ( visibleOnly ) {
				return new VisibleItemIterator(entry.itemList, false);
			} else {
				return entry.itemList.iterator();
			}
		} else {
			throw new IllegalArgumentException("The input string must be a"
						+ " recognized item class!");
		}
	} //

    /**
     * "Touches" an item class, marking it as modified. This causes the items
     * in this class to be re-sorted next time an Iterator over the class
     * is requested.
     * @param itemClass the item class to "touch"
     */
	public synchronized void touch(String itemClass) {
		ItemEntry entry = (ItemEntry)m_entryMap.get(itemClass);
		if ( entry != null ) {
			entry.modified = true;
		} else {
			throw new IllegalArgumentException("The input string must be a"
						+ " recognized item class!");		
		}
	} //
	
    /**
     * Touches the default item class for NodeItems.
     */
	public synchronized void touchNodeItems() {
		touch(DEFAULT_NODE_CLASS);
	} //
	
    /**
     * Touches the default item class for EdgeItems.
     */
	public synchronized void touchEdgeItems() {
		touch(DEFAULT_EDGE_CLASS);
	} //
	
    /**
     * Touches the default item class for AggregateItems.
     */
	public synchronized void touchAggregateItems() {
		touch(DEFAULT_AGGR_CLASS);
	} //

	/**
	 * Returns an iterator over all visible NodeItems, in rendering order.
	 * @return iterator over NodeItems in rendering order
	 */
	public synchronized Iterator getNodeItems() {
		return getItems(DEFAULT_NODE_CLASS, true);
	} //
	
	/**
	 * Returns an iterator over NodeItems, in rendering order. If 
	 * <code>visibleOnly</code> is true, only currently visible items will be
	 * returned. If it is false, all NodeItems currently in the queue will be
	 * returned. 
	 * @param visibleOnly true to show only visible items, false for all items
	 * @return an <code>Iterator</code> over items in rendering order.
	 */
	public synchronized Iterator getNodeItems(boolean visibleOnly) {
		return getItems(DEFAULT_NODE_CLASS, visibleOnly);
	} //
	
	/**
	 * Returns an iterator over all visible EdgeItems, in rendering order.
	 * @return iterator over EdgeItems in rendering order
	 */
	public synchronized Iterator getEdgeItems() {
		return getItems(DEFAULT_EDGE_CLASS, true);
	} //
	
	/**
	 * Returns an iterator over EdgeItems, in rendering order. If 
	 * <code>visibleOnly</code> is true, only currently visible items will be
	 * returned. If it is false, all EdgeItems currently in the queue will be
	 * returned. 
	 * @param visibleOnly true to show only visible items, false for all items
	 * @return an <code>Iterator</code> over items in rendering order.
	 */
	public synchronized Iterator getEdgeItems(boolean visibleOnly) {
		return getItems(DEFAULT_EDGE_CLASS, visibleOnly);
	} //
	
	/**
	 * Returns an iterator over all visible AggregateItems, in rendering order.
	 * @return iterator over AggregateItems in rendering order
	 */
	public synchronized Iterator getAggregateItems() {
		return getItems(DEFAULT_AGGR_CLASS, true);
	} //
	
	/**
	 * Returns an iterator over AggregateItems, in rendering order. If 
	 * <code>visibleOnly</code> is true, only currently visible items will be
	 * returned. If it is false, all AggregateItems currently in the queue will
	 * be returned. 
	 * @param visibleOnly true to show only visible items, false for all items
	 * @return an <code>Iterator</code> over items in rendering order.
	 */
	public synchronized Iterator getAggregateItems(boolean visibleOnly) {
		return getItems(DEFAULT_AGGR_CLASS, visibleOnly);
	} //
	
	/**
	 * Returns the entity associated with the given VisualItem, if any.
	 * If multiple entities are associated with an input VisualItem of
	 * type AggregateItem, the first one is returned. To get all entities
	 * in such cases use the getEntities() method instead.
	 * @param item
	 * @return Entity
	 */
	public synchronized Entity getEntity(VisualItem item) {
		Object o = m_entityMap.get(item);
		if ( o == null ) {
			return null;
		} else if ( o instanceof Entity ) {
			return (Entity)o;
		} else {
			return (Entity)((List)o).get(0);
		}
	} //
	
	/**
	 * Returns the entities associated with the given VisualItem, if any.
	 * @param item
	 * @return Entity
	 */
	public synchronized List getEntities(VisualItem item) {
		Object o = m_entityMap.get(item);
		List list;
		if ( o instanceof Entity ) {
			(list = new LinkedList()).add(o);
		} else {
			list = (List)o;
		}
		return list;
	} //
	
	/**
	 * Determines if a node is visible (i.e. directly displayed by the
	 * visualization, not as part of an aggregate).
	 */
	public synchronized boolean isVisible(Node n) {
		NodeItem item;
		return ( (item=getNodeItem(n)) != null && item.isVisible() );		 
	} //
	
    /**
     * Requests a VisualItem of the specified item class corresponding to a
     * given Entity, optionally creating the VisualItem if it doesn't already
     * exist.
     * @param itemClass the item class from which the VisualItem should be taken
     * @param entity the Entity that this VisualItem is visualizing
     * @param create indicates whether or not the VisualItem should be created
     *  if it doesn't already exist.
     * @return the requested VisualItem, or null if the VisualItem wasn't found
     *  and the create parameter is false.
     */
	public synchronized VisualItem getItem(String itemClass, Entity entity, boolean create) {
		ItemEntry entry = (ItemEntry)m_entryMap.get(itemClass);
		if ( entry != null ) {
			VisualItem item = (VisualItem)entry.itemMap.get(entity);
			if ( !create ) {
				return item;
			} else if ( item == null ) {
				item = m_ifactory.getItem(itemClass);
				item.init(this, itemClass, entity);
				addItem(entry, entity, item);
			}
            if ( item instanceof NodeItem )
                ((NodeItem)item).removeAllNeighbors();
            item.setDirty(0);
            item.setVisible(true);
			return item;
		} else {
			throw new IllegalArgumentException("The input string must be a"
						+ " recognized item class!");
		}		
	} //
	
	/**
	 * Returns the visualized NodeItem associated with the given Node, if any.
	 * @param node the Node to look up
	 * @return NodeItem the NodeItem associated with the node, if any.
	 */
	public synchronized NodeItem getNodeItem(Node node) {
		return (NodeItem)getItem(DEFAULT_NODE_CLASS, node, false);			
	} //
	
	/**
	 * Returns the visualized NodeItem associated with the given Node, if any.
	 * If create is true, creates the desired NodeItem and adds it to the
	 * registry, and removes any previous bindings associated with the Node.
	 * @param node the Node to look up
	 * @param create if true, a new NodeItem will be allocated if necessary
	 * @return NodeItem the NodeItem associated with the node, if any
	 */
	public synchronized NodeItem getNodeItem(Node node, boolean create) {
		return (NodeItem)getItem(DEFAULT_NODE_CLASS, node, create);		
	} //

	/**
	 * Returns the visualized EdgeItem associated with the given Edge, if any.
	 * @param edge the Edge to look up
	 * @return EdgeItem the EdgeItem associated with the edge, if any
	 */
	public synchronized EdgeItem getEdgeItem(Edge edge) {
		return (EdgeItem)getItem(DEFAULT_EDGE_CLASS, edge, false);
	} //
	
	/**
	 * Returns the visualized EdgeItem associated with the given Edge, if any.
	 * If create is true, creates the desired EdgeItem and adds it to the
	 * registry, and removes any previous bindings associated with the Edge.
	 * @param edge the Edge to look up
	 * @param create if true, a new EdgeItem will be allocated if necessary
	 * @return EdgeItem the EdgeItem associated with the edge, if any
	 */
	public synchronized EdgeItem getEdgeItem(Edge edge, boolean create) {
		return (EdgeItem)getItem(DEFAULT_EDGE_CLASS, edge, create);		
	} //
	
	/**
	 * Returns the visualized AggregateItem associated with the given Entity,
	 * if any.
	 * @param entity the Entity to look up
	 * @return the AggregateItem associated with the entity, if any
	 */
	public synchronized AggregateItem getAggregateItem(Entity entity) {
		return (AggregateItem)getItem(DEFAULT_AGGR_CLASS, entity, false);
	} //
	
	/**
	 * Returns the visualized AggregateItem associated with the given Entity, if
	 * any. If create is true, creates the desired AggregateItem and adds it to
	 * the registry, and removes any previous bindings associated with the
	 * Entity.
	 * @param entity the Entity to look up
	 * @param create if true, a new AggregateItem will be allocated if 
	 *  necessary
	 * @return AggregateItem the AggregateItem associated with the entity, if any
	 */
	public synchronized AggregateItem getAggregateItem(Entity entity, boolean create) {
		return (AggregateItem)getItem(DEFAULT_AGGR_CLASS, entity, create);
	} //

	/**
	 * Add a mapping between the given entity and item, this means that
	 * the entity is part of the aggregation represented by the item.
	 * @param entity the Entity (e.g. Node or Edge) to add
	 * @param item the VisualItem
	 */	
	public synchronized void addMapping(Entity entity, VisualItem item) {
		String itemClass = item.getItemClass();
		ItemEntry entry = (ItemEntry)m_entryMap.get(itemClass);
		if ( entry != null ) {
			addMapping(entry, entity, item);
		} else {
			throw new IllegalArgumentException("The input string must be a"
						+ " recognized item class!");
		}
	} //
	
	/**
	 * Add a mapping between the given entity and the item within the
	 *  given item class
	 * @param entity the graph Entity to add
	 * @param item the VisualItem corresponding to the entity
	 */
	private synchronized void addMapping(ItemEntry entry, Entity entity, VisualItem item) {
		entry.itemMap.put(entity, item);
		if ( m_entityMap.containsKey(item) ) {
			Object o = m_entityMap.get(item);
			List list;
			if ( o instanceof List ) {
				list = (List)o;
			} else {
				(list = new LinkedList()).add(o);
			}
			list.add(entity);
			m_entityMap.put(item, list);
		} else {
			m_entityMap.put(item, entity);
		}
	} //
	
	/**
	 * Removes all extraneous mappings from an item 
	 * @param item the item to strip of all mappings
	 */
	public synchronized void removeMappings(VisualItem item) {
		ItemEntry entry = (ItemEntry)m_entryMap.get(item.getItemClass());
		if ( entry != null ) {
			removeMappings(entry, item);
		} else {
			throw new IllegalArgumentException("Didn't recognize the item's"
						+ " item class.");				
		}
	} //
	
	private synchronized void removeMappings(ItemEntry entry, VisualItem item) {
		if ( m_entityMap.containsKey(item) ) {
			Object o = m_entityMap.get(item);
			m_entityMap.remove(item);
			if ( o instanceof Entity ) {
				entry.itemMap.remove(o);
			} else {
				Iterator iter = ((List)o).iterator();
				while ( iter.hasNext() ) {
					entry.itemMap.remove(iter.next());
				}
			}
		}		
	} //

	/**
	 * Add a graph item to the visualization queue, and add a mapping
	 * between the given entity and the item.
	 * @param entity the graph Entity to add
	 * @param item the VisualItem corresponding to the entity
	 */
	private synchronized void addItem(ItemEntry entry, Entity entity, VisualItem item) {
		addItem(entry, item);
		addMapping(entry, entity, item);
	} //

	/**
	 * Add a graph item to the visualization queue, but do not add any new
	 * mappings.
	 * @param item the item to add the the visualization queue
	 */
	private synchronized void addItem(ItemEntry entry, VisualItem item) {
		entry.itemList.add(item);
		entry.modified = true;
        m_size++;
		if ( m_registryListener != null )
    		m_registryListener.registryItemAdded(item);
	} //
	
	/**
	 * Remove an item from the visualization queue.
	 * @param entry the <code>ItemEntry</code> for this item's item class.
	 * @param item the item to remove from the visualization queue
     * @param lr indicates whether or not to remove the item from it's
     *  rendering queue. This option is available to avoid errors that
     *  arise when removing items coming from a currently active Iterator.
	 */
	private synchronized void removeItem(ItemEntry entry, VisualItem item, boolean lr) {
		removeMappings(entry, item);
		if (lr) entry.itemList.remove(item);
        m_size--;
		if ( m_registryListener != null )
			m_registryListener.registryItemRemoved(item);
		m_ifactory.reclaim(item);
	} //

	/**
	 * Remove an item from the visualization queue.
	 * @param item the item to remove from the visualization queue
	 */
	public synchronized void removeItem(VisualItem item) {
		ItemEntry entry = (ItemEntry)m_entryMap.get(item.getItemClass());
		if ( entry != null ) {
			removeItem(entry, item, true);
		} else {
			throw new IllegalArgumentException("Didn't recognize the item's"
						+ " item class.");				
		}
	} //

  	// ========================================================================
  	// == LISTENER METHODS ====================================================

	/**
	 * Add an item registry listener.
	 * @param irl the listener to add.
	 */
  	public synchronized void addItemRegistryListener(ItemRegistryListener irl) {
    	m_registryListener = RegistryEventMulticaster.add(m_registryListener, irl);
  	} //

	/**
	 * Remove an item registry listener.
	 * @param irl the listener to remove.
	 */
  	public synchronized void removeItemRegistryListener(ItemRegistryListener irl) {
    	m_registryListener = RegistryEventMulticaster.remove(m_registryListener, irl);
  	} //

} // end of class ItemRegistry

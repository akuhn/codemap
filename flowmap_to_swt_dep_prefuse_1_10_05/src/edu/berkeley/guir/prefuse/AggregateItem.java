package edu.berkeley.guir.prefuse;

import java.util.List;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Visual representation of an aggregate of nodes and edges in a graph.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class AggregateItem extends NodeItem {

	private double m_orientation = 0;
	private double m_startOrientation = 0;
	private double m_endOrientation = 0;

	private int m_aggrSize;
	private NodeItem m_nitem;

	/**
	 * Initialize this AggregateItem, binding it to the given
	 * ItemRegistry and Entity.
	 * @param registry the ItemRegistry monitoring this VisualItem
	 * @param entity the Entity represented by this VisualItem
	 */
	public void init(ItemRegistry registry, String itemClass, Entity entity) {
		if (entity != null && !(entity instanceof Node)) {
			throw new IllegalArgumentException("AggregateItem can only represent an Entity of type Node.");
		}
		super.init(registry, itemClass, entity);
		
		VisualItem item = null;
		if ( entity instanceof Node ) {
			item = m_registry.getNodeItem((Node)entity);
		} else if ( entity instanceof Edge ) {
			item = m_registry.getEdgeItem((Edge)entity);
		}
		if ( item != null ) {
			setDOI(item.getDOI());
			setStartLocation(item.getStartLocation());
			setLocation(item.getLocation());
			setEndLocation(item.getEndLocation());
			setStartSize(item.getStartSize());
			setSize(item.getSize());
			setEndSize(item.getEndSize());
			setFont(item.getFont());
		}
	} //

	/**
	 * Clear the state of this AggregateItem.
	 */
	public void clear() {
		super.clear();
		m_aggrSize = 0;
		m_orientation = 0;
		m_startOrientation = 0;
		m_endOrientation = 0;
		m_location.setLocation(0, 0);
		m_startLocation.setLocation(0, 0);
		m_endLocation.setLocation(0, 0);
	} //

    /**
     * Get the entities associated with this aggregate.
     * @return a List of Entity instances "contained" in this aggregate
     */
	public List getEntities() {
		return m_registry.getEntities(this);
	} //
	
    /**
     * Returns the Entity "contained" in this aggregate at the given index.
     * @param idx the index of the Entity to return
     * @return the requested Entity
     */
	public Entity getEntity(int idx) {
		return (Entity)m_registry.getEntities(this).get(idx);
	} //

	public NodeItem getNodeItem() {
		return m_nitem;
	} //

	public void setNodeItem(NodeItem item) {
		m_nitem = item;
	} //

    /**
     * Returns the number of elements represented by this aggregate.
     * @return the number of elements represented by this aggregate.
     */
	public int getAggregateSize() {
		return m_aggrSize;
	} //

    /**
     * Sets the number of elements represented by this aggregate.
     * @param size the size to set.
     */
	public void setAggregateSize(int size) {
		m_aggrSize = size;
	} //

	/**
     * Returns the orientation of this aggregate, in radians
	 * @return the angle of orientation, in radians
	 */
	public double getOrientation() {
		return m_orientation;
	} //

	/**
	 * Sets the orientation of this aggregate, in radians
	 * @param orientation the orientation to set
	 */
	public void setOrientation(double orientation) {
		m_orientation = orientation;
	} //

	/**
	 * @return double
	 */
	public double getEndOrientation() {
		return m_endOrientation;
	} //

	/**
	 * @return double
	 */
	public double getStartOrientation() {
		return m_startOrientation;
	} //

	/**
	 * Sets the end orientation.
	 * @param endOrientation The ending orientation to set
	 */
	public void setEndOrientation(double endOrientation) {
		this.m_endOrientation = endOrientation;
	} //

	/**
	 * Sets the starting orientation.
	 * @param startOrientation The starting orientation to set
	 */
	public void setStartOrientation(double startOrientation) {
		this.m_startOrientation = startOrientation;
	} //

} // end of class AggregateItem

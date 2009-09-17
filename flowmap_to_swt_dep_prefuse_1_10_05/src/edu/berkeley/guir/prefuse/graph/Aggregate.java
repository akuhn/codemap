package edu.berkeley.guir.prefuse.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an aggregation of graph entities.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class Aggregate extends DefaultEntity {
	
	// The type of list instance used to store entities.
	protected static final Class LIST_TYPE = ArrayList.class;

	protected List m_entities;
	
	/**
	 * Default constructor. Creates a new, empty aggregate.
	 */
	public Aggregate() {
		try {
			m_entities = (List)LIST_TYPE.newInstance();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	} //
	
	/**
	 * Returns an iterator over all entities in this aggregate.
	 * @return an iterator over this aggregate's entities.
	 */
	public Iterator getAggregateEntities() {
		return m_entities.iterator();
	} //

	/**
	 * Indicates if a given entity is in this aggregate.
	 * @param n the entity to check for membership
	 * @return true if the entity is in this aggregate, false otherwise
	 */
	public boolean isAggregateEntity(Entity n) {
		return ( m_entities.indexOf(n) > -1 );
	} //

	/**
	 * Return the total number of entities in this aggregate. If this
	 * aggregate contains sub-aggregates, only the encompassing 
	 * sub-aggregate will be counted (i.e. entities in the sub-aggregate
	 * will not contribute to the count).
	 * @return the number of entities
	 */
	public int getNumAggregateEntities() {
		return m_entities.size();
	} //

	/**
	 * Add a new entity to this aggregate.
	 * @param n the node to add
	 */
	public void addAggregateEntity(Entity n) {
		if ( isAggregateEntity(n) )
			throw new IllegalStateException("Entity already contained in aggregate!");
		m_entities.add(n);
	} //
	
	/**
	 * Remove the given entity from this aggregate.
	 * @param n the entity to remove
	 */
	public boolean removeAggregateEntity(Entity n) {
		return ( m_entities.remove(n) );
	} //
	
} // end of class Aggregate

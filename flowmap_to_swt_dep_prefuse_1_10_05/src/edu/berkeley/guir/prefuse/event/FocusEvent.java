package edu.berkeley.guir.prefuse.event;

import java.util.EventObject;

import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.Entity;

/**
 * 
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FocusEvent extends EventObject {

    public static final Entity[] EMPTY = new Entity[0];    
    
    public static final int FOCUS_ADDED   = 0;
    public static final int FOCUS_REMOVED = 1;
    public static final int FOCUS_SET     = 2;

    private Entity[] m_added, m_removed;
    private long m_when;
    private int m_type;
    
    public FocusEvent(FocusSet src, int type, Entity[] added, Entity[] removed) {
        super(src);
        if ( type < FOCUS_ADDED || type > FOCUS_SET )
            throw new IllegalArgumentException("Unrecognized event type:"+type);
        m_when = System.currentTimeMillis();
        m_type = type;
        m_added = (added == null ? EMPTY : added);
        m_removed = (removed == null ? EMPTY : removed);
    } //
    
    public long getWhen() {
        return m_when;
    } //
    
    public int getEventType() {
        return m_type;
    } //
    
    public Entity[] getAddedFoci() {
        return m_added;
    } //
    
    public Entity[] getRemovedFoci() {
        return m_removed;
    } //
    
    public Entity getFirstAdded() {
        return ( m_added.length > 0 ? m_added[0] : null );
    } //
    
    public Entity getFirstRemoved() {
        return ( m_removed.length > 0 ? m_removed[0] : null );
    } //
    
} // end of class FocusEvent

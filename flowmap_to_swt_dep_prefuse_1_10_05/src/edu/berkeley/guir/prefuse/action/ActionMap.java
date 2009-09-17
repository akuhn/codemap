package edu.berkeley.guir.prefuse.action;

import java.util.HashMap;

/**
 * Maps between arbitrary object keys and Action instances. Useful for keeping
 * a store of Action instances for future parameterization.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ActionMap {

    private HashMap   m_map;
    private ActionMap m_parent;
    
    public ActionMap() {
        this(null);
    } //
    
    public ActionMap(ActionMap parent) {
        m_map = new HashMap();
        m_parent = parent;
    } //
    
    public void clear() {
        m_map.clear();
    } //
    
    public int size() {
        return m_map.size();
    } //
    
    public Action get(Object key) {
        Action a = (Action)m_map.get(key);
        return (a==null && m_parent!=null ? m_parent.get(key) : a);
    } //
    
    public Action put(Object key, Action action) {
        m_map.put(key, action);
        return action;
    } //
    
    public void remove(Object key) {
        m_map.remove(key);
    } //
    
    public Object[] keys() {
        return m_map.keySet().toArray();
    } //
    
    public Object[] allKeys() {
        Object[] a1 = m_map.keySet().toArray();
        if ( m_parent != null ) {
            Object[] a2 = m_parent.allKeys();
            if ( a2 != null && a2.length > 0 ) {
                Object[] o = new Object[a1.length+a2.length];
                System.arraycopy(a1,0,o,0,a1.length);
                System.arraycopy(a2,0,o,a1.length,a2.length);
                return o;
            }
        }
        return a1;
    } //
    
    public void setParent(ActionMap parent) {
        m_parent = parent;
    } //
    
    public ActionMap getParent() {
        return m_parent;
    } //
    
} // end of class ActionMap

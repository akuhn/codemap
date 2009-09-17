package edu.berkeley.guir.prefuse.graph.external;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.berkeley.guir.prefuse.ItemRegistry;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.event.GraphLoaderListener;
import edu.berkeley.guir.prefuse.graph.event.GraphLoaderMulticaster;


/**
 * Loads graph data from an external data source, such as a database or
 * filesystem, and manages a LRU cache for storing and evicting this data. By
 * default, the cache supports strict boundaries on it's size. This means
 * that otherwise active data may be evicted! To prevent this, set the
 * cache size appropriately!
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class GraphLoader implements Runnable {

    public static final int LOAD_NEIGHBORS = 0;
    public static final int LOAD_CHILDREN  = 1;
    public static final int LOAD_PARENT    = 2;
    
    protected List m_queue = new LinkedList();
    
    protected Graph m_graph;
    protected ItemRegistry m_registry;
    
    protected int m_maxSize = 5000;
    
    protected String m_keyField;
    protected LinkedHashMap m_cache = new LinkedHashMap(m_maxSize,.75f,true) {
        public boolean removeEldestEntry(Map.Entry eldest) {
            return evict((ExternalEntity)eldest.getValue());
        }
    };
    protected GraphLoaderListener m_listener;
    
    public GraphLoader(ItemRegistry registry, String keyField) {
        m_keyField = keyField;
        m_registry = registry;
        m_graph = registry.getGraph();
        Thread t = new Thread(this);
        
        // we don't want this to slow down animation!
        // besides, most of its work is blocking on IO anyway...
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();
    } //
    
    public void setMaximumCacheSize(int size) {
        m_maxSize = size;
    } //
    
    public int getMaximumCacheSize() {
        return m_maxSize;
    } //
    
    public void addGraphLoaderListener(GraphLoaderListener l) {
        m_listener = GraphLoaderMulticaster.add(m_listener, l);
    } //
    
    public void removeGraphLoaderListener(GraphLoaderListener l) {
        m_listener = GraphLoaderMulticaster.remove(m_listener, l);
    } //
    
    public void touch(ExternalEntity e) {
        m_cache.get(e.getAttribute(m_keyField));
    } //
    
    public synchronized void loadNeighbors(ExternalNode n) {
        submit(new Job(LOAD_NEIGHBORS,n));
    } //
    
    public synchronized void loadChildren(ExternalTreeNode n) {
        submit(new Job(LOAD_CHILDREN,n));
    } //
    
    public synchronized void loadParent(ExternalTreeNode n) {
        submit(new Job(LOAD_PARENT,n));
    } //
    
    private synchronized void submit(Job j) {
        if ( !m_queue.contains(j) ) {
            m_queue.add(j);
            this.notifyAll();
        }
    } //
    
    public boolean evict(ExternalEntity eldest) {
        boolean b = m_cache.size()>m_maxSize && !m_registry.isVisible(eldest);
        if ( b && m_listener != null )
            m_listener.entityUnloaded(this, eldest);
        if ( b ) {
            eldest.unload();
            m_graph.removeNode(eldest); 
        }
        return b;
    } //
    
    public void run() {
        while ( true ) {
            Job job = getNextJob();
            if ( job != null ) {
                if ( job.type == LOAD_NEIGHBORS ) {
                    ExternalNode n = (ExternalNode)job.n;
                    getNeighbors(n);
                    n.setNeighborsLoaded(true);
                } else if ( job.type == LOAD_CHILDREN ) {
                    ExternalTreeNode n = (ExternalTreeNode)job.n;
                    getChildren(n);
                    n.setChildrenLoaded(true);
                } else if ( job.type == LOAD_PARENT ) {
                    ExternalTreeNode n = (ExternalTreeNode)job.n;
                    getParent(n);
                    n.setParentLoaded(true);
                }
            } else {
                // nothing to do, chill out until notified
                try {
                    synchronized (this) { wait(); }
                } catch (InterruptedException e) { }
            }
        }
    } //
    
    protected synchronized Job getNextJob() {
        return (m_queue.isEmpty() ? null : (Job)m_queue.remove(0));
    } //
    
    protected void foundNode(int type, ExternalEntity src, ExternalEntity n, Edge e) {
        boolean inCache = false;
        String key = n.getAttribute(m_keyField);
        if ( m_cache.containsKey(key) ) {
            // switch n reference to original loaded version 
            n = (ExternalEntity)m_cache.get(key);
            inCache = true;
        } else
            m_cache.put(key, n);
        
        n.setLoader(this);
        if (e == null && src != null )
            if ( type == LOAD_PARENT )
                e = new DefaultEdge(n, src, m_graph.isDirected());
            else
                e = new DefaultEdge(src, n, m_graph.isDirected());
        
        synchronized ( m_registry ) {
            if ( type == LOAD_NEIGHBORS ) {
                m_graph.addNode(n);
                if ( src != null )
                    m_graph.addEdge(e);
            } else if ( src != null && (type == LOAD_PARENT || type == LOAD_CHILDREN) ) {
                ((Tree)m_graph).addChild(e);
                if ( type == LOAD_CHILDREN )
                    ((ExternalTreeNode)n).setParentLoaded(true);
            }
        }
        
        if ( m_listener != null && !inCache )
            m_listener.entityLoaded(this,n);
    } //
    
    protected abstract void getNeighbors(ExternalNode n);
    protected abstract void getChildren(ExternalTreeNode n);
    protected abstract void getParent(ExternalTreeNode n);
    
    public class Job {
        public Job(int type, ExternalEntity n) {
            this.type = type;
            this.n = n;
        }
        int type;
        ExternalEntity n;
        public boolean equals(Object o) {
            if ( !(o instanceof Job) )
                return false;
            Job j = (Job)o;
            return ( type==j.type && n==j.n );
        }
        public int hashCode() {
            return type ^ n.hashCode();
        }
    } //
    
} // end of class GraphLoader

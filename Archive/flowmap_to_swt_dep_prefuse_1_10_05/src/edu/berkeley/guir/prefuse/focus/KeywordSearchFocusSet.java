package edu.berkeley.guir.prefuse.focus;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.StringTokenizer;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.event.FocusEventMulticaster;
import edu.berkeley.guir.prefuse.event.FocusListener;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.util.Trie;

/**
 * <p>
 * A {@link FocusSet FocusSet} implementation that performs efficient keyword
 * searches on graph data. The {@link #index(Iterator, String) index} method
 * should be used to register searchable graph data. Then the
 * {@link #search(String) search} method can be used to perform a search. The
 * matching search results then become the members of this 
 * <code>FocusSet</code>. This class uses a {@link Trie Trie} data structure
 * to find search results in time proportional to only the length of the
 * query string, however, only prefix matches will be identified as valid
 * search matches.
 * </p>
 * 
 * <p>
 * <b>NOTE:</b> The {@link #add(Entity) add}, (@link #remove(Entity) remove},
 * and {@link #set(Entity) set} methods are not supported by this 
 * implementation, and will generate exceptions if called. Instead, the focus
 * membership is determined by the search matches found using the
 * {@link #search(String) search} method.
 * </p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class KeywordSearchFocusSet implements FocusSet {

    private FocusListener m_listener = null;
    private LinkedHashSet m_set = new LinkedHashSet();
    private Trie m_trie;
    private Trie.TrieNode m_curNode;
    private String m_delim = ", ";
    private String m_query = null;
    
    /**
     * Creates a new KeywordSearchFocusSet that is not case sensitive.
     */
    public KeywordSearchFocusSet() {
        this(false);
    } //
    
    /**
     * Creates a new KeywordSearchFocusSet with the indicated case sensitivity.
     * @param caseSensitive true if the search routines should be case
     * sensitive, false otherwise.
     */
    public KeywordSearchFocusSet(boolean caseSensitive) {
        m_trie = new Trie(caseSensitive);
    } //
    
    /**
     * Adds a listener to monitor changes to this FocusSet.
     * @param fl the FocusListener to add
     */
    public void addFocusListener(FocusListener fl) {
        m_listener = FocusEventMulticaster.add(m_listener, fl);
    } //

    /**
     * Removes a listener currently monitoring this FocusSet.
     * @param fl the FocusListener to remove
     */
    public void removeFocusListener(FocusListener fl) {
        m_listener = FocusEventMulticaster.remove(m_listener, fl);
    } //

    /**
     * Returns the delimiter string used to divide attribute values
     * into separately indexed words. By default, this is just a
     * whitespace.
     * @return the delimiter string used by the indexer
     * @see java.util.StringTokenizer
     */
    public String getDelimiterString() {
        return m_delim;
    } //
    
    /**
     * Sets the delimiter string used to divide attribute values
     * into separately indexed words.
     * @param delim the new delimiter string used by the indexer
     * @see java.util.StringTokenizer
     */
    public void setDelimiterString(String delim) {
        m_delim = delim;
    } //
    
    /**
     * Returns the current search query, if any
     * @return the current;y active search query
     */
    public String getQuery() {
        return m_query;
    } //
    
    /**
     * Searches the indexed attributes of this FocusSet for matching
     * string prefixes, adding the Entity instances for each search match
     * to the FocusSet.
     * @param query the query string to search for. Indexed attributes
     *  with a matching prefix will be added to the FocusSet.
     */
    public void search(String query) {
        Entity[] rem = (Entity[])m_set.toArray(FocusEvent.EMPTY);
        m_set.clear();
        m_query = query;
        m_curNode = m_trie.find(query);
        if ( m_curNode != null ) {
            Iterator iter = trieIterator();
            while ( iter.hasNext() )
                m_set.add(iter.next());
        }
        Entity[] add = (Entity[])m_set.toArray(FocusEvent.EMPTY);
        FocusEvent fe = new FocusEvent(this, FocusEvent.FOCUS_SET, add, rem);
        m_listener.focusChanged(fe);
    } //
    
    /**
     * Provides a Tree of the underlying Trie backing this focus set.
     * This is here solely for debugging purposes.
     * @param entities the entities to index
     * @param attrName the Entity attribute to index
     * @return a Tree of the Trie
     */
    public static Tree getTree(Iterator entities, String attrName) {
        KeywordSearchFocusSet set = new KeywordSearchFocusSet(false);
        set.index(entities, attrName);
        return set.m_trie.tree();
    } //
    
    /**
     * Indexes the attribute values for the given attribute name for
     * each Entity in the provided Iterator. These values are used
     * to construct an internal data structure allowing fast searches
     * over these attributes. To index multiple attributes, simply call
     * this method multiple times with the desired attribute names.
     * @param entities an Iterator over Entity instances to index
     * @param attrName the name of the attribute to index
     */
    public void index(Iterator entities, String attrName) {
        while ( entities.hasNext() ) {
            Entity e = (Entity)entities.next();
            index(e, attrName);
        }
    } //
    
    public void index(Entity e, String attrName) {
        String s;
        if ( (s=e.getAttribute(attrName)) == null ) return;
        StringTokenizer st = new StringTokenizer(s,m_delim);
        while ( st.hasMoreTokens() ) {
            String tok = st.nextToken();
            addString(tok, e);
        }
    } //
    
    private void addString(String s, Entity e) {
        m_trie.addString(s,e);
    } //
    
    public void remove(Entity e, String attrName) {
        String s;
        if ( (s=e.getAttribute(attrName)) == null ) return;
        StringTokenizer st = new StringTokenizer(s,m_delim);
        while ( st.hasMoreTokens() ) {
            String tok = st.nextToken();
            removeString(tok, e);
        }
    } //
    
    private void removeString(String s, Entity e) {
        m_trie.removeString(s,e);
    } //
    
    /**
     * Clears this focus set, invalidating any previous search.
     * @see edu.berkeley.guir.prefuse.focus.FocusSet#clear()
     */
    public void clear() {
        m_curNode = null;
        m_query = null;
        Entity[] rem = (Entity[])m_set.toArray(FocusEvent.EMPTY);
        m_set.clear();
        FocusEvent fe = new FocusEvent(this, FocusEvent.FOCUS_REMOVED, null, rem);
        m_listener.focusChanged(fe);
    } //

    /**
     * Returns an Iterator over the Entity instances matching
     * the most recent search query.
     * @return an Iterator over the Entity instances matching
     * the most recent search query.
     */
    public Iterator iterator() {
        if ( m_curNode == null ) {
            return Collections.EMPTY_LIST.iterator();
        } else {
            return m_set.iterator();
        }
    } //
    
    private Iterator trieIterator() {
        return m_trie.new TrieIterator(m_curNode);
    } //

    /**
     * Returns the number of matches for the most recent search query.
     * @return the number of matches for the most recent search query.
     */
    public int size() {
        return (m_curNode==null ? 0 : m_set.size());
    } //

    /**
     * Indicates if a given Entity is contained within this FocusSet (i.e.
     * the Entity is currently a matching search result).
     * @param entity the Entity to check for containment
     * @return true if this Entity is in the FocusSet, false otherwise
     */
    public boolean contains(Entity entity) {
        return m_set.contains(entity);
    } //
    
    // ========================================================================
    // == UNSUPPORTED OPERATIONS ==============================================
    
    /**
     * This method is not supported by this implementation. Don't call it!
     * Instead, use the {@link #search(String) search} or
     * {@link #clear() clear} methods.
     */
    public void add(Entity focus) {
        throw new UnsupportedOperationException();
    } //
    /**
     * This method is not supported by this implementation. Don't call it!
     * Instead, use the {@link #search(String) search} or
     * {@link #clear() clear} methods.
     */
    public void add(Collection foci) {
        throw new UnsupportedOperationException();
    } //
    /**
     * This method is not supported by this implementation. Don't call it!
     * Instead, use the {@link #search(String) search} or
     * {@link #clear() clear} methods.
     */
    public void remove(Entity focus) {
        throw new UnsupportedOperationException();
    } //
    /**
     * This method is not supported by this implementation. Don't call it!
     * Instead, use the {@link #search(String) search} or
     * {@link #clear() clear} methods.
     */
    public void remove(Collection foci) {
        throw new UnsupportedOperationException();
    } //
    /**
     * This method is not supported by this implementation. Don't call it!
     * Instead, use the {@link #search(String) search} or
     * {@link #clear() clear} methods.
     */
    public void set(Entity focus) {
        throw new UnsupportedOperationException();
    } //
    /**
     * This method is not supported by this implementation. Don't call it!
     * Instead, use the {@link #search(String) search} or
     * {@link #clear() clear} methods.
     */
    public void set(Collection foci) {
        throw new UnsupportedOperationException();
    } //
    
}  // end of class KeywordSearchFocusSet

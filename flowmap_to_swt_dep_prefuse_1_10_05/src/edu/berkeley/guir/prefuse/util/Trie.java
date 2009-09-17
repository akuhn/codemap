package edu.berkeley.guir.prefuse.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import edu.berkeley.guir.prefuse.focus.KeywordSearchFocusSet;
import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Entity;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Represents a trie data structure (a play on the words "tree" and 
 * "retrieval"). This builds a tree structure representing a set of
 * words by indexing on word prefixes. It is useful for performing
 * prefix-based searches over large amounts of text in an
 * efficient manner.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 * @see KeywordSearchFocusSet
 */
public class Trie {

    public class TrieNode {
        boolean isLeaf;
        int leafCount = 0;
    } //
    public class TrieBranch extends TrieNode {
        char[] chars = new char[] {0};
        TrieNode[] children = new TrieNode[1];
    } //
    public class TrieLeaf extends TrieNode {
        public TrieLeaf(String word, Entity e) {
            this.word = word;
            entity = e;
            next = null;
            leafCount = 1;
        }
        String word;
        Entity entity;
        TrieLeaf next;
    } //
    public class TrieIterator implements Iterator {
        private LinkedList queue;
        private Entity next;
        public TrieIterator(TrieNode node) {
            queue = new LinkedList();
            queue.add(node);
        } //
        public boolean hasNext() {
            return !queue.isEmpty();
        } //
        public Object next() {
            if ( queue.isEmpty() )
                throw new NoSuchElementException();
            
            TrieNode n = (TrieNode)queue.removeFirst();
            Object o;
            if ( n instanceof TrieLeaf ) {
                TrieLeaf l = (TrieLeaf)n;
                o = l.entity;
                if ( l.next != null )
                    queue.addFirst(l.next);
                return o;
            } else {
                TrieBranch b = (TrieBranch)n;
                for ( int i = b.chars.length-1; i > 0; i-- ) {
                    queue.addFirst(b.children[i]);
                }
                if ( b.children[0] != null )
                    queue.addFirst(b.children[0]);
                return next();
            }
        } //
        public void remove() {
            throw new UnsupportedOperationException();
        } //
    } // end of inner clas TrieIterator
    
    private TrieBranch root = new TrieBranch();
    private boolean caseSensitive = false;
    
    public Trie(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    } //
    
    public void addString(String word, Entity e) {
        TrieLeaf leaf = new TrieLeaf(word,e);
        addLeaf(root, leaf, 0);
    } //
    
    public void removeString(String word, Entity e) {
        removeLeaf(root, word, e, 0);
    } //
    
    private final int getIndex(char[] chars, char c) {
        for ( int i=0; i<chars.length; i++ )
            if ( chars[i] == c ) return i;
        return -1;
    } //
    
    private final char getChar(String s, int i) {
        char c = ( i < 0 || i >= s.length() ? 0 : s.charAt(i) );
        return ( caseSensitive ? c : Character.toLowerCase(c) );
    } //
    
    private boolean removeLeaf(TrieBranch b, String word, Entity ent, int depth) {
        char c = getChar(word, depth);
        int i = getIndex(b.chars, c);
        
        if ( i == -1 ) {
            // couldn't find leaf
            return false;
        } else {
            TrieNode n = b.children[i];
            if ( n instanceof TrieBranch ) {
                TrieBranch tb = (TrieBranch)n;
                boolean rem = removeLeaf(tb, word, ent, depth+1);
                if ( rem ) {
                    b.leafCount--;
                    if ( tb.leafCount == 1 )
                        b.children[i] = tb.children[tb.children[0]!=null?0:1];
                }
                return rem;
            } else {
                TrieLeaf nl = (TrieLeaf)n;
                if ( nl.entity == ent ) {
                    b.children[i] = nl.next;
                    if ( nl.next == null )
                        repairBranch(b,i);
                    b.leafCount--;
                    return true;
                } else {
                    TrieLeaf nnl = nl.next;
                    while ( nnl != null && nnl.entity != ent ) {
                        nl = nnl; nnl = nnl.next;
                    }
                    if ( nnl == null )
                        return false; // couldn't find leaf
                    
                    // update leaf counts
                    for ( TrieLeaf tl = (TrieLeaf)n; tl.entity != ent; tl = tl.next )
                        tl.leafCount--;
                    
                    nl.next = nnl.next;
                    b.leafCount--;
                    return true;
                }     
            }
        }
    } //
    
    private void repairBranch(TrieBranch b, int i) {
        if ( i == 0 ) {
            b.children[0] = null;
        } else {
            int len = b.chars.length;
            char[] nchars = new char[len-1];
            TrieNode[] nkids = new TrieNode[len-1];
            System.arraycopy(b.chars,0,nchars,0,i);
            System.arraycopy(b.children,0,nkids,0,i);
            System.arraycopy(b.chars,i+1,nchars,i,len-i-1);
            System.arraycopy(b.children,i+1,nkids,i,len-i-1);
            b.chars = nchars;
            b.children = nkids;
        }
    } //
    
    private void addLeaf(TrieBranch b, TrieLeaf l, int depth) {
        b.leafCount += l.leafCount;
        
        char c = getChar(l.word, depth);
        int i = getIndex(b.chars, c);
        
        if ( i == -1 ) {
            addChild(b,l,c);
        } else {
            TrieNode n = b.children[i];
            if ( n == null ) {
                // we have completely spelled out the word
                b.children[i] = l;
            } else if ( n instanceof TrieBranch ) {
                // recurse down the tree
                addLeaf((TrieBranch)n,l,depth+1);
            } else {
                // node is a leaf, need to do a split?
                TrieLeaf nl = (TrieLeaf)n;
                if ( i==0 || (caseSensitive ? nl.word.equals(l.word) 
                                  : nl.word.equalsIgnoreCase(l.word)) )
                {
                    // same word, so chain the entries
                    for ( ; nl.next != null; nl = nl.next )
                        nl.leafCount++;
                    nl.leafCount++;
                    nl.next = l;
                } else {
                    // different words, need to do a split
                    TrieBranch nb = new TrieBranch();
                    b.children[i] = nb;
                    addLeaf(nb,nl,depth+1);
                    addLeaf(nb,l,depth+1);
                }
            }
        }
    } //
    
    private void addChild(TrieBranch b, TrieNode n, char c) {
        int len = b.chars.length;
        char[] nchars = new char[len+1];
        TrieNode[] nkids = new TrieNode[len+1];
        System.arraycopy(b.chars,0,nchars,0,len);
        System.arraycopy(b.children,0,nkids,0,len);
        nchars[len] = c;
        nkids[len] = n;
        b.chars = nchars;
        b.children = nkids;
    } //
    
    public TrieNode find(String word) {
        return (word.length() < 1 ? null : find(word, root, 0));
    } //
    
    private TrieNode find(String word, TrieBranch b, int depth) {
        char c = getChar(word, depth);
        int i = getIndex(b.chars, c);
        if ( i == -1 ) {
            return null; // not in trie
        } else if ( b.children[i] instanceof TrieLeaf ) {
            return b.children[i];
        } else if ( word.length()-1 == depth ) {
            return b.children[i]; // end of search
        } else {
            return find(word, (TrieBranch)b.children[i], depth+1); // recurse
        }
    } //
    
    public Tree tree() {
        TreeNode r = new DefaultTreeNode();
        r.setAttribute("label", "root");
        tree(root, r);
        return new DefaultTree(r);
    } //
    
    private void tree(TrieBranch b, TreeNode n) {
        for ( int i=0; i<b.chars.length; i++ ) {
            if ( b.children[i] instanceof TrieLeaf ) {
                TrieLeaf l = (TrieLeaf)b.children[i];
                while ( l != null ) {
                    TreeNode c = new DefaultTreeNode();
                    c.setAttribute("label", l.word);
                    Edge e = new DefaultEdge(n,c);
                    n.addChild(e);
                    l = l.next;
                }
            } else {
                DefaultTreeNode c = new DefaultTreeNode();
                c.setAttribute("label", String.valueOf(b.chars[i]));
                Edge e = new DefaultEdge(n,c);
                n.addChild(e);
                tree((TrieBranch)b.children[i], c);
            }
        }
    } //
    
} // end of class Trie

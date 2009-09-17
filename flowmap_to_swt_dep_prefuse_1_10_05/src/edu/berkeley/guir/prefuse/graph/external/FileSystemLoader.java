package edu.berkeley.guir.prefuse.graph.external;

import java.io.File;
import java.io.IOException;

import edu.berkeley.guir.prefuse.ItemRegistry;

/**
 * Loads graph data as needed from a local filesystem. Created nodes are given
 * four attributes:
 * <ul>
 *  <li>"label" - The filename, not including the full path
 *  <li>"filename" - The absolute filename, including the full path
 *  <li>"size" - The size of the file, in bytes
 *  <li>"modified" - The file's last modification date, in milliseconds since the epoch.
 * </ul>
 * The "filname" attribute is used as the key for indexing data in the
 * <tt>GraphLoader</tt>'s cache. The "modified" attribute is a
 * <tt>String</tt> instance representing a <tt>long</tt> value. Use the
 * <tt>java.util.Date</tt> class to create corresponding <tt>Date</tt> instances.
 *
 * @see GraphLoader
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FileSystemLoader extends GraphLoader {

    public FileSystemLoader(ItemRegistry registry) {
        super(registry, "filename");
    } //
    
    /**
     * @see edu.berkeley.guir.prefuse.graph.external.GraphLoader#getNeighbors(edu.berkeley.guir.prefuse.graph.external.ExternalNode)
     */
    protected void getNeighbors(ExternalNode n) {
        String filename = n.getAttribute("filename");
        File f = new File(filename);
        
        File p = f.getParentFile();
        if ( p != null )
            loadNode(GraphLoader.LOAD_NEIGHBORS, n, p);
        
        File[] fl = f.listFiles();
        if ( fl == null ) return;
        for ( int i=0; i<fl.length; i++ ) {
            loadNode(GraphLoader.LOAD_NEIGHBORS, n, fl[i]);
        }
    } //

    /**
     * @see edu.berkeley.guir.prefuse.graph.external.GraphLoader#getChildren(edu.berkeley.guir.prefuse.graph.external.ExternalTreeNode)
     */
    protected void getChildren(ExternalTreeNode n) {
        String filename = n.getAttribute("filename");
        File f = new File(filename);
        
        File[] fl = f.listFiles();
        if ( fl == null ) return;
        for ( int i=0; i<fl.length; i++ ) {
            loadNode(GraphLoader.LOAD_CHILDREN, n, fl[i]);
        }
    } //
    
    protected void getParent(ExternalTreeNode n) {
        String filename = n.getAttribute("filename");
        File f = new File(filename);
        
        File p = f.getParentFile();
        if ( p != null )
            loadNode(GraphLoader.LOAD_PARENT, n, p);
    } //
    
    public ExternalEntity loadNode(int type, ExternalEntity o, File f) {
        ExternalEntity n = null;
        try {
            f = f.getCanonicalFile();
            String filename = f.getName();
            
            if ( m_cache.containsKey(filename) ) {
                // node already loaded
                n = (ExternalEntity)m_cache.get(filename);
            } else {
                // need to load the node
                if ( type == GraphLoader.LOAD_NEIGHBORS )
                    n = new ExternalNode();
                else
                    n = new ExternalTreeNode();
                String name = f.getName();
                n.setAttribute("label",(name.equals("") ? f.getPath() : name));
                n.setAttribute("filename", f.getPath());
                n.setAttribute("size", String.valueOf(f.length()));
                n.setAttribute("modified", String.valueOf(f.lastModified()));
            }
            foundNode(type, o, n, null);
        } catch ( IOException ie ) {
            ie.printStackTrace();
        }
        return n;
    } //

} // end of class FileSystemLoader

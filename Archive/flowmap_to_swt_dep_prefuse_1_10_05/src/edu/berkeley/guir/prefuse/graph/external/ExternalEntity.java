package edu.berkeley.guir.prefuse.graph.external;

import edu.berkeley.guir.prefuse.graph.Node;

/**
 * 
 * Mar 11, 2004 - jheer - Created class
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface ExternalEntity extends Node {

    public void setLoader(GraphLoader loader);
    
    public void unload();
    
    public void touch();
    
} // end of interface

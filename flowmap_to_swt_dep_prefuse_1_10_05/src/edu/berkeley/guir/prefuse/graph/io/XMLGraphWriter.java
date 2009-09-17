package edu.berkeley.guir.prefuse.graph.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * Writes out a graph to an XGMML-format XML file. See
 * <a href="http://www.cs.rpi.edu/~puninj/XGMML/">www.cs.rpi.edu/~puninj/XGMML/</a>
 * for a description of the XGMML format.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> - prefuse(AT)jheer.org
 */
public class XMLGraphWriter extends AbstractGraphWriter {

	public static final String NODE   = "node";
	public static final String EDGE   = "edge";
	public static final String ATT    = "att";
	public static final String ID     = "id";
	public static final String LABEL  = "label";
	public static final String SOURCE = "source";
	public static final String TARGET = "target";
	public static final String WEIGHT = "weight";
	public static final String TYPE   = "type";
	public static final String NAME   = "name";
	public static final String VALUE  = "value";
	public static final String LIST   = "list";
	public static final String GRAPH  = "graph";
	public static final String DIRECTED = "directed";

	public static final String NODE_ATTR[] = {ID, LABEL, WEIGHT};
	public static final String EDGE_ATTR[] = {LABEL, WEIGHT	};

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.GraphWriter#writeGraph(edu.berkeley.guir.prefuse.graph.Graph, java.io.OutputStream)
	 */
	public void writeGraph(Graph g, OutputStream os) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedOutputStream(os));
		assignIDs(g);
		printGraph(pw, g);
		pw.flush();
	} //
	
	protected void assignIDs(Graph g) {
	    Set ids = initializeIDs(g);
        int curID = 0;
        String id;
		Iterator nodeIter = g.getNodes();
		while ( nodeIter.hasNext() ) {
			Node n = (Node)nodeIter.next();
			if ( n.getAttribute(ID) == null ) {
                do {
                    id = String.valueOf(++curID);
                } while ( ids.contains(id) );
				n.setAttribute(ID, id);
			}
		}
	} //
    
    private Set initializeIDs(Graph g) {
        Set s = new HashSet(g.getNodeCount()/2);
        String a;
        Iterator nodeIter = g.getNodes();
        while ( nodeIter.hasNext() ) {
            Node n = (Node)nodeIter.next();
            if ( (a=n.getAttribute(ID)) != null )
                s.add(a);
        }
        return s;
    } //
	
	private void printGraph(PrintWriter pw, Graph g) {
		int directed = g.isDirected() ? 1 : 0;
		pw.println("<!-- prefuse graph writer :: " + (new Date()) + " -->");
		pw.println("<"+GRAPH+" "+DIRECTED+"=\""+directed+"\">");
		pw.println("  <!-- nodes -->");
		Iterator nodeIter = g.getNodes();
		while ( nodeIter.hasNext() ) {
			Node n = (Node)nodeIter.next();
			printNode(pw, n);
		}
		pw.println("  <!-- edges -->");
		Iterator edgeIter = g.getEdges();
		while ( edgeIter.hasNext() ) {
			Edge e = (Edge)edgeIter.next();
			printEdge(pw, e);
		}		
		pw.println("</graph>");
	} //

	private void printNode(PrintWriter pw, Node n) {		
		pw.print("  <"+NODE);
		for ( int i = 0; i < NODE_ATTR.length; i++ ) {
			String key = NODE_ATTR[i];
			String val = n.getAttribute(key);
			if ( val != null )
				pw.print(" "+key+"=\""+val+"\"");
		}
		pw.print(">");
		
		Map attr = n.getAttributes();
		Iterator attrIter = attr.keySet().iterator();
		boolean hadAttr = false;
		while ( attrIter.hasNext() ) {
			String key = (String)attrIter.next();
			if ( contains(NODE_ATTR, key) ) continue;
			String val = (String)attr.get(key);
			if ( !hadAttr ) {
				pw.println(); hadAttr = true;
			}
			printAttr(pw, key, val);
		}
		pw.println("  </"+NODE+">");
	} //
	
	private void printEdge(PrintWriter pw, Edge e) {
		String source = e.getFirstNode().getAttribute(ID);
		String target = e.getSecondNode().getAttribute(ID);
		
		pw.print("  <"+EDGE);
		pw.print(" "+SOURCE+"=\""+source+"\"");
		pw.print(" "+TARGET+"=\""+target+"\"");
		for ( int i = 0; i < EDGE_ATTR.length; i++ ) {
			String key = EDGE_ATTR[i];
			String val = e.getAttribute(key);
			if ( val != null )
				pw.print(" "+key+"=\""+val+"\"");
		}
		pw.print(">");
		
		Map attr = e.getAttributes();
		Iterator attrIter = attr.keySet().iterator();
		boolean hadAttr = false;
		while ( attrIter.hasNext() ) {
			String key = (String)attrIter.next();
			if ( contains(EDGE_ATTR, key) ) continue;
			String val = (String)attr.get(key);
			if ( !hadAttr ) {
				pw.println(); hadAttr = true;
			}
			printAttr(pw, key, val);
		}
		pw.println("  </"+EDGE+">");
	} //
	
	private void printAttr(PrintWriter pw, String key, String val) {
		pw.println("    <"+ATT+" "+NAME+"=\""+key+"\" "+VALUE+"=\""+val+"\"/>");
	} //
	
	private boolean contains(String list[], String item) {
		for ( int i = 0; i < list.length; i++ ) {
			if ( list[i].equals(item) )
				return true;
		}
		return false;
	} //
	
} // end of class XMLGraphWriter

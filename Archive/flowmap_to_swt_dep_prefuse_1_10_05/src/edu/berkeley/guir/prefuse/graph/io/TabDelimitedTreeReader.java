package edu.berkeley.guir.prefuse.graph.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Reads in a tree from a tab-delimited text format.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TabDelimitedTreeReader extends AbstractTreeReader {

	public static final String COMMENT = "#";

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.TreeReader#loadTree(java.io.InputStream)
	 */
	public Tree loadTree(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line, label = null, parentLabel = null;
		int lineno = 0;
		
		boolean parsedHeader = false;
		List headers  = new ArrayList();
		Map nodeMap   = new HashMap();
		TreeNode root = null;
		int key = 0;	
		
		while ( (line=br.readLine()) != null ) {
			lineno++;
			try {					
				// skip commented lines
				if ( line.startsWith(COMMENT) ) { continue; }
				
				if ( !parsedHeader ) {
					StringTokenizer st = new StringTokenizer(line);
					while ( st.hasMoreTokens() ) {
						String tok = st.nextToken();					
						tok = tok.substring(0, tok.length()-1);
						headers.add(tok);
					}
					label        = (String)headers.get(0);
					parentLabel  = (String)headers.get(1);				
					parsedHeader = true;	
				} else {
					TreeNode n = new DefaultTreeNode();
					String values[] = line.split("\t");				
					for ( int i = 0; i < values.length; i++ ) {				
						n.setAttribute((String)headers.get(i), values[i]);					
					}
					n.setAttribute("Key",String.valueOf(key++));
					
					if ( nodeMap.containsKey(label) ) {
						String context = "[" + n.getAttribute(label) + "]";
						throw new IllegalStateException("Found duplicate node label: " 
															+ context + " line " + lineno);
					}
					nodeMap.put(n.getAttribute(label), n);
					
					String plabel = n.getAttribute(parentLabel);								
					if ( plabel.equals("") ) {
						if ( root != null ) {														
							String context = "[" + n.getAttribute(label) + "]";
							throw new IllegalStateException("Found multiple tree roots: "
															+ context+ " line " + lineno);
						} else {
							root = n;
						}
					} else if ( nodeMap.containsKey(plabel) ) {
						TreeNode p = (TreeNode)nodeMap.get(plabel);
						p.addChild(new DefaultEdge(p,n));
					}			
				}
				
			} catch ( NullPointerException npe ) {
				System.err.println(npe + " :: line " + lineno);
				npe.printStackTrace();				
			} catch ( IllegalStateException ise ) {
				System.err.println(ise);
			}
		}		
		br.close();
		
		// now perform clean-up! 
		// if nodes still don't have parents, try to 'adopt' them
		Iterator nodeIter = nodeMap.values().iterator();
		while ( nodeIter.hasNext() ) {
			try {
				TreeNode n = (TreeNode)nodeIter.next();
				if ( n.getParent() == null && n != root ) {
					String plabel = n.getAttribute(parentLabel);
					TreeNode p = (TreeNode)nodeMap.get(plabel);
					if ( p == null ) {
						String context = "[" + n.getAttribute(label) + ", " + plabel + "]";
						throw new IllegalStateException("Found parentless node: " + context);
					} else {
						p.addChild(new DefaultEdge(p,n));
					}
				}
			} catch ( NullPointerException npe ) {				
				npe.printStackTrace();
			} catch ( IllegalStateException ise ) {
				System.err.println(ise);
			}
		}
		
		System.out.println("Read in tree with "+(root.getDescendantCount()+1)+" nodes.");
		return new DefaultTree(root);
	} //

} // end of class TabDelimitedTreeReader

package edu.berkeley.guir.prefuse.graph.io;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.DefaultTree;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.Tree;
import edu.berkeley.guir.prefuse.graph.TreeNode;

/**
 * Reads in tree-structured data in the TreeML, XML-based format.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class TreeMLTreeReader extends AbstractTreeReader {

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.TreeReader#loadTree(java.io.InputStream)
	 */
	public Tree loadTree(InputStream is) throws IOException {
		try {		
			TreeMLHandler    handler   = new TreeMLHandler();
			SAXParserFactory factory   = SAXParserFactory.newInstance();
			SAXParser        saxParser = factory.newSAXParser();
			saxParser.parse(is, handler);
			return handler.getTree();
		} catch ( SAXException se ) {
			se.printStackTrace();
		} catch ( ParserConfigurationException pce ) {
			pce.printStackTrace();
		} 
		return null;
	} //

	/**
	 * Helper class that performs XML parsing of graph files using
	 * SAX (the Simple API for XML).
	 */
	public class TreeMLHandler extends DefaultHandler {
		
		public static final String TREE   = "tree";
		public static final String BRANCH = "branch";
		public static final String LEAF   = "leaf";
		public static final String ATTR   = "attribute";
		public static final String NAME   = "name";
		public static final String VALUE  = "value";
		
		private Tree m_tree = null;
		private TreeNode m_root = null;
		private TreeNode m_activeNode = null;
		private boolean m_directed = false;
		
		private boolean inNode, inEdge;
		
		public void startDocument() {
			m_tree = null;
		} //
		
		public void endDocument() {
			// construct tree
			m_tree = new DefaultTree(m_root);
		} //
		
		public void endElement(String namespaceURI, String localName, String qName) {
			if ( qName.equals(BRANCH) || qName.equals(LEAF) ) {
				m_activeNode = m_activeNode.getParent();
			}
		} //
		
		public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {			
			if ( qName.equals(BRANCH) || qName.equals(LEAF) ) {
				// parse a node element
				TreeNode n;
				if ( m_activeNode == null ) {
					n = new DefaultTreeNode();
					m_root = n;
				} else {
					n = new DefaultTreeNode();
                    m_activeNode.addChild(new DefaultEdge(m_activeNode,n));
				}
				m_activeNode = n;
			} else if ( qName.equals(ATTR) ) {
				// parse an attribute
				parseAttribute(atts);
			}
		} //
		
		protected void parseAttribute(Attributes atts) {
			String alName, name = null, value = null;
			for ( int i = 0; i < atts.getLength(); i++ ) {
				alName = atts.getQName(i);
				if ( alName.equals(NAME) ) {
					name = atts.getValue(i);
				} else if ( alName.equals(VALUE) ) {
					value = atts.getValue(i);
				}
			}
			if ( name == null || value == null ) {
				System.err.println("Attribute under-specified");
				return;
			}

			m_activeNode.setAttribute(name, value);
		} //
		
		public Tree getTree() {
			return m_tree;
		} //
		
	} // end of inner class TreeMLHandler
	
} // end of class TreeMLTReeReader

package edu.berkeley.guir.prefuse.graph.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.berkeley.guir.prefuse.graph.DefaultEdge;
import edu.berkeley.guir.prefuse.graph.Edge;
import edu.berkeley.guir.prefuse.graph.Graph;
import edu.berkeley.guir.prefuse.graph.DefaultNode;
import edu.berkeley.guir.prefuse.graph.DefaultGraph;
import edu.berkeley.guir.prefuse.graph.Node;

/**
 * <p>Reads in a graph from an XGMML-format XML file.
 * See <a href="http://www.cs.rpi.edu/~puninj/XGMML/">
 * www.cs.rpi.edu/~puninj/XGMML/</a>
 * for a description of the XGMML format.</p>
 * 
 * <p>This class supports setting the node type to use when building the graph.
 * For example, one should set the node type to DefaultTreeNode.class if one 
 * wants to impose tree structures on the input graph.</p>
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class XMLGraphReader extends AbstractGraphReader	implements GraphReader {

	protected Class NODE_TYPE = DefaultNode.class;

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.GraphReader#loadGraph(java.io.InputStream)
	 */
	public Graph loadGraph(InputStream is) throws IOException {
		try {		
			XMLGraphHandler handler    = new XMLGraphHandler();
			SAXParserFactory factory   = SAXParserFactory.newInstance();
			SAXParser        saxParser = factory.newSAXParser();
			saxParser.parse(is, handler);
			return handler.getGraph();
		} catch ( SAXException se ) {
			se.printStackTrace();
		} catch ( ParserConfigurationException pce ) {
			pce.printStackTrace();
		} 
		return null;
	} //
	
	/**
	 * Return the class type used for representing nodes.
	 * @return the Class of new DefaultNode instances.
	 */
	public Class getNodeType() {
		return NODE_TYPE;
	} //
	
	/**
	 * Set the type to be used for node instances. All created nodes
	 * will be instances of the input type. The input class should be
	 * a descendant class of edu.berkeley.guir.prefuse.graph.DefaultNode.
	 * @param type the class type to use for node instances.
	 */
	public void setNodeType(Class type) {
		NODE_TYPE = type;
	} //
	
	/**
	 * Helper class that performs XML parsing of graph files using
	 * SAX (the Simple API for XML).
	 */
	public class XMLGraphHandler extends DefaultHandler {
		
		public static final String NODE   = "node";
		public static final String EDGE   = "edge";
		public static final String ATT    = "att";
		public static final String ID     = "id";
		public static final String LABEL  = "label";
		public static final String SOURCE = "source";
		public static final String TARGET = "target";
		public static final String TYPE   = "type";
		public static final String NAME   = "name";
		public static final String VALUE  = "value";
		public static final String LIST   = "list";
		public static final String WEIGHT = "weight";
		public static final String GRAPH  = "graph";
		public static final String DIRECTED = "directed";
		
		protected Graph m_graph = null;
		protected HashMap m_nodeMap = new HashMap();
		private Node m_activeNode = null;
		private Edge m_activeEdge = null;
		private boolean m_directed = false;
		
		private boolean inNode, inEdge;
		
		public void startDocument() {
			m_nodeMap.clear();
		} //
		
		public void endElement(String namespaceURI, String localName, String qName) {
			if ( qName.equals(NODE) ) {
				m_activeNode = null;
				inNode = false;
			} else if ( qName.equals(EDGE) ) {
				m_activeEdge = null;
				inEdge = false;
			}
		} //
		
		public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {			
			if ( qName.equals(GRAPH) ) {
				int idx = atts.getIndex(DIRECTED);
			    m_directed = (idx >= 0 && atts.getValue(idx).equals("1"));
                m_graph = new DefaultGraph(m_directed);
		    } else if ( qName.equals(NODE) ) {
				// parse a node element
				Node n = parseNode(atts);
				m_activeNode = n;
				inNode = true;
			} else if ( qName.equals(EDGE) ) {
				// parse an edge element
				Edge e = parseEdge(atts);
				m_activeEdge = e;
				inEdge = true;
			} else if ( qName.equals(ATT) ) {
				// parse an attribute
				parseAttribute(atts);
			}
		} //
		
		protected Node parseNode(Attributes atts) {
			String alName;
			String id = null;
			for ( int i = 0; i < atts.getLength(); i++ ) {
				if ( atts.getQName(i).equals(ID) ) {
					id = atts.getValue(i);
				}
			}
			if ( id == null ) {
				System.err.println("Node missing id");
				return null;
			}

			Node n = null;
			try {
				n = (Node)NODE_TYPE.newInstance();
			} catch ( Exception e ) {
				throw new RuntimeException(e);
			}
			n.setAttribute(ID,id);
			m_nodeMap.put(id, n);

			for ( int i = 0; i < atts.getLength(); i++ ) {
				alName = atts.getQName(i);
				if ( !alName.equals(ID) ) {
					n.setAttribute(alName, atts.getValue(i));
				}
			}
            m_graph.addNode(n);
			return n;
		} //
		
		protected Edge parseEdge(Attributes atts) {
			String alName;
			String source = null, target = null;
			String label = null, weight = null;

			for ( int i = 0; i < atts.getLength(); i++ ) {
				alName = atts.getQName(i);
				if ( alName.equals(SOURCE) ) {
					source = atts.getValue(i);
				} else if ( alName.equals(TARGET) ) {
					target = atts.getValue(i);
				} else if ( alName.equals(LABEL) ) {
					label = atts.getValue(i);
				} else if ( alName.equals(WEIGHT) ) {
					weight = atts.getValue(i);
				}
			}

			Node s = (Node)m_nodeMap.get(source);
			Node t = (Node)m_nodeMap.get(target);

			if ( source == null || target == null || s == null || t == null ) {
				System.err.println("Edge missing source or target");
				return null;
			}

			Edge e = new DefaultEdge(s,t,m_directed);
			if ( label != null ) e.setAttribute(LABEL, label);
			if ( weight != null ) e.setAttribute(WEIGHT, weight);
			
            m_graph.addEdge(e);
			return e;
		} //
		
		protected void parseAttribute(Attributes atts) {
			String alName, type = null, name = null, value = null;
			for ( int i = 0; i < atts.getLength(); i++ ) {
				alName = atts.getQName(i);
				if ( alName.equals(TYPE) ) {
					type = atts.getValue(i);
				} else if ( alName.equals(NAME) ) {
					name = atts.getValue(i);
				} else if ( alName.equals(VALUE) ) {
					value = atts.getValue(i);
				}
			}
			if ( (type != null && type.equals(LIST)) ||
					name == null || value == null ) {
				System.err.println("Attribute under-specified");
				return;
			}

			if ( inNode ) {
				m_activeNode.setAttribute(name, value);
			} else if ( inEdge ) {
				m_activeEdge.setAttribute(name, value);
			}
		} //
		
		public Graph getGraph() {
			return m_graph;
		} //
		
	} // end of inner class XMLGraphHandler

} // end of class XMLGraphReader

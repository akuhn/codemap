package edu.berkeley.guir.prefuse.graph.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import edu.berkeley.guir.prefuse.graph.Graph;

/**
 * Abstract class supporting GraphReader implementations.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class AbstractGraphReader implements GraphReader {

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.GraphReader#loadGraph(java.lang.String)
	 */
	public Graph loadGraph(String filename) 
		throws FileNotFoundException, IOException
	{
		return loadGraph(new FileInputStream(filename));		
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.GraphReader#loadGraph(java.net.URL)
	 */
	public Graph loadGraph(URL url) throws IOException {
		return loadGraph(url.openStream());
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.GraphReader#loadGraph(java.io.File)
	 */
	public Graph loadGraph(File f) throws FileNotFoundException, IOException {
		return loadGraph(new FileInputStream(f));
	}
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.GraphReader#loadGraph(InputStream)
	 */
	public abstract Graph loadGraph(InputStream is) throws IOException;

} // end of class AbstractGraphReader

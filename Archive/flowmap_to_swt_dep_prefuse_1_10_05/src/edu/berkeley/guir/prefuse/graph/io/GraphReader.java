package edu.berkeley.guir.prefuse.graph.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import edu.berkeley.guir.prefuse.graph.Graph;

/**
 * Interface by which to read in Graph instances from stored files.
 * 
 * May 21, 2003 - jheer - Created class
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface GraphReader {

	/**
	 * Load a graph from the file with the given filename.
	 * @param filename the file to load the graph from
	 * @return the loaded Graph
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Graph loadGraph(String filename) throws FileNotFoundException, IOException;
	
	/**
	 * Load a graph from the given URL.
	 * @param url the url to load the graph from
	 * @return the loaded Graph
	 * @throws IOException
	 */
	public Graph loadGraph(URL url) throws IOException;
	
	/**
	 * Load a graph from the given File.
	 * @param f the file to load the graph from
	 * @return the loaded Graph
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Graph loadGraph(File f) throws FileNotFoundException, IOException;
	
	/**
	 * Load a graph from the given InputStream.
	 * @param is the InputStream to load the graph from
	 * @return the loaded Graph
	 * @throws IOException
	 */
	public Graph loadGraph(InputStream is) throws IOException;

} // end of interface GraphReader

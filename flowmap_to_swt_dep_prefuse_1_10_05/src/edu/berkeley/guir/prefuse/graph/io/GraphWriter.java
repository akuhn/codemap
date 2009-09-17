package edu.berkeley.guir.prefuse.graph.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import edu.berkeley.guir.prefuse.graph.Graph;

/**
 * Interface by which to write Graph instances out.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface GraphWriter {

	/**
	 * Writes a graph to the file with the specified filename.
	 * @param g the Graph to write
	 * @param filename the file name of the file to write to
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeGraph(Graph g, String filename) throws FileNotFoundException, IOException;
	
	/**
	 * Writes a graph to the specified file.
	 * @param g the Graph to write
	 * @param f the file to write to
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void writeGraph(Graph g, File f) throws FileNotFoundException, IOException;
	
	/**
	 * Writes a graph to the specified OutputStream.
	 * @param g the Graph to write
	 * @param os the OutputStream to write to
	 * @throws IOException
	 */
	public void writeGraph(Graph g, OutputStream os) throws IOException;

} // end of interface GraphWriter

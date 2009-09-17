package edu.berkeley.guir.prefuse.graph.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import edu.berkeley.guir.prefuse.graph.Tree;

/**
 * Abstract class supporting TreeReader implementations.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public abstract class AbstractTreeReader implements TreeReader {

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.TreeReader#loadTree(java.lang.String)
	 */
	public Tree loadTree(String filename) 
		throws FileNotFoundException, IOException
	{
		return loadTree(new FileInputStream(filename));		
	} //

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.TreeReader#loadTree(java.net.URL)
	 */
	public Tree loadTree(URL url) throws IOException {
		return loadTree(url.openStream());
	}

	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.TreeReader#loadTree(java.io.File)
	 */
	public Tree loadTree(File f) throws FileNotFoundException, IOException {
		return loadTree(new FileInputStream(f));
	}
	
	/**
	 * @see edu.berkeley.guir.prefuse.graph.io.TreeReader#loadTree(InputStream)
	 */
	public abstract Tree loadTree(InputStream is) throws IOException;

} // end of class AbstractTreeReader

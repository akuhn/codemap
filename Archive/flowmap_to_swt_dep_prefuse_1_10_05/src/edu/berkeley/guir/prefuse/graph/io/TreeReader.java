package edu.berkeley.guir.prefuse.graph.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import edu.berkeley.guir.prefuse.graph.Tree;

/**
 * Interface by which to read in DefaultTree instances from stored files.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface TreeReader {

	/**
	 * Load a tree from the file with the given filename.
	 * @param filename the file name of the file containing the tree
	 * @return the loaded DefaultTree
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Tree loadTree(String filename) throws FileNotFoundException, IOException;
	
	/**
	 * Load a tree from the given url.
	 * @param url the url to read the tree from
	 * @return the loaded DefaultTree
	 * @throws IOException
	 */
	public Tree loadTree(URL url) throws IOException;
	
	/**
	 * Load a tree from the given file
	 * @param f the file to read the tree from
	 * @return the loaded DefaultTree
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Tree loadTree(File f) throws FileNotFoundException, IOException;
	
	/**
	 * Load a tree from the given input stream
	 * @param is the input stream to read the tree from
	 * @return the loaded DefaultTree
	 * @throws IOException
	 */
	public Tree loadTree(InputStream is) throws IOException;

} // end of interface TreeReader

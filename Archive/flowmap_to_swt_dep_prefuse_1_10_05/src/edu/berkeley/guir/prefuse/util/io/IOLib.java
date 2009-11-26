package edu.berkeley.guir.prefuse.util.io;

import java.io.File;

/**
 * Useful library routines for input/ouput tasks
 *  
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class IOLib {

    private IOLib() {
        // disallow instantiation
    } //
    
    /**
     * Returns the extension for a file or null if there is none
     * @param f the input file
     * @return the file extension, or null if none
     */
    public static String getExtension(File f) {
    	return (f != null ? getExtension(f.getName()) : null);
    } //
    
    /**
     * Returns the extension for a file or null if there is none
     * @param filename the input filename
     * @return the file extension, or null if none
     */
    public static String getExtension(String filename) {
	    int i = filename.lastIndexOf('.');
	    if ( i>0 && i<filename.length()-1 ) {
	        return filename.substring(i+1).toLowerCase();
	    } else {
	        return null;
	    }
    } //
    
} // end of class IOLib

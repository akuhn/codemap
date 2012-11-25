package edu.berkeley.guir.prefuse.util.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * SimpleFileFilter
 *  
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class SimpleFileFilter extends FileFilter {
    private String ext, desc;
    
    public SimpleFileFilter(String ext, String desc) {
        this.ext = ext;
        this.desc = desc;
    } //
    
    public boolean accept(File f) {
        if ( f == null )
            return false;
        if ( f.isDirectory() )
            return true;
        String extension = IOLib.getExtension(f);
        return ( extension != null && extension.equals(ext) );
    } //
    
    public String getDescription() {
        return desc;
    } //
    
    public String getExtension() {
        return ext;
    } //
    
} // end of class SimpleFileFilter

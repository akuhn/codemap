package ch.deif.meander;

import org.codemap.util.MapScheme;


public class DefaultLabelScheme extends MapScheme<String> {
    
    @Override
    public String forLocation(Point location) {
        // assumes the document names are paths
        String name = location.getDocument();
        int lastPathSeparator = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
        int lastDot = name.lastIndexOf('.');
        if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
        return name;
    }
    
}
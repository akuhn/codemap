/**
 * 
 */
package org.codemap.layers;

import org.codemap.util.MapScheme;

final class DefaultLabelScheme extends MapScheme<String> {
    @Override
    public String forLocation(org.codemap.Point location) {
        String name = location.getDocument();
        int lastPathSeparator = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
        int lastDot = name.lastIndexOf('.');
        if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
        return name;
    }
}
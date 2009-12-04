/**
 * 
 */
package ch.deif.meander.swt;

import ch.deif.meander.util.MapScheme;

final class DefaultLabelScheme extends MapScheme<String> {
    @Override
    public String forLocation(ch.deif.meander.Point location) {
        String name = location.getDocument();
        int lastPathSeparator = Math.max(name.lastIndexOf('\\'), name.lastIndexOf('/'));
        int lastDot = name.lastIndexOf('.');
        if (lastPathSeparator < lastDot) return name.substring(lastPathSeparator + 1, lastDot);
        return name;
    }
}
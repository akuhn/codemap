package ch.deif.meander;

import java.util.Collection;

/** A set of identifier handles.
 * External classes can use the collection interface to add and remove handles. 
 * Internal classes use {@link #locationsOn(MapInstance)} to iterate over all selected locations of a given map instance. 
 * 
 * @author Adrian Kuhn
 * @author David Erni
 *
 */
public class MapSelection extends AbstractMapSelection {

    @Override
    public boolean contains(Location element) {
        return contains(element.getDocument());
    }

    public void replaceAll(Collection<String> newLocations) {
        boolean retainChange = value.retainAll(newLocations);
        boolean addChange = value.addAll(newLocations);
        if (retainChange || addChange) this.changed();
    }	

}

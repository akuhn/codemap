package org.codemap.resources;

import java.util.Collection;
import java.util.EventObject;

import org.codemap.MapSelection;

import ch.akuhn.values.CollectionValue;

public class MapSelectionsValue extends CollectionValue<MapSelection> {
    
    @Override
    public boolean add(MapSelection element) {
        element.addDependent(this);
        return super.add(element);
    }
    
    @Override
    public boolean addAll(Collection<? extends MapSelection> collection) {
        for (MapSelection each: collection) each.addDependent(this);
        return super.addAll(collection);
    }
    
    @Override
    public boolean remove(Object object) {
        removeDependant(object);
        return super.remove(object);
    }
    
    private void removeDependant(Object object) {
        if (object instanceof MapSelection) {
            ((MapSelection) object).removeDependent(this);        
        }
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        for(Object each: collection) {
            removeDependant(each);            
        }
        return super.removeAll(collection);
    }
    
    @Override
    public boolean retainAll(Collection<?> collection) {
        // nahh we don't
        throw new UnsupportedOperationException();
    }
    
    /**
     * Propagate change on contained objects to listeners on 
     * this MapSelectionsValue.
     */
    @Override
    public void valueChanged(EventObject event) {
        changed();
    }

}

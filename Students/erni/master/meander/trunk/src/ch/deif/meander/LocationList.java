package ch.deif.meander;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.akuhn.hapax.corpus.Document;

/** List of locations, (value object).
 * 
 * @author Adrian Kuhn
 *
 */
public class LocationList implements Iterable<Location> {

    private List<Location> locations;
    
    public LocationList() {
        this.locations = new ArrayList<Location>();
    }
    
    @Override
    public Iterator<Location> iterator() {
        return locations.iterator();
    }
    
    public int count() {
        return locations.size();
    }
    
    public Location makeLocation(double x, double y, double elevation) {
        Location location = new Loc(x, y, elevation);
        locations.add(location);
        return location;
    }

    private class Loc implements Location {

        private double elevation;
        private double y, x;
        private Document document;

        private Loc(double x, double y, double elevation) {
            this.x = x;
            this.y = y; 
            this.elevation = elevation;
        }
        
        public double elevation() {
            return elevation;
        }
        
        public double x() {
            return x;
        }
        
        public double y() {
            return y;
        }

        @Override
        public Document getDocument() {
            return document;
        }

        @Override
        public void setDocument(Document document) {
            this.document = document;
        }
        
        private Location copyRestFrom(Location other) {
            this.document = ((Loc) other).document;
            return this;
        }
        
    }
    
    public LocationList normalizeElevation() {
        LocationList result = new LocationList();
        double maxElevation = maxElevation();
        for (Location each: locations) {
            result.locations.add(new Loc(each.x(), each.y(), 
                    each.elevation() / maxElevation * 100)
                    .copyRestFrom(each));
        }
        return result;
    }

    public double maxElevation() {
        double maxElevation = 0.0;
        for (Location each: locations) 
            maxElevation = Math.max(maxElevation, each.elevation());
        return maxElevation;
    }

    public Location at(int index) {
        return locations.get(index);
    }
    
}

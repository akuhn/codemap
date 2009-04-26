package ch.deif.meander;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static ch.unibe.scg.util.Extension.$;
import ch.akuhn.hapax.corpus.Document;

/** List of locations, (scalable value object).
 * 
 * @author Adrian Kuhn
 *
 */
public class LocationList implements Iterable<Location> {

    private List<Location> locations;
    private double maxElevation = 1.0;
    private int pixelScale = 200;
    
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
        maxElevation = Math.max(maxElevation, elevation);
        Location location = new Loc(x, y, elevation);
        locations.add(location);
        return location;
    }

    private class Loc implements Location {

        private double elevation;
        private double y, x;
        private Document document;
        private String name;

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
        
        @Override
        public float px() {
            return (float) (x * getPixelScale());
        }
        
        @Override
        public float py() {
            return (float) (y * getPixelScale());
        }
        
        @Override
        public double normElevation() {
            return elevation / maxElevation * 100;
        }

        @Override
        public String getName() {
            if (document == null) return String.valueOf(name);
            String name = (new File(document.name()).getName());
            return $(name).removeSuffix(".java");
        }

        @Override
        public void setName(String string) {
            this.name = string;
        }

        public void normalizeXY(double minX, double maxX, double minY, double maxY) {
            x = (x - minX) / (maxX - minX);
            y = (y - minY) / (maxY - minY);
        }
        
    }

    public Location at(int index) {
        return locations.get(index);
    }

    public void setPixelScale(int pixelScale) {
        this.pixelScale = pixelScale;
    }

    public int getPixelScale() {
        return pixelScale;
    }

    /*package*/ void normalizeXY() {
        double minX = 0, maxX = 0, minY = 0, maxY = 0;
        for (Location each: this) {
            minY = Math.min(minY, each.y());
            maxY = Math.max(maxY, each.y());
            minX = Math.min(minX, each.x());
            maxX = Math.max(maxX, each.x());
        }
        for (Location each: this) {
            ((Loc) each).normalizeXY(minX, maxX, minY, maxY);
        }
    }
    
}

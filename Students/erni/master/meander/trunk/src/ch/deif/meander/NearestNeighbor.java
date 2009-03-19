package ch.deif.meander;

import java.awt.Point;

public class NearestNeighbor {
    
    protected Map map;
    protected double shortest;
    protected Location location;
    
    public NearestNeighbor(Map map) {
        this.map = map;
    }
    
    public Location location() {
        return location;
    }
    
    public Point forLocation(Point point) {
        int xres = -1;
        int yres = -1;
        shortest = -1;
        int dimension = map.width;
        
        for(Location l: map.locations()) {
            //get location in map coordinates
            int x = (int) Math.round(l.x*dimension);
            int y = (int) Math.round(l.y*dimension);
            double dist = dist(x, y, point.x, point.y);
            if (shortest == -1 || dist < shortest) {
                location = l;
                shortest = dist;
                xres = x;
                yres = y;
            }
        }
        // currently we assume that there are locations
        assert shortest != -1;
        return new Point(xres, yres);
    }
    
    private static double dist(int x1, int y1, int x2, int y2) {
        int xdiff = x1-x2;
        int ydiff = y1-y2;
        return Math.sqrt(ydiff*ydiff + xdiff*xdiff);
    }    

}

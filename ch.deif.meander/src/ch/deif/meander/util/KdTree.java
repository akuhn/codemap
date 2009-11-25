package ch.deif.meander.util;

import static java.lang.Math.pow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.deif.meander.Location;
import ch.deif.meander.main.Log;
import ch.deif.meander.main.NullLog;

public class KdTree {

    private KdTreeNode root;
    private Log log = new NullLog();

    public KdTree(List<Location> locations) {
        assert locations.size() > 0;
        root = new KdTreeNode(locations, Axis.X, null);
    }

    public KdTreeNode getRoot() {
        return root;
    }

    public static class KdTreeNode {

        public final Axis axis;

        private Location location;
        private KdTreeNode lower;
        private KdTreeNode higher;
        private KdTreeNode parent;

        public KdTreeNode(List<Location> locations, Axis axis, KdTreeNode parent) {
            // Select axis based on depth so that axis cycles through all valid values
            this.axis = axis;
            this.parent = parent;
            Collections.sort(locations, new LocationComparator(axis));

            init(locations, axis.other());
        }

        private void init(List<Location> locations, Axis axis) {

            int median = locations.size() / 2;
            this.location = locations.get(median);

            List<Location> leftLocations = locations.subList(0, median);
            if (leftLocations.size() > 0) {
                lower = new KdTreeNode(leftLocations, axis, this);
            }
            List<Location> rightLocations = locations.subList(median+1, locations.size());
            if (rightLocations.size() > 0) {
                higher = new KdTreeNode(rightLocations, axis, this);
            }
        }

        public int[] getPosition() {
            return new int[]{location.px, location.py};
        }

        public KdTreeNode getLower() {
            return lower;
        }

        public KdTreeNode getHigher() {
            return higher;
        }

        public KdTreeNode findChild(int[] xy) {
            if (this.isHigher(xy)) {
                return lower;
            } else if (this.isLower(xy)) {
                return higher;
            }
            return null;
        }

        private boolean isLower(int[] xy) {
            return axis.get(getPosition()) <= axis.get(xy);
        }

        private boolean isHigher(int[] xy) {
            return axis.get(getPosition()) >= axis.get(xy);
        }

        public Location getLocation() {
            return location;
        }

        private KdTreeNode getOtherChild(KdTreeNode node) {
            if (lower != null && lower.equals(node)) {
                return higher;
            }
            if (higher != null && higher.equals(node)) {
                return lower;
            }
            return null;
        }
        
        public KdTreeNode getSibling() {
            if (parent == null) return null;
            return parent.getOtherChild(this);
        }        

        @Override
        public String toString() {
            return "Node: " + location.toString();
        }
    }

    public Location getNearestNeighbor(int[] xy) {
        return getNearestNeighbor(xy, root, null).getLocation();
    }

    private KdTreeNode getNearestNeighbor(int[] xy, KdTreeNode searchRoot, KdTreeNode rootSibling) {
        //      @see http://en.wikipedia.org/wiki/Kd-tree
        //        
        //      1. Starting with the root node, the algorithm moves down the tree recursively, in the same way that it would if the search point were being inserted (i.e. it goes right or left depending on whether the point is greater or less than the current node in the split dimension).
        //      2. Once the algorithm reaches a leaf node, it saves that node point as the "current best"
        //      3. The algorithm unwinds the recursion of the tree, performing the following steps at each node:
        //            1. If the current node is closer than the current best, then it becomes the current best.
        //            2. The algorithm checks whether there could be any points on the other side of the splitting 
        //               plane that are closer to the search point than the current best. In concept, this is done by 
        //               intersecting the splitting hyperplane with a hypersphere around the search point that has a radius
        //               equal to the current nearest distance. Since the hyperplanes are all axis-aligned this is
        //               implemented as a simple comparison to see whether the difference between the splitting 
        //               coordinate of the search point and current node is less than the distance (overall coordinates) 
        //               from the search point to the current best.
        //      
        //                  1. If the hypersphere crosses the plane, there could be nearer points on the other side of 
        //                     the plane, so the algorithm must move down the other branch of the tree from the current 
        //                     node looking for closer points, following the same recursive process as the entire search.
        //                  2. If the hypersphere doesn't intersect the splitting plane, then the algorithm continues 
        //                     walking up the tree, and the entire branch on the other side of that node is eliminated.
        //      
        //      4. When the algorithm finishes this process for the root node, then the search is complete.
        //      
        ArrayList<KdTreeNode> trace = new ArrayList<KdTreeNode>();
        KdTreeNode current;
        KdTreeNode next = searchRoot;
        // 1)
        do {
            current = next;            
            trace.add(current);
        } while ((next = current.findChild(xy)) != null);

        // 2)
        double bestDistance = squareDist(xy, current.location);
        KdTreeNode bestNeighbor = current;
        int traceindex = trace.size()-1;
        // 3)
        while(traceindex >= 0) {
            KdTreeNode traced = trace.get(traceindex);
            log.print();
            log.print("node from stack: ", traced);
            traceindex -= 1;
            // 3.1)
            double currentDistance = squareDist(xy, traced.location);
            if (currentDistance < bestDistance) {
                bestDistance = currentDistance;
                bestNeighbor = traced;
            }
            log.print("distance: ", currentDistance);
            log.print("bestdistance: ", bestDistance);
            log.print("new best node is: ", bestNeighbor);
            // 3.2)
            // 3.2.1)
            //          if (true) {
            double splitDistance = pow(traced.axis.get(traced.getPosition()) - traced.axis.get(xy), 2);
            log.print("splitdistance: ", splitDistance);
            if ( bestDistance >= splitDistance) {
                log.print("\ndescending into other tree\n", "***********************************");
                KdTreeNode sibling = traced.getSibling();
                if (sibling != null && (rootSibling == null || !rootSibling.equals(sibling))){
                    KdTreeNode leafNeighbor = getNearestNeighbor(xy, sibling, traced);
                    double leafDistance = squareDist(xy, leafNeighbor.location);
                    if (leafDistance < bestDistance) {
                        bestDistance = leafDistance;
                        bestNeighbor = leafNeighbor;
                    }
                }
                log.print("***********************************");
            }
            // 3.2.2)
        }
        return bestNeighbor;
    }

    public static double squareDist(int[] xy, Location location) {
        return squareDist(xy[0], xy[1], location.px, location.py);
    }

    public static double squareDist(int x1, int y1, int x2, int y2) {
        return pow(x1 - x2, 2) + pow(y1 - y2, 2);
    }

    public void setLog(Log l) {
        log  = l;
    }
}

/**
 * compares two locations on their x or y coordinate depending on the
 * current axis. 
 * 
 * @author deif
 */
class LocationComparator implements Comparator<Location> {

    private Axis axis;

    /**
     * @param axis the axis to compare on. 0 means x axis, 1 means y axis.
     */
    public LocationComparator(Axis axis) {
        this.axis = axis;
    }

    @Override
    public int compare(Location o1, Location o2) {
        return axis.compare(o1, o2, this);
    }

    int yAxis(Location o1, Location o2) {
        return o1.py - o2.py;
    }

    int xAxis(Location o1, Location o2) {
        return o1.px - o2.px;
    }
}

enum Axis {

    X, Y;

    Axis other() {
        if (isX()) return Y;
        return X;
    }

    int get(int[] xy) {
        if (isX()) return xy[0];
        return xy[1];
    }

    int compare(Location o1, Location o2, LocationComparator compare) {
        if (isX()) return compare.xAxis(o1, o2);
        return compare.yAxis(o1, o2);
    }

    boolean isX() {
        return this.equals(X);
    }
}


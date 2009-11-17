package ch.deif.meander.util;

import static java.lang.Math.pow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.deif.meander.Location;

public class KdTree {

    private KdTreeNode root;

    public KdTree(List<Location> locations) {
        assert locations.size() > 0;
        int k = 2;
        int depth = 0;
        
        root = new KdTreeNode(locations, depth, k);
    }

    public KdTreeNode getRoot() {
        return root;
    }
    
    public static class KdTreeNode {

        public final int axis;
        
        private Location location;
        private KdTreeNode left;
        private KdTreeNode right;

        public KdTreeNode(List<Location> locations, int depth, int dimensions) {
            // Select axis based on depth so that axis cycles through all valid values
            this.axis = depth % dimensions;
            
            Collections.sort(locations, new LocationComparator(axis));
            int median = locations.size() / 2;
            this.location = locations.get(median);
            
            List<Location> leftLocations = locations.subList(0, median);
            if (leftLocations.size() > 0) {
                left = new KdTreeNode(leftLocations, depth+1, dimensions);
            }
            List<Location> rightLocations = locations.subList(median+1, locations.size());
            if (rightLocations.size() > 0) {
                right = new KdTreeNode(rightLocations, depth+1, dimensions);
            }
        }

        public int[] getPosition() {
            return new int[]{location.px, location.py};
        }

        public KdTreeNode getLeft() {
            return left;
        }

        public KdTreeNode getRight() {
            return right;
        }

        public KdTreeNode findChild(int[] xy) {
            if (getPosition()[axis] >= xy[axis]) {
                return left;
            } else if (getPosition()[axis] <= xy[axis]) {
                return right;
            }
            return null;
        }

        public Location getLocation() {
            return location;
        }

        public KdTreeNode getOtherChild(KdTreeNode node) {
            if (left != null && left.equals(node)) {
                return right;
            }
            if (right != null && right.equals(node)) {
                return left;
            }
            return null;
        }
    }

    public Location getNearestNeighbor(int[] xy) {
        return getNearestNeighbor(xy, root).getLocation();
    }

    private KdTreeNode getNearestNeighbor(int[] xy, KdTreeNode searchRoot) {
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
      while(traceindex > 0) {
          KdTreeNode traced = trace.get(traceindex);
          traceindex -= 1;
          // 3.1)
          double currentDistance = squareDist(xy, traced.location);
          if (currentDistance < bestDistance) {
              bestDistance = currentDistance;
              bestNeighbor = traced;
          }
          // 3.2)
          // 3.2.1)          
          if ( bestDistance > pow(traced.getPosition()[traced.axis] - xy[traced.axis], 2)) {
              KdTreeNode otherChild = traced.getOtherChild(trace.get(traceindex+1));
              if (otherChild != null){
                  KdTreeNode leafNeighbor = getNearestNeighbor(xy, otherChild);
                  double leafDistance = squareDist(xy, leafNeighbor.location);
                  if (leafDistance < bestDistance) {
                      bestDistance = leafDistance;
                      bestNeighbor = leafNeighbor;
                  }
              }
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
}

class LocationComparator implements Comparator<Location> {
    
        private int axis;

        public LocationComparator(int axis) {
            this.axis = axis;
        }

        @Override
        public int compare(Location o1, Location o2) {
            if (axis == 0) return compareX(o1, o2);
            else return compareY(o1, o2);
        }

        private int compareY(Location o1, Location o2) {
            return o1.py - o2.py;
        }

        private int compareX(Location o1, Location o2) {
            return o1.px - o2.px;
        }
}


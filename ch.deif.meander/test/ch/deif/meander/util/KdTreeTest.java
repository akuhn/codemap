package ch.deif.meander.util;

import static org.junit.Assert.*;
import static ch.deif.meander.util.KdTree.squareDist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import ch.deif.meander.Location;
import ch.deif.meander.Point;
import ch.deif.meander.main.QuickNDirtyMap;
import ch.deif.meander.util.KdTree.KdTreeNode;

public class KdTreeTest {
    
    @Test
    public void testSqDist() {
        assertEquals(8, squareDist(0, 0, 2, 2), Double.MIN_VALUE);
        assertEquals(18, squareDist(0, 0, 3, 3), Double.MIN_VALUE);
        assertEquals(32, squareDist(0, 0, 4, 4), Double.MIN_VALUE);
        assertEquals(50, squareDist(0, 0, 5, 5), Double.MIN_VALUE);                
    }
    
    @Test
    public void testAdvancedTree() {
        final HashMap<Point, Integer> points = new HashMap<Point, Integer>();
        QuickNDirtyMap.addDebugPoints(points);
    }
    
    @Test
    public void testTree() {
        KdTree kdTree = makeTree(new int[][]{{2,3}, {5,4}, {9,6}, {4,7}, {8,1}, {7,2}});
        KdTreeNode root = kdTree.getRoot();
        assertArrayEquals(new int[]{7, 2}, root.getPosition());
        assertArrayEquals(new int[]{5, 4}, root.getLower().getPosition());
        assertArrayEquals(new int[]{9, 6}, root.getHigher().getPosition());
        
        Location nn = kdTree.getNearestNeighbor(new int[]{2, 2});
        assertEquals(2, nn.px);
        assertEquals(3, nn.py);
        
        nn = kdTree.getNearestNeighbor(new int[]{7, 0});
        assertEquals(7, nn.px);
        assertEquals(2, nn.py);
        
        nn = kdTree.getNearestNeighbor(new int[]{0, 3});
        assertEquals(2, nn.px);
        assertEquals(3, nn.py);
        
        nn = kdTree.getNearestNeighbor(new int[]{0, 3});
        assertEquals(2, nn.px);
        assertEquals(3, nn.py);
        
        nn = kdTree.getNearestNeighbor(new int[]{9, 1});
        assertEquals(8, nn.px);
        assertEquals(1, nn.py);
        
        nn = kdTree.getNearestNeighbor(new int[]{9, 1});
        assertEquals(8, nn.px);
        assertEquals(1, nn.py);
        
        nn = kdTree.getNearestNeighbor(new int[]{9, 10});
        assertEquals(9, nn.px);
        assertEquals(6, nn.py);        
    }

    private KdTree makeTree(int[][] points) {
        ArrayList<Location> locations = new ArrayList<Location>();
        for(int[] xy : points) {
            locations.add(new Location(new Point(0.0, 0.0, ""), 0.0f, xy[0], xy[1]));
        }
        return new KdTree(locations);
    }
}

package magic.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import magic.util.CycleDetector;

import org.junit.Test;

public class CycleDetectorTest {

    static class CD extends CycleDetector<Integer> {

        private Map<Integer,Collection<Integer>> data = new TreeMap();
        
        @Override
        public Collection<Integer> getChildren(Integer key) {
            return data.get(key);
        }

        public CD node(Integer i, Integer... is) {
            data.put(i, Arrays.asList(is));
            return (CD) this.put(i);
        }
        
    }
    
    @Test
    public void emptyGraph() {
        CD cd = new CD();
        assertEquals(null, cd.getCycle());
    }

    @Test
    public void cycleOfThree() {
        CD cd = new CD()
                .node(1, 2)
                .node(2, 3)
                .node(3, 1);
        List<Integer> cycle = cd.getCycle();
        assertNotNull(cycle);
        assertEquals(3, cycle.size());
    }
    
    @Test
    public void cycleOfOne() {
        CD cd = new CD()
                .node(1, 1);
        List<Integer> cycle = cd.getCycle();
        assertNotNull(cycle);
        assertEquals(1, cycle.size());
    }
    
    @Test
    public void noCycle() {
        CD cd = new CD()
                .node(1, 2, 3, 4)
                .node(2, 3)
                .node(3, 4)
                .node(4);
        List<Integer> cycle = cd.getCycle();
        assertNull(cycle);
    }

    @Test
    public void someCycle() {
        CD cd = new CD()
                .node(1, 2, 3, 4)
                .node(2, 3)
                .node(3, 4)
                .node(4, 2);
        List<Integer> cycle = cd.getCycle();
        assertNotNull(cycle);
    }
    
    @Test
    public void hasCycle() {
        CD cd = new CD()
                .node(1, 2)
                .node(2);
        assertEquals(false, cd.hasCycle());
        cd = new CD()
                .node(1, 2)
                .node(2, 1);
        assertEquals(true, cd.hasCycle());
    }
    
}

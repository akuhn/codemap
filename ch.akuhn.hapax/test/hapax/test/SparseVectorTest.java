package hapax.test;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.hapax.linalg.SparseVector;
import ch.akuhn.hapax.linalg.Vector.Entry;
import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
public class SparseVectorTest {
	
	double BIG_DELTA = 0.0000001;

    @Test
    public SparseVector empty() {
        SparseVector v = new SparseVector(10, 3);
        assertEquals(10, v.size());
        assertEquals(0, v.used());
        assertEquals(0, v.get(0), Double.MIN_VALUE);
        assertEquals(0, v.get(1), Double.MIN_VALUE);
        assertEquals(0, v.get(2), Double.MIN_VALUE);
        assertEquals(0, v.get(3), Double.MIN_VALUE);
        assertEquals(0, v.get(4), Double.MIN_VALUE);
        assertEquals(0, v.get(5), Double.MIN_VALUE);
        assertEquals(0, v.get(6), Double.MIN_VALUE);
        assertEquals(0, v.get(7), Double.MIN_VALUE);
        assertEquals(0, v.get(8), Double.MIN_VALUE);
        assertEquals(0, v.get(9), Double.MIN_VALUE);
        return v;
    }
    
    @Test @Given("#empty")
    public void putBeyondCapacity(SparseVector v) {
        v.put(1,0.1);
        assertEquals(1, v.used());
        v.put(2,0.2);
        assertEquals(2, v.used());
        v.put(3,0.3);
        assertEquals(3, v.used());
        v.put(4,0.4);
        assertEquals(4, v.used());
        assertEquals(0.1, v.get(1), BIG_DELTA);
        assertEquals(0.2, v.get(2), BIG_DELTA);
        assertEquals(0.3, v.get(3), BIG_DELTA);
        assertEquals(0.4, v.get(4), BIG_DELTA);
    }
    
    @Test @Given("#empty")
    public SparseVector putOneValue(SparseVector v) {
        v.put(4, 404);
        assertEquals(10, v.size());
        assertEquals(1, v.used());
        assertEquals(0, v.get(0), Double.MIN_VALUE);
        assertEquals(0, v.get(1), Double.MIN_VALUE);
        assertEquals(0, v.get(2), Double.MIN_VALUE);
        assertEquals(0, v.get(3), Double.MIN_VALUE);
        assertEquals(404, v.get(4), Double.MIN_VALUE);
        assertEquals(0, v.get(5), Double.MIN_VALUE);
        assertEquals(0, v.get(6), Double.MIN_VALUE);
        assertEquals(0, v.get(7), Double.MIN_VALUE);
        assertEquals(0, v.get(8), Double.MIN_VALUE);
        assertEquals(0, v.get(9), Double.MIN_VALUE);
        return v;
    }

    @Test @Given("#putMoreValues")
    public SparseVector preprendValue(SparseVector v) {
        v.put(2, 202);
        assertEquals(10, v.size());
        assertEquals(5, v.used());
        assertEquals(0, v.get(0), Double.MIN_VALUE);
        assertEquals(0, v.get(1), Double.MIN_VALUE);
        assertEquals(202, v.get(2), Double.MIN_VALUE);
        assertEquals(303, v.get(3), Double.MIN_VALUE);
        assertEquals(404, v.get(4), Double.MIN_VALUE);
        assertEquals(505, v.get(5), Double.MIN_VALUE);
        assertEquals(0, v.get(6), Double.MIN_VALUE);
        assertEquals(707, v.get(7), Double.MIN_VALUE);
        assertEquals(0, v.get(8), Double.MIN_VALUE);
        assertEquals(0, v.get(9), Double.MIN_VALUE);
        return v;
    }

    @Test @Given("#putMoreValues")
    public SparseVector appendValue(SparseVector v) {
        v.put(8, 808);
        assertEquals(10, v.size());
        assertEquals(5, v.used());
        assertEquals(0, v.get(0), Double.MIN_VALUE);
        assertEquals(0, v.get(1), Double.MIN_VALUE);
        assertEquals(0, v.get(2), Double.MIN_VALUE);
        assertEquals(303, v.get(3), Double.MIN_VALUE);
        assertEquals(404, v.get(4), Double.MIN_VALUE);
        assertEquals(505, v.get(5), Double.MIN_VALUE);
        assertEquals(0, v.get(6), Double.MIN_VALUE);
        assertEquals(707, v.get(7), Double.MIN_VALUE);
        assertEquals(808, v.get(8), Double.MIN_VALUE);
        assertEquals(0, v.get(9), Double.MIN_VALUE);
        return v;
    }
    
    @Test @Given("#putMoreValues")
    public SparseVector insertValue(SparseVector v) {
        v.put(6, 606);
        assertEquals(10, v.size());
        assertEquals(5, v.used());
        assertEquals(0, v.get(0), Double.MIN_VALUE);
        assertEquals(0, v.get(1), Double.MIN_VALUE);
        assertEquals(0, v.get(2), Double.MIN_VALUE);
        assertEquals(303, v.get(3), Double.MIN_VALUE);
        assertEquals(404, v.get(4), Double.MIN_VALUE);
        assertEquals(505, v.get(5), Double.MIN_VALUE);
        assertEquals(606, v.get(6), Double.MIN_VALUE);
        assertEquals(707, v.get(7), Double.MIN_VALUE);
        assertEquals(0, v.get(8), Double.MIN_VALUE);
        assertEquals(0, v.get(9), Double.MIN_VALUE);
        return v;
    }   
    
    @Test @Given("#putMoreValues")
    public SparseVector replaceValue(SparseVector v) {
        v.put(4, 414);
        assertEquals(10, v.size());
        assertEquals(4, v.used());
        assertEquals(0, v.get(0), Double.MIN_VALUE);
        assertEquals(0, v.get(1), Double.MIN_VALUE);
        assertEquals(0, v.get(2), Double.MIN_VALUE);
        assertEquals(303, v.get(3), Double.MIN_VALUE);
        assertEquals(414, v.get(4), Double.MIN_VALUE);
        assertEquals(505, v.get(5), Double.MIN_VALUE);
        assertEquals(0, v.get(6), Double.MIN_VALUE);
        assertEquals(707, v.get(7), Double.MIN_VALUE);
        assertEquals(0, v.get(8), Double.MIN_VALUE);
        assertEquals(0, v.get(9), Double.MIN_VALUE);
        return v;
    }      
    
    @Test @Given("#putOneValue")
    public SparseVector putMoreValues(SparseVector v) {
        v.put(3, 303);
        v.put(5, 505);
        v.put(7, 707);
        assertEquals(10, v.size());
        assertEquals(4, v.used());
        assertEquals(0, v.get(0), Double.MIN_VALUE);
        assertEquals(0, v.get(1), Double.MIN_VALUE);
        assertEquals(0, v.get(2), Double.MIN_VALUE);
        assertEquals(303, v.get(3), Double.MIN_VALUE);
        assertEquals(404, v.get(4), Double.MIN_VALUE);
        assertEquals(505, v.get(5), Double.MIN_VALUE);
        assertEquals(0, v.get(6), Double.MIN_VALUE);
        assertEquals(707, v.get(7), Double.MIN_VALUE);
        assertEquals(0, v.get(8), Double.MIN_VALUE);
        assertEquals(0, v.get(9), Double.MIN_VALUE);
        return v;
    }    
    
    @Test @Given("#putMoreValues")
    public void trim(SparseVector v) {
        assertEquals(10, v.size());
        assertEquals(4, v.used());
        v.trim();
        assertEquals(10, v.size());
        assertEquals(4, v.used());
        assertEquals(0, v.get(0), Double.MIN_VALUE);
        assertEquals(0, v.get(1), Double.MIN_VALUE);
        assertEquals(0, v.get(2), Double.MIN_VALUE);
        assertEquals(303, v.get(3), Double.MIN_VALUE);
        assertEquals(404, v.get(4), Double.MIN_VALUE);
        assertEquals(505, v.get(5), Double.MIN_VALUE);
        assertEquals(0, v.get(6), Double.MIN_VALUE);
        assertEquals(707, v.get(7), Double.MIN_VALUE);
        assertEquals(0, v.get(8), Double.MIN_VALUE);
        assertEquals(0, v.get(9), Double.MIN_VALUE);
    }
    
    @Test @Given("putMoreValues")
    public SparseVector replaceWithZero(SparseVector v) {
        v.put(4, 0.0);
        assertEquals(10, v.size());
        //assertEquals(3, v.used());
        assertEquals(0, v.get(0), Double.MIN_VALUE);
        assertEquals(0, v.get(1), Double.MIN_VALUE);
        assertEquals(0, v.get(2), Double.MIN_VALUE);
        assertEquals(303, v.get(3), Double.MIN_VALUE);
        assertEquals(0, v.get(4), Double.MIN_VALUE);
        assertEquals(505, v.get(5), Double.MIN_VALUE);
        assertEquals(0, v.get(6), Double.MIN_VALUE);
        assertEquals(707, v.get(7), Double.MIN_VALUE);
        assertEquals(0, v.get(8), Double.MIN_VALUE);
        assertEquals(0, v.get(9), Double.MIN_VALUE);
        return v;
    }
    
    @Test @Given("replaceWithZero") 
    @Ignore // TODO deal with putting 0.0 in SparseVector#put
    public void replaceWithZeroDecreasesUsedCount(SparseVector v) {
        assertEquals(3, v.used());
    }
    
    @Test @Given("#putMoreValues")
    public void entries(SparseVector v) {
        Iterator<Entry> it = v.entries().iterator();
        Entry e;
        assertEquals(true, it.hasNext());
        e = it.next();
        assertEquals(3, e.index);
        assertEquals(303, e.value, Double.MIN_VALUE);
        assertEquals(true, it.hasNext());
        e = it.next();
        assertEquals(4, e.index);
        assertEquals(404, e.value, Double.MIN_VALUE);
        assertEquals(true, it.hasNext());
        e = it.next();
        assertEquals(5, e.index);
        assertEquals(505, e.value, Double.MIN_VALUE);
        assertEquals(true, it.hasNext());
        e = it.next();
        assertEquals(7, e.index);
        assertEquals(707, e.value, Double.MIN_VALUE);
        assertEquals(false, it.hasNext());
    }
    
}

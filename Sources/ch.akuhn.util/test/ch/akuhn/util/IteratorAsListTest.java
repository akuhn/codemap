package ch.akuhn.util;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IteratorAsListTest {

    private List<String> words;
    
    @Before
    public void setup() {
        words = new LinkedList<String>();
        words.add("Lorem");
        words.add("ipsum");
        words.add("dolor");
        words.add("sit");
        words.add("amit");
    };
    
    @Test
    public void testLazyGet() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        assertEquals(words.get(1), ll.get(1));
        assertEquals(2, ll.size());
        assertEquals(words.get(3), ll.get(3));
        assertEquals(4, ll.size());
        assertEquals(words.get(2), ll.get(2));
        assertEquals(4, ll.size());
    }

    @Test
    public void testIsEmpty() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        assertEquals(false, ll.isEmpty());
        ll.upTo(2);
        assertEquals(false, ll.isEmpty());
        ll.upToEnd();
        assertEquals(false, ll.isEmpty());
    }
 
    @Test
    public void testIsEmpty2() {
        IteratorAsList<String> ll = new IteratorAsList<String>(Collections.<String>emptyList());
        assertEquals(true, ll.isEmpty());
        ll.upToEnd();
        assertEquals(true, ll.isEmpty());
    }
    
    @Test
    public void testSize() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        assertEquals(0, ll.size());
        ll.upTo(2);
        assertEquals(3, ll.size());
        ll.upToEnd();
        assertEquals(5, ll.size());
    }
    
    @Test(expected=IndexOutOfBoundsException.class)
    public void testGetBeyond() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        ll.get(words.size());
    }
 
    @Test
    public void testLazyIterator() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        ll.upTo(1);
        assertEquals(2, ll.size());
        Iterator<String> it = ll.iterator();
        assertEquals(true, it.hasNext());
        assertEquals(words.get(0), 
                it.next());
        assertEquals(2, ll.size());
        assertEquals(true, it.hasNext());
        assertEquals(words.get(1), 
                it.next());
        assertEquals(2, ll.size());
        assertEquals(true, it.hasNext());
        assertEquals(words.get(2), 
                it.next()); // strike!
        assertEquals(3, ll.size());
        assertEquals(true, it.hasNext());
        assertEquals(words.get(3), 
                it.next());
        assertEquals(4, ll.size());
        assertEquals(true, it.hasNext());
        assertEquals(words.get(4), 
                it.next());
        assertEquals(5, ll.size());
        assertEquals(false, it.hasNext());
    }
    
    @Test
    public void testLazyRemove() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        boolean found = ll.remove(words.get(2));
        assertEquals(true,found);
        assertEquals(2, ll.size());
        assertEquals(words.get(0), ll.get(0));
        assertEquals(words.get(1), ll.get(1));
        assertEquals(words.get(2+1), ll.get(2));
        assertEquals(words.get(3+1), ll.get(3));
    }
 
    @Test
    public void testLazyRemove2() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        boolean found = ll.remove("zork");
        assertEquals(false,found);
        assertEquals(words.size(), ll.size());
    }
  
    @Test
    public void testIndexOf() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        int found = ll.indexOf(words.get(2));
        assertEquals(2,found);
        assertEquals(3, ll.size());
    }
    
    @Test
    public void testIndexOf2() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        int found = ll.indexOf("zork");
        assertEquals(-1,found);
        assertEquals(words.size(), ll.size());
    }
   
    @Test
    public void testContains() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        boolean found = ll.contains(words.get(2));
        assertEquals(true,found);
        assertEquals(3, ll.size());
    }
    
    @Test
    public void testContains2() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        boolean found = ll.contains("zork");
        assertEquals(false,found);
        assertEquals(words.size(), ll.size());
    }    
    
    @Test
    public void testLastIndexOf() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        int found = ll.lastIndexOf(words.get(2));
        assertEquals(2,found);
        assertEquals(words.size(), ll.size());
    }
    
    @Test
    public void testLastIndexOf2() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        int found = ll.lastIndexOf("zork");
        assertEquals(-1,found);
        assertEquals(words.size(), ll.size());
    }
    
    @Test
    public void testToString() {
        IteratorAsList<String> ll = new IteratorAsList<String>(words);
        assertEquals("[Lorem, ...]", ll.toString());
        ll.upTo(2);
        assertEquals("[Lorem, ipsum, dolor, ...]", ll.toString());
        ll.upToEnd();
        assertEquals("[Lorem, ipsum, dolor, sit, amit]", ll.toString());
    }

    
}

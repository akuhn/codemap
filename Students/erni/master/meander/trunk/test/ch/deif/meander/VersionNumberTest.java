package ch.deif.meander;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VersionNumberTest {

    VersionNumber num, a, b; 
    
    @Test
    public void testOne() {
        num = new VersionNumber("6");
        assertEquals(1, num.numbers.length);
        assertEquals(6, num.numbers[0]);
    }

    @Test
    public void testTwo() {
        num = new VersionNumber("6.10");
        assertEquals(2, num.numbers.length);
        assertEquals(6, num.numbers[0]);
        assertEquals(10, num.numbers[1]);
    }
    
    @Test
    public void testThree() {
        num = new VersionNumber("6.10.4");
        assertEquals(3, num.numbers.length);
        assertEquals(6, num.numbers[0]);
        assertEquals(10, num.numbers[1]);
        assertEquals(4, num.numbers[2]);
    }
    
    @Test
    public void compareTwo() {
        a = new VersionNumber("6.10.4");
        b = new VersionNumber("6.11.4");
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }
    
    @Test
    public void compareOne() {
        a = new VersionNumber("7.5.10");
        b = new VersionNumber("6.8.11");
        assertTrue(a.compareTo(b) > 0);
        assertTrue(b.compareTo(a) < 0);
    }

    @Test
    public void compareThree() {
        a = new VersionNumber("6.10.5");
        b = new VersionNumber("6.10.0");
        assertTrue(a.compareTo(b) > 0);
        assertTrue(b.compareTo(a) < 0);
    }

    @Test
    public void compareThree2() {
        a = new VersionNumber("6.10.5");
        b = new VersionNumber("6.10");
        assertTrue(a.compareTo(b) > 0);
        assertTrue(b.compareTo(a) < 0);
    }

}

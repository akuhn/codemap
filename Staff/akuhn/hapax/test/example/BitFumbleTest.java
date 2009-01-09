package example;

import static org.junit.Assert.*;

import org.junit.Test;

public class BitFumbleTest {

    @Test
    public void testHiBit() {
        assertEquals("0",Integer.toString(0, 16));
        assertEquals("8000",Integer.toString(1 << 15, 16));
        assertEquals("-80000000",Integer.toString(1 << 31, 16));
        assertEquals("0",Integer.toString(1 << 32, 16));
    }
    
}

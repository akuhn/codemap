package ch.akuhn.util;

import static ch.akuhn.util.Get.first;
import static ch.akuhn.util.Strings.letters;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Test;

public class Lorem {

    public static final String IPSUM = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    
    public static final Iterable<String> ipsum(int num) {
        return first(num, letters(IPSUM));
    }
    
    public static final Iterable<String> ipsum() {
        return letters(IPSUM);
    }
    
    @Test
    public void testLorem() {
        Iterator<String> lorem = Lorem.ipsum(5).iterator();
        assertEquals(true, lorem.hasNext());
        assertEquals("Lorem", lorem.next());
        assertEquals(true, lorem.hasNext());
        assertEquals("ipsum", lorem.next());
        assertEquals(true, lorem.hasNext());
        assertEquals("dolor", lorem.next());
        assertEquals(true, lorem.hasNext());
        assertEquals("sit", lorem.next());
        assertEquals(true, lorem.hasNext());
        assertEquals("amet", lorem.next());
        assertEquals(false, lorem.hasNext());
    }
    
}

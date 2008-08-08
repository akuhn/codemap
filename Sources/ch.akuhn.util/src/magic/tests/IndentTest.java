package magic.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import jexample.Depends;
import jexample.JExampleRunner;

import magic.Tab;


@RunWith( JExampleRunner.class )
public class IndentTest {

    @Test
    public Tab testEmpty() {
        Tab n = new Tab("abc");
        assertEquals("", n.toString());
        assertEquals(true, n.isEmpty());
        return n;
    }
    
    @Test
    @Depends("testEmpty")
    public Tab testIncrement(Tab n) {
        n.more();
        assertEquals("abc", n.toString());
        n.more();
        assertEquals("abcabc", n.toString());
        n.more();
        assertEquals("abcabcabc", n.toString());
        return n;
    }
    
    @Test
    @Depends("testIncrement")
    public Tab testDecrement(Tab n) {
        assertEquals("abcabcabc", n.toString());
        n.less();
        assertEquals("abcabc", n.toString());
        n.less();
        assertEquals("abc", n.toString());
        n.less();
        assertEquals("", n.toString());
        return n;
    }
    
    @Test
    @Depends("testDecrement")
    public Tab testDone(Tab n) {
        assertEquals(true, n.isEmpty());
        return n;
    }
    
    @Test
    @Depends("testIncrement")
    public Tab testNotDone(Tab n) {
        assertEquals(false, n.isEmpty());
        return n;
    }
    
    @Test(expected = IllegalStateException.class)
    @Depends("testDone")
    public void testDecrementFailsWhenDone(Tab n) {
        n.less();
    }
    
}

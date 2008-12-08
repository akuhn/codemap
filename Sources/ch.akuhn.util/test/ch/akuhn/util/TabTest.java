package ch.akuhn.util;

import static org.junit.Assert.assertEquals;
import jexample.Depends;
import jexample.JExample;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JExample.class)
public class TabTest {

    @Test
    public void defaultValue() {
        assertEquals("\t", new Tab().more().toString());
    }

    @Test
    @Depends("#testEmpty")
    public void testBeginEnd(Tab n) {
        assertEquals("", n.toString());
        assertEquals("", n.begin());
        assertEquals("abc", n.toString());
        assertEquals("abc", n.toString());
        assertEquals("", n.end());
        assertEquals("", n.toString());
    }

    @Test
    @Depends("#testIncrement")
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

    @Test(expected = IllegalStateException.class)
    @Depends("#testDone")
    public void testDecrementFailsWhenDone(Tab n) {
        n.less();
    }

    @Test
    @Depends("#testDecrement")
    public Tab testDone(Tab n) {
        assertEquals(true, n.isEmpty());
        return n;
    }

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
    @Depends("#testIncrement")
    public Tab testNotDone(Tab n) {
        assertEquals(false, n.isEmpty());
        return n;
    }

}

package ch.akuhn.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
public class TabTest {

    @Test
    public void defaultValue() {
        assertEquals("\t", new Tab().more().toString());
    }

    @Test
    @Given("#testEmpty")
    public void testBeginEnd(Tab n) {
        assertEquals("", n.toString());
        assertEquals("", n.begin());
        assertEquals("abc", n.toString());
        assertEquals("abc", n.toString());
        assertEquals("", n.end());
        assertEquals("", n.toString());
    }

    @Test
    @Given("#testIncrement")
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
    @Given("#testDone")
    public void testDecrementFailsWhenDone(Tab n) {
        n.less();
    }

    @Test
    @Given("#testDecrement")
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
    @Given("testEmpty")
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
    @Given("#testIncrement")
    public Tab testNotDone(Tab n) {
        assertEquals(false, n.isEmpty());
        return n;
    }

}

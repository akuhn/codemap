package magic.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import jexample.Depends;
import jexample.JExampleRunner;

import magic.Indent;


@RunWith( JExampleRunner.class )
public class IndentTest {

    @Test
    public Indent testEmpty() {
        Indent n = new Indent("abc");
        assertEquals("", n.toString());
        assertEquals(true, n.done());
        return n;
    }
    
    @Test
    @Depends("testEmpty")
    public Indent testIncrement(Indent n) {
        n.inc();
        assertEquals("abc", n.toString());
        n.inc();
        assertEquals("abcabc", n.toString());
        n.inc();
        assertEquals("abcabcabc", n.toString());
        return n;
    }
    
    @Test
    @Depends("testIncrement")
    public Indent testDecrement(Indent n) {
        assertEquals("abcabcabc", n.toString());
        n.dec();
        assertEquals("abcabc", n.toString());
        n.dec();
        assertEquals("abc", n.toString());
        n.dec();
        assertEquals("", n.toString());
        return n;
    }
    
    @Test
    @Depends("testDecrement")
    public Indent testDone(Indent n) {
        assertEquals(true, n.done());
        return n;
    }
    
    @Test
    @Depends("testIncrement")
    public Indent testNotDone(Indent n) {
        assertEquals(false, n.done());
        return n;
    }
    
    @Test(expected = IllegalStateException.class)
    @Depends("testDone")
    public void testDecrementFailsWhenDone(Indent n) {
        n.dec();
    }
    
}

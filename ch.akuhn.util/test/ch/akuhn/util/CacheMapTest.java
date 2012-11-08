
package ch.akuhn.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Map;

import org.junit.Test;

public class CacheMapTest {

    public static class Wrapper {
        public Object delegate;

        public Wrapper(Object delegate) {
            this.delegate = delegate;
        }
    }

    @Test
    public void example() {
        Map<Integer,String> map = new CacheMap<Integer,String>() {
            public String initialize(Integer key) {
                return key.toString();
            }
        };
        assertEquals(map.get(1), "1");
        assertEquals(map.get(2), "2");
        assertEquals(map.get(3), "3");
        assertEquals(map.get(2), "2");
    }

    @Test
    public void instancesArgument() {
        Map<String,Wrapper> map = CacheMap.instances(Wrapper.class);
        Wrapper a = map.get("a");
        Wrapper b = map.get("b");
        Wrapper a2 = map.get("a");
        Wrapper c = map.get("c");
        assertSame(a, a2);
        assertNotSame(a, b);
        assertNotSame(b, c);
        assertNotSame(c, a);
        assertEquals("a", a.delegate);
        assertEquals("b", b.delegate);
        assertEquals("c", c.delegate);
    }

    @Test
    public void instancesSimple() {
        Map<String,Object> map = CacheMap.instances(Object.class);
        Object a = map.get("a");
        Object b = map.get("b");
        Object a2 = map.get("a");
        Object c = map.get("c");
        assertSame(a, a2);
        assertNotSame(a, b);
        assertNotSame(b, c);
        assertNotSame(c, a);
    }

}

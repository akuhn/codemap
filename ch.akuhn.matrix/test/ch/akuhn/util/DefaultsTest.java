package ch.akuhn.util;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

public class DefaultsTest {

    @Test(expected = InvocationTargetException.class)
    public void cannotInstantiate() throws Exception {
        Constructor<Defaults> init = Defaults.class.getDeclaredConstructor();
        init.setAccessible(true);
        init.newInstance();
    }

    @Test
    public void testInteger() {
        Integer n = 23;
        assertEquals(23, Defaults.to(n, 42));
    }

    @Test
    public void testNull() {
        String answer = null;
        assertEquals("A", Defaults.to(answer, "A"));
    }

    @Test
    public void testNullInteger() {
        Integer n = null;
        assertEquals(42, Defaults.to(n, 42));
    }

    @Test
    public void testValue() {
        String answer = "B";
        assertEquals("B", Defaults.to(answer, "A"));
    }

}

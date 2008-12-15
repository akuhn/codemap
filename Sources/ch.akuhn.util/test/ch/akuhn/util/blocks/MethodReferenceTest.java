package ch.akuhn.util.blocks;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import jexample.Depends;
import jexample.JExample;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.akuhn.util.blocks.MethodReference;

@RunWith(JExample.class)
public class MethodReferenceTest {

    @Test
    public MethodReference ambigousToString() {
        MethodReference m = new MethodReference("toString");
        assertEquals("#toString", m.toString());
        return m;
    }

    @Test
    public MethodReference integerToString() {
        MethodReference m = new MethodReference("toString(int,int)");
        assertEquals("#toString(int,int)", m.toString());
        return m;
    }

    @Test
    public MethodReference objectToString() {
        MethodReference m = new MethodReference("Object#toString");
        assertEquals("Object#toString", m.toString());
        return m;
    }

    @Test
    @Depends("ambigousToString")
    public void resolveAmbigous(MethodReference ref) {
        Method m = ref.resolve(Object.class);
        assertEquals(Object.class, m.getDeclaringClass());
        assertEquals("toString", m.getName());
        assertEquals(0, m.getParameterTypes().length);
    }

    @Test
    @Depends("ambigousToString")
    public void resolveAmbigous2(MethodReference ref) {
        Method m = ref.resolve(MethodReference.class);
        assertEquals(MethodReference.class, m.getDeclaringClass());
        assertEquals("toString", m.getName());
        assertEquals(0, m.getParameterTypes().length);
    }

    @Depends("ambigousToString")
    @Test(expected = NoSuchMethodException.class)
    public void resolveAmbigous3(MethodReference ref) {
        ref.resolve(Integer.class);
    }

    @Test
    @Depends("theToString")
    public void resolveArityGiven(MethodReference ref) {
        Method m = ref.resolve(Object.class);
        assertEquals(Object.class, m.getDeclaringClass());
        assertEquals("toString", m.getName());
        assertEquals(0, m.getParameterTypes().length);
    }

    @Test
    @Depends("theToString")
    public void resolveArityGiven2(MethodReference ref) {
        Method m = ref.resolve(MethodReference.class);
        assertEquals(MethodReference.class, m.getDeclaringClass());
        assertEquals("toString", m.getName());
        assertEquals(0, m.getParameterTypes().length);
    }

    @Test
    @Depends("theToString")
    public void resolveArityGiven3(MethodReference ref) {
        Method m = ref.resolve(Integer.class);
        assertEquals(Integer.class, m.getDeclaringClass());
        assertEquals("toString", m.getName());
        assertEquals(0, m.getParameterTypes().length);
    }

    @Test
    public MethodReference theToString() {
        MethodReference m = new MethodReference("toString()");
        assertEquals("#toString()", m.toString());
        return m;
    }

}

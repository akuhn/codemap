package ch.akuhn.util;

import static ch.akuhn.util.ListTest.Elem.a;
import static ch.akuhn.util.ListTest.Elem.b;
import static ch.akuhn.util.ListTest.Elem.c;
import static ch.akuhn.util.ListTest.Elem.d;
import static ch.akuhn.util.ListTest.Elem.e;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.unibe.jexample.Given;
import ch.unibe.jexample.JExample;

@RunWith(JExample.class)
@SuppressWarnings("unchecked")
public class ListTest {

    public enum Elem {
        a, b, c, d, e
    };

    @Given("nil")
    @Test(expected = UnsupportedOperationException.class)
    public void cannotSetTailOfEmpty(List nil) {
        nil.setTail(List.nil());
    }

    @Test
    public List fill0Elem() {
        Object a = new Object();
        List nil = List.fill(0, a);
        assertEquals(0, nil.size());
        assertEquals(true, nil.isEmpty());
        assertEquals(false, nil.iterator().hasNext());
        return nil;
    }

    @Test
    public List fill1Elem() {
        Object a = new Object();
        List $ = List.fill(1, a);
        assertEquals(1, $.size());
        assertEquals(false, $.isEmpty());
        Iterator it = $.iterator();
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(false, it.hasNext());
        return $;
    }

    @Test
    public List fill5Elem() {
        List $ = List.fill(5, a);
        assertEquals(5, $.size());
        assertEquals(false, $.isEmpty());
        Iterator it = $.iterator();
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(false, it.hasNext());
        return $;
    }

    @Test
    public List from0ElemArray() {
        List nil = List.from(new Object[] {});
        assertEquals(0, nil.size());
        assertEquals(true, nil.isEmpty());
        assertEquals(false, nil.iterator().hasNext());
        return nil;
    }

    @Test
    public List from1ElemArray() {
        List $ = List.from(new Object[] { a });
        assertEquals(1, $.size());
        assertEquals(false, $.isEmpty());
        Iterator it = $.iterator();
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(false, it.hasNext());
        return $;
    }

    @Test
    public List from5ElemArray() {
        List $ = List.from(new Object[] { a, b, c, d, e });
        assertEquals(5, $.size());
        assertEquals(false, $.isEmpty());
        Iterator it = $.iterator();
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(true, it.hasNext());
        assertSame(b, it.next());
        assertEquals(true, it.hasNext());
        assertSame(c, it.next());
        assertEquals(true, it.hasNext());
        assertSame(d, it.next());
        assertEquals(true, it.hasNext());
        assertSame(e, it.next());
        assertEquals(false, it.hasNext());
        return $;
    }

    @Test
    @Given("new5tuple")
    public void get(List $) {
        assertSame(a, $.get(0));
        assertSame(b, $.get(1));
        assertSame(c, $.get(2));
        assertSame(d, $.get(3));
        assertSame(e, $.get(4));
    }

    @Given("new5tuple")
    @Test(expected = IndexOutOfBoundsException.class)
    public void getMinusOneIsFail(List $) {
        assertSame(a, $.get(-1));
    }

    @Given("new5tuple")
    @Test(expected = IndexOutOfBoundsException.class)
    public void getSizeIsFail(List $) {
        assertSame(a, $.get(5));
    }

    @Test
    public List new1tuple() {
        List $ = List.of(a);
        assertEquals(1, $.size());
        assertEquals(false, $.isEmpty());
        Iterator it = $.iterator();
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(false, it.hasNext());
        return $;
    }

    @Test
    public List new2tuple() {
        List $ = List.of(a, b);
        assertEquals(2, $.size());
        assertEquals(false, $.isEmpty());
        Iterator it = $.iterator();
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(true, it.hasNext());
        assertSame(b, it.next());
        assertEquals(false, it.hasNext());
        return $;
    }

    @Test
    public List new3tuple() {
        List $ = List.of(a, b, c);
        assertEquals(3, $.size());
        assertEquals(false, $.isEmpty());
        Iterator it = $.iterator();
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(true, it.hasNext());
        assertSame(b, it.next());
        assertEquals(true, it.hasNext());
        assertSame(c, it.next());
        assertEquals(false, it.hasNext());
        return $;
    }

    @Test
    public List new4tuple() {
        List $ = List.of(a, b, c, d);
        assertEquals(4, $.size());
        assertEquals(false, $.isEmpty());
        Iterator it = $.iterator();
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(true, it.hasNext());
        assertSame(b, it.next());
        assertEquals(true, it.hasNext());
        assertSame(c, it.next());
        assertEquals(true, it.hasNext());
        assertSame(d, it.next());
        assertEquals(false, it.hasNext());
        return $;
    }

    @Test
    public List new5tuple() {
        List $ = List.of(a, b, c, d, e);
        assertEquals(5, $.size());
        assertEquals(false, $.isEmpty());
        Iterator it = $.iterator();
        assertEquals(true, it.hasNext());
        assertSame(a, it.next());
        assertEquals(true, it.hasNext());
        assertSame(b, it.next());
        assertEquals(true, it.hasNext());
        assertSame(c, it.next());
        assertEquals(true, it.hasNext());
        assertSame(d, it.next());
        assertEquals(true, it.hasNext());
        assertSame(e, it.next());
        assertEquals(false, it.hasNext());
        return $;
    }

    @Test
    public List nil() {
        List nil = List.nil();
        assertEquals(0, nil.size());
        assertEquals(true, nil.isEmpty());
        assertEquals(false, nil.iterator().hasNext());
        return nil;
    }

    @Test
    @Given("new3tuple")
    public void testAppend(List $3) {
        List $ = $3.append(e);
        assertNotSame($3, $);
        assertEquals(4, $.size());
        assertEquals(false, $.isEmpty());
        assertEquals(a, $.get(0));
        assertEquals(b, $.get(1));
        assertEquals(c, $.get(2));
        assertEquals(e, $.get(3));
    }

    @Test
    @Given("new3tuple")
    public void testAppendList(List $3) {
        List $2 = List.of(d, e);
        List $ = $3.appendList($2);
        assertNotSame($2, $);
        assertNotSame($3, $);
        assertEquals(5, $.size());
        assertEquals(false, $.isEmpty());
        assertEquals(a, $.get(0));
        assertEquals(b, $.get(1));
        assertEquals(c, $.get(2));
        assertEquals(d, $.get(3));
        assertEquals(e, $.get(4));
    }

    @Test
    @Given("new3tuple")
    public void testPrepend(List $3) {
        List $ = $3.prepend(e);
        assertNotSame($3, $);
        assertEquals(4, $.size());
        assertEquals(false, $.isEmpty());
        assertEquals(e, $.get(0));
        assertEquals(a, $.get(1));
        assertEquals(b, $.get(2));
        assertEquals(c, $.get(3));
    }

    @Test
    @Given("new3tuple")
    public void testPrependList(List $3) {
        List $2 = List.of(d, e);
        List $ = $3.prependList($2);
        assertNotSame($2, $);
        assertNotSame($3, $);
        assertEquals(5, $.size());
        assertEquals(false, $.isEmpty());
        assertEquals(d, $.get(0));
        assertEquals(e, $.get(1));
        assertEquals(a, $.get(2));
        assertEquals(b, $.get(3));
        assertEquals(c, $.get(4));
    }

    @Test
    @Given("new5tuple")
    public void testSublist(List $5) {
        java.util.List sub = $5.subList(1, 4);
        assertEquals(3, sub.size());
        assertEquals(b, sub.get(0));
        assertEquals(c, sub.get(1));
        assertEquals(d, sub.get(2));
    }

    @Test
    @Given("new5tuple")
    public void testSublistEmpty(List $5) {
        java.util.List sub = $5.subList(2, 2);
        assertEquals(0, sub.size());
    }

    @Given("new5tuple")
    @Test(expected = IllegalArgumentException.class)
    public void testSublistNegative(List $5) {
        @SuppressWarnings("unused")
        java.util.List sub = $5.subList(4, 1);
    }

    @Test
    @Given("new3tuple")
    public void testToArray(List $3) {
        Object[] $ = new Object[3];
        Object[] prev = $;
        $ = $3.toArray($);
        assertSame($, prev);
        assertEquals(3, $.length);
        assertEquals(a, $[0]);
        assertEquals(b, $[1]);
        assertEquals(c, $[2]);
    }

    @Test
    @Given("new3tuple")
    public void testToLongArrayIsLong(List $3) {
        Object[] $ = new Object[4];
        Object[] prev = $;
        $ = $3.toArray($);
        assertSame($, prev); // !!!
        assertEquals(4, $.length);
        assertEquals(a, $[0]);
        assertEquals(b, $[1]);
        assertEquals(c, $[2]);
        assertEquals(null, $[3]);
    }

    @Test
    @Given("new3tuple")
    public void testToShortArray(List $3) {
        Object[] $ = new Object[2];
        Object[] prev = $;
        $ = $3.toArray($);
        assertNotSame($, prev); // !!!
        assertEquals(3, $.length);
        assertEquals(a, $[0]);
        assertEquals(b, $[1]);
        assertEquals(c, $[2]);
    }

}

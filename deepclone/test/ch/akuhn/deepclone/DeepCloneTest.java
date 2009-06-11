package ch.akuhn.deepclone;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

public class DeepCloneTest {

    @Test
    public void cloneString() {
	String original = "ABCDEFG";
	String clone = new CloneFactory().deepClone(original);
	assertEquals(original, clone);
    }

    @Test
    public void cloneFile() {
	File original = new File("/home/user/folder/file.ext");
	File clone = new CloneFactory().deepClone(original);
	assertEquals(original, clone);
    }
    
    @Test
    public void cloneObject() {
	Object original = new Object();
	Object clone = new CloneFactory().deepClone(original);
	assertNotSame(original, clone);
    }
    
    @Test
    public void cloneArrayList() {
	ArrayList<Dummy> original = new ArrayList<Dummy>();
	original.add(new Dummy(1));
	original.add(new Dummy(2));
	original.add(new Dummy(3));
	ArrayList<Dummy> clone = new CloneFactory().deepClone(original);
	assertNotSame(original, clone);
	assertEquals(original, clone);
	assertEquals(original.size(), clone.size());
	assertEquals(original.get(0), clone.get(0));
	assertEquals(original.get(0), clone.get(0));
	assertEquals(original.get(0), clone.get(0));
	assertNotSame(original, clone);
	assertNotSame(original.get(0), clone.get(0));
	assertNotSame(original.get(0), clone.get(0));
	assertNotSame(original.get(0), clone.get(0));
    }
    
    @Test
    public void cloneArray() {
	Dummy[] original = new Dummy[] { new Dummy(1), new Dummy(2), new Dummy(3) };
	Dummy[] clone = new CloneFactory().deepClone(original);
	assertArrayEquals(original, clone);
	assertEquals(original.length, clone.length);
	assertEquals(original[0], clone[0]);
	assertEquals(original[1], clone[1]);
	assertEquals(original[2], clone[2]);
	assertNotSame(original, clone);
	assertNotSame(original[0], clone[0]);
	assertNotSame(original[1], clone[1]);
	assertNotSame(original[2], clone[2]);
    }
    
    static class Dummy {
	final int num;
	public Dummy(int num) {
	    this.num = num;
	}
	@Override
        public boolean equals(Object object) {
	    return object instanceof Dummy && ((Dummy) object).num == num;
	}
	@Override
        public int hashCode() {
	    return num;
	}
    }
    
}

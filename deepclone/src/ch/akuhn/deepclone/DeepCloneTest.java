package ch.akuhn.deepclone;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

public class DeepCloneTest {

    @Test
    public void cloneString() {
	String original = "ABCDEFG";
	String clone = new DeepClone().deepClone(original);
	assertEquals(original, clone);
    }

    @Test
    public void cloneFile() {
	File original = new File("/home/user/folder/file.ext");
	File clone = new DeepClone().deepClone(original);
	assertEquals(original, clone);
    }
    
    @Test
    public void cloneObject() {
	Object original = new Object();
	Object clone = new DeepClone().deepClone(original);
	assertNotSame(original, clone);
    }
    
    @Test
    public void cloneArrayList() {
	ArrayList<Object> original = new ArrayList<Object>();
	original.add(new Object());
	original.add(new Object());
	original.add(new Object());
	ArrayList<Object> clone = new DeepClone().deepClone(original);
	assertNotSame(original, clone);
    }
    
    @Test
    public void cloneArray() {
	Dummy[] original = new Dummy[] { new Dummy(1), new Dummy(2), new Dummy(3) };
	Dummy[] clone = new DeepClone().deepClone(original);
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

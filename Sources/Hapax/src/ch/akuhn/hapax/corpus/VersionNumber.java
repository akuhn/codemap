package ch.akuhn.hapax.corpus;

import static ch.akuhn.util.Strings.chars;
import static java.lang.Character.isDigit;
import static java.lang.Integer.valueOf;
import static java.lang.Math.min;

import java.util.Collection;
import java.util.LinkedList;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;

@FamePackage("Hapax")
@FameDescription("Document")
public class VersionNumber implements Comparable<VersionNumber> {

    public int numbers[];
    @FameProperty
    private String name;
    
    @SuppressWarnings("unused")
    private VersionNumber() {
        // for Fame
    }
    
    public VersionNumber(String name) {
        this.name = name;
        this.numbers = parse(name);
    }

    private int[] parse(String string) {
        Collection<Integer> numbers = new LinkedList<Integer>();
        StringBuilder buf = new StringBuilder();
        for (char each: chars(string)) {
            if (isDigit(each)) buf.append(each);
            else if (buf.length() > 0) {
                numbers.add(valueOf(buf.toString()));
                buf.setLength(0);
            }
        }
        if (buf.length() > 0) numbers.add(valueOf(buf.toString()));
        int[] array = new int[numbers.size()];
        int index = 0;
        for (int each: numbers) array[index++] = each;
        return array;
    }

    //@Override
    public int compareTo(VersionNumber other) {
        int len = min(this.numbers.length, other.numbers.length);
        for (int index = 0; index < len; index++) {
            int diff = this.numbers[index] - other.numbers[index];
            if (diff != 0) return diff;
        }
        return this.numbers.length - other.numbers.length;
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof VersionNumber &&
                compareTo((VersionNumber) other) == 0;
    }

    public String name() {
        return name;
    }
    
    
    
}

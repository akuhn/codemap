package ch.akuhn.hapax.index;

import java.util.ArrayList;
import java.util.Collections;

class Rank<T> implements Comparable<Rank<T>> {

    public final T element;
    public final double rank;

    public Rank(T element, double rank) {
        this.element = element;
        this.rank = rank;
    }

    //@Override
    public int compareTo(Rank<T> rank) {
        return (int) Math.signum(rank.rank - this.rank);
    }

    @Override
    public String toString() {
        return String.format("%s %.0f%%", element, rank * 100);
    }

}

@SuppressWarnings("serial")
public class Ranking<T> extends ArrayList<Rank<T>> {

    public void add(T element, double rank) {
        this.add(new Rank<T>(element, rank));
    }

    public Ranking<T> sort() {
        Collections.sort(this);
        return this;
    }
    
    public Ranking<T> top(int ten) {
    	Ranking<T> result = new Ranking<T>();
    	int end = Math.max(this.size(), ten);
    	for (Rank<T> each: this.subList(0, end)) result.add(each);
    	return result;
    }

}

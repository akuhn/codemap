package ch.akuhn.hapax.lsi;

import java.util.ArrayList;
import java.util.Collections;

import ch.akuhn.hapax.lsi.Ranking.Rank;

@SuppressWarnings("serial")
public class Ranking<T> extends ArrayList<Rank<T>> {

    public static final class Rank<T> implements Comparable<Rank<T>>{
        
        public final T element;
        public final double rank;

        public Rank(T element, double rank) {
            this.element = element;
            this.rank = rank;
        }

        @Override
        public int compareTo(Rank<T> rank) {
            return (int) Math.signum(rank.rank - this.rank);
        }

        @Override
        public String toString() {
            return String.format("%s %.0f%%", element, rank * 100);
        }
        
    }
    
    public void add(T element, double rank) {
        this.add(new Rank<T>(element, rank));
    }
    
    public Ranking<T> sort() {
        Collections.sort(this);
        return this;
    }
    
}

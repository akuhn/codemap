package example;

import java.util.Iterator;
import java.util.Random;

import ch.akuhn.util.query.Times;

public class EachFinalEachOnceBench {

    public static final class Mutable {
        public Mutable(double d, int i) {
            this.index = index;
            this.value = value;
        }
        public Mutable() {
            // TODO Auto-generated constructor stub
        }
        public int index;
        public double value;
    }
    
    public static final class Immutable {
        public final int index;
        public final double value;
        public Immutable(double value, int index) {
            this.index = index;
            this.value = value;
        }
    }
    
    public static final double[] data = new double[10000000];
    static {
        Random rand = new Random(20080615l);
        for (int n = 0; n < data.length; n++) data[n] = rand.nextDouble() * 20;
    }
    
    public static Iterable<Immutable> immutable(double[] array) {
        return new ImmutableIter(array);
    }
    
    public static Iterable<Mutable> mutable(double[] array) {
        return new MutableIter(array);
    }

    public static Iterable<Mutable> mutable2(double[] array) {
        return new MutableIter2(array);
    }    
    
    static class MutableIter2 implements Iterable<Mutable>, Iterator<Mutable> {

        private double[] array;
        private int index;
        
        public MutableIter2(double[] array) {
            this.array = array;
            this.index = 0;
        }
        
        @Override
        public Iterator<Mutable> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < array.length;
        }

        @Override
        public Mutable next() {
            return new Mutable(array[index], index++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }    
    static class ImmutableIter implements Iterable<Immutable>, Iterator<Immutable> {

        private double[] array;
        private int index;
        
        public ImmutableIter(double[] array) {
            this.array = array;
            this.index = 0;
        }
        
        @Override
        public Iterator<Immutable> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < array.length;
        }

        @Override
        public Immutable next() {
            return new Immutable(array[index], index++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }

    static class MutableIter implements Iterable<Mutable>, Iterator<Mutable> {

        private Mutable each;
        private double[] array;
        private int index;
        
        public MutableIter(double[] array) {
            this.each = new Mutable();
            this.array = array;
            this.index = 0;
        }
        
        @Override
        public Iterator<Mutable> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return index < array.length;
        }

        @Override
        public Mutable next() {
            each.value = array[index];
            each.index = index++;
            return each;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    public static void main(String[] args) throws InterruptedException {
        
        double total = 0;
        
        for (Void _: Times.repeat(20)) {
            
            long tiem;
            double sum;
            
            tiem = System.currentTimeMillis();
            sum = 0;
            
            switch(3) {
                case 1:
                    for (Mutable each: mutable(data)) sum += each.value;
                    break;
                case 2:
                    for (Immutable each: immutable(data)) sum += each.value;
                    break;
                case 3:
                    for (Mutable each: mutable2(data)) sum += each.value;
                    break;
            }
            System.out.print(total += (System.currentTimeMillis() - tiem));
            System.out.print("\t" + sum + "\t");
            
            System.out.println();
            
        }
        
        System.out.println(total);
        
    }
    
}

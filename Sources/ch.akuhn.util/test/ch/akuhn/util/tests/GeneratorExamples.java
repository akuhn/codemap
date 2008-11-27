package ch.akuhn.util.tests;

import static ch.akuhn.util.Extensions.puts;

import java.awt.Point;

import ch.akuhn.util.Generator;
import ch.akuhn.util.List;


public class GeneratorExamples {

	public static void main(String[] args) {
		
		Generator<Integer> fibonacci = new Generator<Integer>() {
		    @Override
		    public void run() {
		        int a = 0, b = 1;
		        while (true) {
		            a = b + (b = a);
		            yield(a); 
		        }
		    }
		};

		for (int x : fibonacci) {
		    if (x > 20000) break;
		    System.out.println(x);
		}
	    System.out.println("done\n");

		Generator<Character> hello = new Generator<Character>() {
		    @Override
		    public void run() {
		        String str = "Hello, Worlds!";
		        for (int n = 0; n < str.length(); n++) {
		            yield(str.charAt(n));
		        }
		    }
		};

		for (char each : hello) {
		    System.out.println(each);
		}
	    System.out.println("done\n");

	    Character[] array = { 'a', 'b', 'c', 'd', 'e', 'f' };
	    int tally = 0;
	    for (Character[] each : permute(array)) {
	    	puts(each);
	    	tally++;
	    }
	    puts(tally);
	    
	}
	
	public static <T> Generator<T[]> permute(final T[] a) {
		return new Generator<T[]>() {
			@Override
			public void run() {
				permute(a.length - 1);
			}
			private void permute(int n) {
				if (n == 0) 
					yield(a.clone());
				else
					for (int k = n; k >= 0; k--) {
						swap(n,k);
						permute(n - 1);
						swap(n,k);
					}
			}
			private void swap(int n, int m) {
				T temp = a[n];
				a[n] = a[m];
				a[m] = temp;
			}
		};
	}
	
}

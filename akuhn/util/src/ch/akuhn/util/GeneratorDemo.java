package ch.akuhn.util;

import static java.lang.System.out;

public class GeneratorDemo {

	public static void main(String[] args) throws InterruptedException {
		
		Generator<Integer> fib = new Generator<Integer>() {
			@Override
			public void run() {
				int a = 0;
				int b = 1;
				while (true) {
					a = b + (b = a);
					yield(a);
				}
			}
		};
		
		for (int x : fib) {
			if (x > 20000) break;
			out.println(x);
		}
	
	}
	
}



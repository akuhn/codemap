package ch.akuhn.util;

import static java.lang.System.out;

public class GeneratorDemo {

	public static void main(String[] args) throws InterruptedException {
		
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
		      out.println(x);
		  }
	
	}
	
}



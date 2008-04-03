package ch.akuhn.sandbox;

import static java.lang.System.out;
import ch.akuhn.util.Generator;
import java.util.*;

/** Compares iterator to generator, produces
 * <pre>
 *561
 *35795
 *</pre> 
 * 
 * @author akuhn
 *
 */
public class GeneratorBenchmark {

	public static void main(String[] args) {
		
		final List<String> list = new ArrayList<String>();
		for (int n = 0; n < 10000000; n++) {
			list.add("Hello world!");
		}
		
		long start = System.currentTimeMillis();
		for (int n = 0; n < 1; n++) {
			for (String each : list) {
				each.isEmpty();
			}
		}
		long time = System.currentTimeMillis() - start;
		System.out.println(time);
		
		Generator<String> generator;
		
		
		start = System.currentTimeMillis();
		for (int n = 0; n < 1; n++) {
			generator = new Generator<String>() {
			      @Override
			      public void run() {
			          for (int n = 0; n < list.size(); n++) {
			                yield(list.get(n));
			          }
			      }
			 };
			for (String each : generator) {
				each.isEmpty();
			}
		}
		time = System.currentTimeMillis() - start;
		System.out.println(time);
		  
	}
	
}

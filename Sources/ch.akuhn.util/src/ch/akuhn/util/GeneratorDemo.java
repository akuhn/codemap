//  Copyright (c) 1998-2008 Adrian Kuhn <akuhn(a)students.unibe.ch>
//  
//  This file is part of ch.akuhn.util.
//  
//  ch.akuhn.util is free software: you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation, either version 3 of the License, or (at your
//  option) any later version.
//  
//  ch.akuhn.util is distributed in the hope that it will be useful, but
//  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
//  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
//  License for more details.
//  
//  You should have received a copy of the GNU Lesser General Public License
//  along with ch.akuhn.util. If not, see <http://www.gnu.org/licenses/>.
//  

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



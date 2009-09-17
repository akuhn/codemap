package edu.stanford.hci.flowmap.utils;

import java.io.File;
import java.io.IOException;


/**
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class SystemUtilities {
	
	public static void exit(String message) {
		System.out.println(message);
		System.exit(0);
	}
	
	
	
	// for timing purposes
	private static long previousTime = 0L;
	public static void tic() {
		SystemUtilities.previousTime = System.currentTimeMillis();
	}
	public static void toc() {
		System.out.println("Clock: " + (System.currentTimeMillis() - SystemUtilities.previousTime) + " ms.");
	}
	

	
	// prints the local directory
	public static void printCurrentDirectory() {
		File f = new File(".");
		try {
			System.out.println(f.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
	}
}

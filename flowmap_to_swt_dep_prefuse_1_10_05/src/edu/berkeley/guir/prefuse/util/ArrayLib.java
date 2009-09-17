package edu.berkeley.guir.prefuse.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * A number of supplementary array routines not 
 *  supported by the java.util.Arrays class.
 *
 * @author Jeffrey Heer
 * @version 1.0
 */
public abstract class ArrayLib {

	//// -----------------------------------------------
	//// -- Searching Functions ------------------------

	public static final int binarySearch(int[] a, int key) {
		int x1 = 0;
		int x2 = a.length;
		int i = x2 / 2;
		while (x1 < x2) {
			if (a[i] == key) {
				return i;
			} else if (a[i] < key) {
				x1 = i + 1;
			} else {
				x2 = i;
			}
			i = x1 + (x2 - x1) / 2;
		}
		return -1*(i+1);
	} //

	public static final int binarySearch(int[] a, int key, int length) {
		int x1 = 0;
		int x2 = length;
		int i = x2 / 2;

		while (x1 < x2) {
			if (a[i] == key) {
				return i;
			} else if (a[i] < key) {
				x1 = i + 1;
			} else {
				x2 = i;
			}
			i = x1 + (x2 - x1) / 2;
		}
		return -1*(i+1);
	} //

	public static final int binarySearch(int[] a, int key, int begin, int end) {
		int x1 = begin;
		int x2 = end;
		int i = x1 + (x2 - x1) / 2;

		while (x1 < x2) {
			if (a[i] == key) {
				return i;
			} else if (a[i] < key) {
				x1 = i + 1;
			} else {
				x2 = i;
			}
			i = x1 + (x2 - x1) / 2;
		}

		return -1*(i+1);
	} //


	public static final int binarySearch(Object[] a, Object key) {
		int x1 = 0;
		int x2 = a.length;
		int i = x2 / 2, c;
		while (x1 < x2) {
			c = ((Comparable)a[i]).compareTo(key);
			if (c == 0) {
				return i;
			} else if (c < 0) {
				x1 = i + 1;
			} else {
				x2 = i;
			}
			i = x1 + (x2 - x1) / 2;
		}
		return -1*(i+1);
	} //

	public static final int binarySearch(Object[] a, Object key, int length) {
		int x1 = 0;
		int x2 = length;
		int i = x2 / 2, c;

		while (x1 < x2) {
			c = ((Comparable)a[i]).compareTo(key);
			if (c == 0) {
				return i;
			} else if (c < 0) {
				x1 = i + 1;
			} else {
				x2 = i;
			}
			i = x1 + (x2 - x1) / 2;
		}
		return -1*(i+1);
	} //

	public static final int binarySearch(Object[] a, Object key, int begin, int end) {
		int x1 = begin;
		int x2 = end;
		int i = x1 + (x2 - x1) / 2, c;

		while (x1 < x2) {
			c = ((Comparable)a[i]).compareTo(key);
			if (c == 0) {
				return i;
			} else if (c < 0) {
				x1 = i + 1;
			} else {
				x2 = i;
			}
			i = x1 + (x2 - x1) / 2;
		}

		return -1*(i+1);
	} //

	public static final int binarySearch(Object[] a, Object key, Comparator cp) {
		int x1 = 0;
		int x2 = a.length;
		int i = x2 / 2, c;
		while (x1 < x2) {
			c = cp.compare(a[i], key);
			if (c == 0) {
				return i;
			} else if (c < 0) {
				x1 = i + 1;
			} else {
				x2 = i;
			}
			i = x1 + (x2 - x1) / 2;
		}
		return -1*(i+1);
	} //

	public static final int binarySearch(Object[] a, Object key, Comparator cp, int length) {
		int x1 = 0;
		int x2 = length;
		int i = x2 / 2, c;

		while (x1 < x2) {
			c = cp.compare(a[i], key);
			if (c == 0) {
				return i;
			} else if (c < 0) {
				x1 = i + 1;
			} else {
				x2 = i;
			}
			i = x1 + (x2 - x1) / 2;
		}
		return -1*(i+1);
	} //

	public static final int binarySearch(Object[] a, Object key, Comparator cp, int begin, int end) {
		int x1 = begin;
		int x2 = end;
		int i = x1 + (x2 - x1) / 2, c;

		while (x1 < x2) {
			c = cp.compare(a[i], key);
			if (c == 0) {
				return i;
			} else if (c < 0) {
				x1 = i + 1;
			} else {
				x2 = i;
			}
			i = x1 + (x2 - x1) / 2;
		}

		return -1*(i+1);
	} //

	//// -----------------------------------------------
	//// -- Finding Functions --------------------------

	public static final int find(int[] a, int key) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] == key) {
				return i;
			}
		}
		return -1;
	} //

	public static final int find(int[] a, int key, int length) {
		for (int i = 0; i < length; i++) {
			if (a[i] == key) {
				return i;
			}
		}
		return -1;
	} //

	public static final int find(int[] a, int key, int begin, int end) {
		for (int i = begin; i < end; i++) {
			if (a[i] == key) {
				return i;
			}
		}
		return -1;
	} //

	//// -----------------------------------------------
	//// -- Resizing Functions -------------------------

	public static final int[] resize(int[] a, int size) {
		//assert (size > a.length);
		int[] b = new int[size];
		System.arraycopy(a, 0, b, 0, a.length);
		return b;
	} //
	
	public static final float[] resize(float[] a, int size) {
		//assert (size > a.length);
		float[] b = new float[size];
		System.arraycopy(a, 0, b, 0, a.length);
		return b;
	} //
	
	public static final double[] resize(double[] a, int size) {
		//assert (size > a.length);
		double[] b = new double[size];
		System.arraycopy(a, 0, b, 0, a.length);
		return b;
	} //
	
	public static final Object[] resize(Object[] a, int size) {
		//assert (size > a.length);
		Object[] b = new Object[size];
		System.arraycopy(a, 0, b, 0, a.length);
		return b;
	} //

	public static final int[] trim(int[] a, int size) {
		//assert (size <= a.length);
		if ( a.length == size ) {
			return a;
		} else {
			int[] b = new int[size];
			System.arraycopy(a, 0, b, 0, size);
			return b;
		}
	} //

	public static final float[] trim(float[] a, int size) {
		//assert (size <= a.length);
		if ( a.length == size ) {
			return a;
		} else {
			float[] b = new float[size];
			System.arraycopy(a, 0, b, 0, size);
			return b;
		}
	} //
	
	public static final double[] trim(double[] a, int size) {
		//assert (size <= a.length);
		if ( a.length == size ) {
			return a;
		} else {
			double[] b = new double[size];
			System.arraycopy(a, 0, b, 0, size);
			return b;
		}
	} //
	//// -----------------------------------------------
	//// -- Sorting Functions --------------------------

	// -- int / double sorting ------------------------------------------

	public static final void sort(int[] a, double[] b) {
		mergesort(a, b, 0, a.length - 1);
	} //

	public static final void sort(int[] a, double[] b, int length) {
		mergesort(a, b, 0, length - 1);
	} //

	public static final void sort(int[] a, double[] b, int begin, int end) {
		mergesort(a, b, begin, end - 1);
	} //

	// -- Insertion Sort --

	public static final void insertionsort(int[] a, double[] b, int p, int r) {
		for (int j = p + 1; j <= r; j++) {
			int key = a[j];
			double val = b[j];
			int i = j - 1;
			while (i >= p && a[i] > key) {
				a[i + 1] = a[i];
				b[i + 1] = b[i];
				i--;
			}
			a[i + 1] = key;
			b[i + 1] = val;
		}
	} //

	// -- Mergesort --

	public static final int MERGE_THRESHOLD = 1000;

	public static final void mergesort(int[] a, double[] b, int p, int r) {
		if (p >= r) {
			return;
		}
		if (r - p + 1 < MERGE_THRESHOLD) {
			insertionsort(a, b, p, r);
		} else {
			int q = (p + r) / 2;
			mergesort(a, b, p, q);
			mergesort(a, b, q + 1, r);
			merge(a, b, p, q, r);
		}
	} //

	public static final void merge(int[] a, double[] b, int p, int q, int r) {
		int[] t = new int[r - p + 1];
		double[] v = new double[r - p + 1];
		int i, p1 = p, p2 = q + 1;
		for (i = 0; p1 <= q && p2 <= r; i++) {
			if (a[p1] < a[p2]) {
				v[i] = b[p1];
				t[i] = a[p1++];
			} else {
				v[i] = b[p2];
				t[i] = a[p2++];
			}
		}
		for (; p1 <= q; p1++, i++) {
			v[i] = b[p1];
			t[i] = a[p1];
		}
		for (; p2 <= r; p2++, i++) {
			v[i] = b[p2];
			t[i] = a[p2];
		}
		for (i = 0, p1 = p; i < t.length; i++, p1++) {
			b[p1] = v[i];
			a[p1] = t[i];
		}
	} //

	// -- Quicksort --

	public static final void quicksort(int[] a, double[] b, int p, int r) {
		if (p >= r) {
			return;
		}
		int q = partition(a, b, p, r);
		quicksort(a, b, p, q);
		quicksort(a, b, q + 1, r);
	} //

	private static final int partition(int[] a, double[] b, int p, int r) {
		int x = a[p];
		int i = p;
		int j = r;

		while (true) {
			while (a[j] > x) {
				j--;
			}
			while (a[i] < x) {
				i++;
			}
			if (i < j) {
				int ti = a[i];
				double td = b[i];
				a[i] = a[j];
				b[i] = b[j];
				a[j] = ti;
				b[j] = td;
			} else {
				return j;
			}
		}
	} //

	// -- int / int sorting ---------------------------------------------

	public static final void sort(int[] a, int[] b) {
		mergesort(a, b, 0, a.length - 1);
	} //

	public static final void sort(int[] a, int[] b, int length) {
		mergesort(a, b, 0, length - 1);
	} //

	public static final void sort(int[] a, int[] b, int begin, int end) {
		mergesort(a, b, begin, end - 1);
	} //

	// -- Insertion Sort --

	public static final void insertionsort(int[] a, int[] b, int p, int r) {
		for (int j = p + 1; j <= r; j++) {
			int key = a[j];
			int val = b[j];
			int i = j - 1;
			while (i >= p && a[i] > key) {
				a[i + 1] = a[i];
				b[i + 1] = b[i];
				i--;
			}
			a[i + 1] = key;
			b[i + 1] = val;
		}
	} //

	// -- Mergesort --

	public static final void mergesort(int[] a, int[] b, int p, int r) {
		if (p >= r) {
			return;
		}
		if (r - p + 1 < MERGE_THRESHOLD) {
			insertionsort(a, b, p, r);
		} else {
			int q = (p + r) / 2;
			mergesort(a, b, p, q);
			mergesort(a, b, q + 1, r);
			merge(a, b, p, q, r);
		}
	} //

	public static final void merge(int[] a, int[] b, int p, int q, int r) {
		int[] t = new int[r - p + 1];
		int[] v = new int[r - p + 1];
		int i, p1 = p, p2 = q + 1;
		for (i = 0; p1 <= q && p2 <= r; i++) {
			if (a[p1] < a[p2]) {
				v[i] = b[p1];
				t[i] = a[p1++];
			} else {
				v[i] = b[p2];
				t[i] = a[p2++];
			}
		}
		for (; p1 <= q; p1++, i++) {
			v[i] = b[p1];
			t[i] = a[p1];
		}
		for (; p2 <= r; p2++, i++) {
			v[i] = b[p2];
			t[i] = a[p2];
		}
		for (i = 0, p1 = p; i < t.length; i++, p1++) {
			b[p1] = v[i];
			a[p1] = t[i];
		}
	} //

	// -- Quicksort --

	public static final void quicksort(int[] a, int[] b, int p, int r) {
		if (p >= r) {
			return;
		}
		int q = partition(a, b, p, r);
		quicksort(a, b, p, q);
		quicksort(a, b, q + 1, r);
	} //

	private static final int partition(int[] a, int[] b, int p, int r) {
		int x = a[p];
		int i = p;
		int j = r;

		while (true) {
			while (a[j] > x) {
				j--;
			}
			while (a[i] < x) {
				i++;
			}
			if (i < j) {
				int ti = a[i];
				int td = b[i];
				a[i] = a[j];
				b[i] = b[j];
				a[j] = ti;
				b[j] = td;
			} else {
				return j;
			}
		}
	} //

	// -- float / int sorting -------------------------------------------

	public static final void sort(float[] a, int[] b) {
		mergesort(a, b, 0, a.length - 1);
	} //

	public static final void sort(float[] a, int[] b, int length) {
		mergesort(a, b, 0, length - 1);
	} //

	public static final void sort(float[] a, int[] b, int begin, int end) {
		mergesort(a, b, begin, end - 1);
	} //

	// -- Insertion Sort --

	public static final void insertionsort(float[] a, int[] b, int p, int r) {
		for (int j = p + 1; j <= r; j++) {
			float key = a[j];
			int val = b[j];
			int i = j - 1;
			while (i >= p && a[i] > key) {
				a[i + 1] = a[i];
				b[i + 1] = b[i];
				i--;
			}
			a[i + 1] = key;
			b[i + 1] = val;
		}
	} //

	// -- Mergesort --

	public static final void mergesort(float[] a, int[] b, int p, int r) {
		if (p >= r) {
			return;
		}
		if (r - p + 1 < MERGE_THRESHOLD) {
			insertionsort(a, b, p, r);
		} else {
			int q = (p + r) / 2;
			mergesort(a, b, p, q);
			mergesort(a, b, q + 1, r);
			merge(a, b, p, q, r);
		}
	} //

	public static final void merge(float[] a, int[] b, int p, int q, int r) {
		float[] t = new float[r - p + 1];
		int[] v = new int[r - p + 1];
		int i, p1 = p, p2 = q + 1;
		for (i = 0; p1 <= q && p2 <= r; i++) {
			if (a[p1] < a[p2]) {
				v[i] = b[p1];
				t[i] = a[p1++];
			} else {
				v[i] = b[p2];
				t[i] = a[p2++];
			}
		}
		for (; p1 <= q; p1++, i++) {
			v[i] = b[p1];
			t[i] = a[p1];
		}
		for (; p2 <= r; p2++, i++) {
			v[i] = b[p2];
			t[i] = a[p2];
		}
		for (i = 0, p1 = p; i < t.length; i++, p1++) {
			b[p1] = v[i];
			a[p1] = t[i];
		}
	} //

	// -- Quicksort --

	public static final void quicksort(float[] a, int[] b, int p, int r) {
		if (p >= r) {
			return;
		}
		int q = partition(a, b, p, r);
		quicksort(a, b, p, q);
		quicksort(a, b, q + 1, r);
	} //

	private static final int partition(float[] a, int[] b, int p, int r) {
		float x = a[p];
		int i = p;
		int j = r;

		while (true) {
			while (a[j] > x) {
				j--;
			}
			while (a[i] < x) {
				i++;
			}
			if (i < j) {
				float ti = a[i];
				int td = b[i];
				a[i] = a[j];
				b[i] = b[j];
				a[j] = ti;
				b[j] = td;
			} else {
				return j;
			}
		}
	} //

	// Array File I/O
	
	public static int[] getIntArray(String filename) {
		int[] array = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);
			int maxlen = st.countTokens();
			int len = 0;
			array = new int[maxlen];			
			while ( st.hasMoreTokens() ) {
				String tok = st.nextToken();
				if ( tok.startsWith("#") ) // commented int
					continue;
				array[len++] = Integer.parseInt(tok);
			}
			if ( len != maxlen )
				array = ArrayLib.trim(array, len);
			return array;
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
		}
	} //

} // end of class ArrayLib

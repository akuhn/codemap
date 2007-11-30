package ch.akuhn.util;

import java.util.List;

public class Lists {

	public static <T> T first(List<T> list) {
		return list.get(0);
	}
	
	public static <T> T last(List<T> list) {
		return list.get(list.size() - 1);
	}
	
	public static <A> int atFirst(List<A> list, Predicate<A> block) {
		for (int n = 0; n < list.size(); n++) {
			if (block.is(list.get(n))) return n;
		}
		return -1;
	}
	
	public static <A> int atLast(List<A> list, Predicate<A> block) {
		for (int n = list.size() -1; n >= 0 ; n--) {
			if (block.is(list.get(n))) return n;
		}
		return -1;
	}
	
}

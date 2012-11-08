package ch.akuhn.hapax.index;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import ch.akuhn.hapax.corpus.Terms;

public class LogLikelihood implements Comparable<LogLikelihood> {

	private Terms t1, t2;
	private String term;
	private double logL;
	private boolean p1HigherThenP2;

	public LogLikelihood(Terms t1, Terms t2, String term) {
		this.t1 = t1;
		this.t2 = t2;
		this.logL = getDunning(term);
		this.term = term;
		this.p1HigherThenP2 = isP1HigherThenP2(term);
	}

	private boolean isP1HigherThenP2(String term2) {
		double k1 = t1.occurrences(term);
		double k2 = t2.occurrences(term);
		double n1 = t1.size();
		double n2 = t2.size();
		return (k1 / n1) > (k2 / n2);
	}

	public double value() {
		return p1HigherThenP2 ? logL : -logL;
	}

	/**
	 * 
	 * @see Ted Dunning, 1993
	 * 
	 */
	private double getDunning(String t) {
		double k1 = t1.occurrences(t);
		double k2 = t2.occurrences(t);
		double n1 = t1.size();
		double n2 = t2.size();
		double p1 = k1 / n1;
		double p2 = k2 / n2;
		double p = (k1 + k2) / (n1 + n2);
		return 2 * (logL(p1, k1, n1) + logL(p2, k2, n2) - logL(p, k1, n1) - logL(p, k2, n2));
	}

	private double logL(double p, double k, double n) {
		return (k == 0 ? 0 : k * Math.log(p)) + ((n - k) == 0 ? 0 : (n - k) * Math.log(1 - p));
	}

	// @Override
	public int compareTo(LogLikelihood other) {
		return (int) (other.value() - this.value());
	}

	@Override
	public String toString() {
		return String.format("logL(%s) = %.3f", term, value());
	}

	public static Comparison compare(Terms t1, Terms t2) {
		Comparison comparison = new Comparison();
		for (String each: new Terms(t1, t2).elementSet()) {
			comparison.add(new LogLikelihood(t1, t2, each));
		}
		Collections.sort(comparison);
		return comparison;
	}

	@SuppressWarnings("serial")
	public static class Comparison extends ArrayList<LogLikelihood> {

		public Comparison withThreshold(int logL) {
			Comparison selection = new Comparison();
			for (LogLikelihood each: this) {
				if (each.isAboveThreshold(logL)) selection.add(each);
			}
			return selection;
		}

	}

	public boolean isAboveThreshold(int threshold) {
		return threshold >= 0 ? this.value() >= threshold : this.value() <= threshold;
	}

	public static class Examples {

		@Test
		public void shouldComputeLogLikelihood() {
			Terms all = new Terms("A A A A A B C C C D D");
			Terms doc = new Terms("A A B B X");
			for (String each: doc.elements()) {
				LogLikelihood loglr = new LogLikelihood(all, doc, each);
				System.out.println(loglr);
			}
		}

	}

}

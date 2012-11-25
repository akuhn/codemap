package org.codemap.util;

import ch.akuhn.util.Assert;

public class Delimiter {

	private int tally = 0;
	private final int period;
	private int tallyCR = 0;
	private final int periodCR;
	private final String value;
	private boolean foo;

	public Delimiter(String value, int period, int linebreakPeriod) {
		this.value = Assert.notNull(value);
		this.period = period;
		this.periodCR = linebreakPeriod;
	}

	public Delimiter(String value, int period) {
		this(value, period, 0);
	}

	public boolean tally() {
		foo = true;
		return ++tally == period;
	}

	@Override
	public String toString() {
		if (!foo) tally++;
		foo = false;
		if (tally != period) return "";
		tally = 0;
		if (++tallyCR != periodCR) return value;
		tallyCR = 0;
		return "\n";
	}

}

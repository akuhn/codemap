package edu.berkeley.guir.prefuse.activity;

/**
 * A Pacer, or pacing function, simply maps one double value to another. They
 * are used to parameterize animation rates, where the input value f moves
 * from 0 to 1 linearly, but the returned output can vary quite differently
 * in response to the input.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 * @see edu.berkeley.guir.prefuse.activity.ActionList
 * @see edu.berkeley.guir.prefuse.activity.SlowInSlowOutPacer
 * @see edu.berkeley.guir.prefuse.activity.ThereAndBackPacer
 */
public interface Pacer {

    /**
     * Maps one double value to another to determine animation pacing. Both
     * the input and output values should be in the range 0-1, inclusive.
     * @param f the input value, should be between 0-1
     * @return the output value, should be between 0-1
     */
    public double pace(double f);
    
} // end of interface Pacer

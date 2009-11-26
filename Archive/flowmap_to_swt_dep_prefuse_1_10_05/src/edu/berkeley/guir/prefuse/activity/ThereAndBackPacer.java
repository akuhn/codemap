package edu.berkeley.guir.prefuse.activity;

/**
 * Pacing function that maps the animation fraction f such that it ranges
 * from 0 to 1 and then back to 0 again. This is useful for animations
 * with periodic activity.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 * @see edu.berkeley.guir.prefuse.activity.Pacer
 */
public class ThereAndBackPacer implements Pacer {

    /**
     * Pacing function for providing there-and-back (periodic) transitions.
     * @see edu.berkeley.guir.prefuse.activity.Pacer#pace(double)
     */
    public double pace(double f) {
        return 2*(f <= 0.5 ? f : (1-f));
    } //

} // end of class ThereAndBackPacer

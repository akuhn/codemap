package edu.berkeley.guir.prefusex.force;

/**
 * Interface for numerical integration routines. These routines are used
 * to update the position and velocity of items in response to forces.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public interface Integrator {

    public void integrate(ForceSimulator sim, long timestep);
    
} // end of interface Integrator

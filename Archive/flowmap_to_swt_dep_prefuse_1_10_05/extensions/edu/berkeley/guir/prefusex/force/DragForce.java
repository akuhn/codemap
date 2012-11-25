package edu.berkeley.guir.prefusex.force;

/**
 * Implements a viscosity/drag force to stabilize items.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class DragForce extends AbstractForce {

    private static String[] pnames = new String[] { "DragCoefficient" };
    
    public static final float DEFAULT_DRAG_COEFF = -0.01f;
    public static final int DRAG_COEFF = 0;

    public DragForce(float dragCoeff) {
        params = new float[] { dragCoeff };
    } //
    
    public DragForce() {
        this(DEFAULT_DRAG_COEFF);
    } //

    public boolean isItemForce() {
        return true;
    } //
    
    protected String[] getParameterNames() {
        return pnames;
    } //
    
    /**
     * Calculates the force vector acting on the given item.
     * @param item the ForceItem for which to compute the force
     */
    public void getForce(ForceItem item) {
        item.force[0] += params[DRAG_COEFF]*item.velocity[0];
        item.force[1] += params[DRAG_COEFF]*item.velocity[1];
    } //

} // end of class DragForce
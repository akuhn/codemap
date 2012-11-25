package edu.berkeley.guir.prefusex.force;

/**
 * Represents a constant gravitational force, like the pull of gravity
 * for an object on the Earth (F = mg). The force experienced by a
 * given item is calculated as the product of a GravitationalConstant 
 * parameter and the mass of the item.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class GravitationalForce extends AbstractForce {

    private static final String[] pnames
        = { "GravitationalConstant", "Direction" };
    public static final int GRAVITATIONAL_CONST = 0;
    public static final int DIRECTION = 1;
    public static final float DEFAULT_FORCE_CONSTANT = 1E-4f;
    public static final float DEFAULT_DIRECTION = -90;
    
    public GravitationalForce(float forceConstant, float direction) {
        params = new float[] { forceConstant, direction };
    } //
    
    public GravitationalForce() {
        this(DEFAULT_FORCE_CONSTANT, DEFAULT_DIRECTION);
    } //
    
    public boolean isItemForce() {
        return true;
    } //

    protected String[] getParameterNames() {
        return pnames;
    } //
    
    public void getForce(ForceItem item) {
        float theta = (float)Math.toRadians(-params[DIRECTION]);
        float coeff = params[GRAVITATIONAL_CONST]*item.mass;
        
        item.force[0] += Math.cos(theta)*coeff;
        item.force[1] += Math.sin(theta)*coeff;
    } //

} // end of class GravitationalForce

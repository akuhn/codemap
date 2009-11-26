package edu.berkeley.guir.prefusex.force;

/**
 * Computes the force on a node resulting from treating incident edges
 * as a system of springs.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class SpringForce extends AbstractForce {

    private static String[] pnames 
        = new String[] { "SpringCoefficient", "DefaultSpringLength" };
    
    public static final float DEFAULT_SPRING_COEFF = 1E-4f;
    public static final float DEFAULT_SPRING_LENGTH = 100;
    public static final int SPRING_COEFF = 0;
    public static final int SPRING_LENGTH = 1;

    private ForceSimulator fsim;
    
    public SpringForce(float springCoeff, float defaultLength) {
        params = new float[] { springCoeff, defaultLength };
    } //
    
    /**
     * Constructs a new SpringForce instance.
     */
    public SpringForce() {
        this(DEFAULT_SPRING_COEFF, DEFAULT_SPRING_LENGTH);
    } //

    public boolean isSpringForce() {
        return true;
    } //
    
    protected String[] getParameterNames() {
        return pnames;
    } //
    
    /**
     * Initialize this force function.
     * Subclasses should override this method with any needed initialization.
     * @param fsim the encompassing ForceSimulator
     */
    public void init(ForceSimulator fsim) {
        this.fsim = fsim;
    } //    
    
    /**
     * Calculates the force vector acting on the items due to the given spring.
     * @param s the Spring for which to compute the force
     */   
    public void getForce(Spring s) {
        ForceItem item1 = s.item1;
        ForceItem item2 = s.item2;
        float length = (s.length < 0 ? params[SPRING_LENGTH] : s.length);
        float x1 = item1.location[0], y1 = item1.location[1];
        float x2 = item2.location[0], y2 = item2.location[1];
        float dx = x2-x1, dy = y2-y1;
        float r  = (float)Math.sqrt(dx*dx+dy*dy);
        if ( r == 0.0 ) {
            dx = ((float)Math.random()-0.5f) / 50.0f;
            dy = ((float)Math.random()-0.5f) / 50.0f;
            r  = (float)Math.sqrt(dx*dx+dy*dy);
        }
        float d  = r-length;
        float coeff = (s.coeff < 0 ? params[SPRING_COEFF] : s.coeff)*d/r;
        item1.force[0] += coeff*dx;
        item1.force[1] += coeff*dy;
        item2.force[0] += -coeff*dx;
        item2.force[1] += -coeff*dy;
    } //
    
} // end of class SpringForce
package edu.berkeley.guir.prefusex.force;

/**
 * Abstract implementation of force functions in a force simulation. This
 * skeletal version provides support for storing and retrieving float-valued
 * parameters of the force function. Subclasses should use the protected
 * field <code>params</code> to store parameter values.
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefusex(AT)jheer.org
 */
public abstract class AbstractForce implements Force {

    protected float[] params;

    /**
     * Initialize this force function. This default implementation does nothing.
     * Subclasses should override this method with any needed initialization.
     * @param fsim the encompassing ForceSimulator
     */
    public void init(ForceSimulator fsim) {
        // do nothing.
    } //

    public int getParameterCount() {
        return ( params == null ? 0 : params.length );
    } //

    public float getParameter(int i) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return params[i];
        }
    } //
    
    public String getParameterName(int i) {
        String[] pnames = getParameterNames();
        if ( i < 0 || pnames == null || i >= pnames.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            return pnames[i];
        }
    } //

    public void setParameter(int i, float val) {
        if ( i < 0 || params == null || i >= params.length ) {
            throw new IndexOutOfBoundsException();
        } else {
            params[i] = val;
        }
    } //
    
    protected abstract String[] getParameterNames();
    
    public boolean isItemForce() {
        return false;
    } //
    
    public boolean isSpringForce() {
        return false;
    } //
    
    public void getForce(ForceItem item) {
        throw new UnsupportedOperationException(
            "This class does not support this operation");
    } //
    
    public void getForce(Spring spring) {
        throw new UnsupportedOperationException(
        "This class does not support this operation");
    } //
    
} // end of abstract class AbstractForce
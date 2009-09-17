package edu.berkeley.guir.prefusex.distortion;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * <p>
 * Creates a bifocal distortion of space, magnifying focal space and
 * uniformly demagnifying non-focal space, where focal space is defined
 * relative to the layout anchor.
 * </p>
 * 
 * <p>
 * For more details on this form of transformation, see Y. K. Leung and 
 * M. D. Apperley, "A Review and Taxonomy of Distortion-Oriented Presentation
 * Techniques", in Transactions of Computer-Human Interaction (TOCHI),
 * 1(2): 126-160 (1994). Available online at
 * <a href="portal.acm.org/citation.cfm?id=180173&dl=ACM">
 * portal.acm.org/citation.cfm?id=180173&dl=ACM</a>.
 * </p>
 * 
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class BifocalDistortion extends Distortion {
    
    private double rx, ry; // magnification ranges
    private double mx, my; // magnification factor
    private boolean bx, by; // is distortion enabled for this dimension?
    
    /**
     * Create a new BifocalDistortion with default range and magnification.
     */
    public BifocalDistortion() {
        this(0.1,3);
    } //
    
    /**
     * Create a new BifocalDistortion with the specified range and
     * magnification. The same range and magnification is used for both
     * axes.
     * <br/><br/>
     * <strong>NOTE:</strong>if the range value times the magnification
     * value is greater than 1, the resulting distortion can exceed the
     * display bounds.
     * @param range the range around the focus that should be magnified. This
     *  specifies the size of the magnified focus region, and should be in the
     *  range of 0 to 1, 0 being no magnification range and 1 being the whole
     *  display.
     * @param mag how much magnification should be used in the focal area
     */
    public BifocalDistortion(double range, double mag) {
        this(range,mag,range,mag);
    } //
    
    /**
     * Create a new BifocalDistortion with the specified range and 
     * magnification along both axes.
     * <br/><br/>
     * <strong>NOTE:</strong>if the range value times the magnification
     * value is greater than 1, the resulting distortion can exceed the
     * display bounds.
     * @param xrange the range around the focus that should be magnified along
     *  the x direction. This specifies the horizontal size of the magnified 
     *  focus region, and should be a value between 0 and 1, 0 indicating no
     *  focus region and 1 indicating the whole display.
     * @param xmag how much magnification along the x direction should be used
     *  in the focal area
     * @param yrange the range around the focus that should be magnified along
     *  the y direction. This specifies the vertical size of the magnified 
     *  focus region, and should be a value between 0 and 1, 0 indicating no
     *  focus region and 1 indicating the whole display.
     * @param ymag how much magnification along the y direction should be used
     *  in the focal area
     */
    public BifocalDistortion(double xrange, double xmag, double yrange, double ymag) {
        this(xrange, xmag, yrange, ymag, false);
    } //
    
    public BifocalDistortion(final double xrange, final double xmag, 
            final double yrange, final double ymag, final boolean useFilteredGraph) {
        super(useFilteredGraph);
        rx = xrange;
        mx = xmag;
        ry = yrange;
        my = ymag;
        bx = !(rx == 0 || mx == 1.0);
        by = !(ry == 0 || my == 1.0);
    }
    
    /**
     * Calculates a bifocal display distortion.
     */
    protected void transformPoint(Point2D o, Point2D p, 
            Point2D anchor, Rectangle2D bounds)
    {
        double x = o.getX();
        if ( bx )
            x = bifocal(x, anchor.getX(), rx, mx,
                    bounds.getMinX(), bounds.getMaxX());
        double y = o.getY();
        if ( by )
            y = bifocal(o.getY(), anchor.getY(), ry, my,
                    bounds.getMinY(), bounds.getMaxY());
        p.setLocation(x,y);
    } //
    

    /**
     * @see edu.berkeley.guir.prefusex.distortion.Distortion#transformSize(java.awt.geom.Rectangle2D, java.awt.geom.Point2D, java.awt.geom.Point2D, java.awt.geom.Rectangle2D)
     */
    protected double transformSize(Rectangle2D bbox, Point2D pf, 
            Point2D anchor, Rectangle2D bounds)
    {
        boolean xmag = false, ymag = false;
        double m;
        
        if ( bx ) {
            double x = bbox.getCenterX(), ax = anchor.getX();
            double minX = bounds.getMinX(), maxX = bounds.getMaxX();
            m = (x<ax ? ax-minX : maxX-ax);
            if ( m == 0 ) m = maxX-minX;
            if ( Math.abs(x-ax) <= rx*m )
                xmag = true;
        }
        
        if ( by ) {
            double y = bbox.getCenterY(), ay = anchor.getY();
            double minY = bounds.getMinY(), maxY = bounds.getMaxY();
            m = (y<ay ? ay-minY : maxY-ay);
            if ( m == 0 ) m = maxY-minY;
            if ( Math.abs(y-ay) <= ry*m )
                ymag = true;
        }
        
        if ( xmag && !by ) {
            return mx;
        } else if ( ymag && !bx ) {
            return my;
        } else if ( xmag && ymag ) {
            return Math.min(mx,my);
        } else {
            return Math.min((1-rx*mx)/(1-rx), (1-ry*my)/(1-ry));
        }
    } //
    
    private double bifocal(double x, double a, double r, double mag, double min, double max) {
        double m = (x<a ? a-min : max-a);
        if ( m == 0 ) m = max-min;
        double v = x - a, s = m*r;
        if ( Math.abs(v) <= s ) {  // in focus
            return x = v*mag + a;
        } else {                   // out of focus
            double bx = r*mag;
            x = ((Math.abs(v)-s) / m) * ((1-bx)/(1-r));
            return (v<0?-1:1)*m*(x + bx) + a;
        }
    } //

    /**
     * Returns the magnification factor for the x-axis.
     * @return Returns the magnification factor for the x-axis.
     */
    public double getXMagnification() {
        return mx;
    } //

    /**
     * Sets the magnification factor for the x-axis.
     * @param mx The magnification factor for the x-axis.
     */
    public void setXMagnification(double mx) {
        this.mx = mx;
    } //

    /**
     * Returns the magnification factor for the y-axis.
     * @return Returns the magnification factor for the y-axis.
     */
    public double getYMagnification() {
        return my;
    } //

    /**
     * Sets the magnification factor for the y-axis.
     * @param my The magnification factor for the y-axis.
     */
    public void setYMagnification(double my) {
        this.my = my;
    } //

    /**
     * Returns the range of the focal area along the x-axis.
     * @return Returns the range of the focal area along the x-axis.
     */
    public double getXRange() {
        return rx;
    } //

    /**
     * Sets the range of the focal area along the x-axis.
     * @param rx The focal range for the x-axis, a value between 0 and 1.
     */
    public void setXRange(double rx) {
        this.rx = rx;
    } //

    /**
     * Returns the range of the focal area along the y-axis.
     * @return Returns the range of the focal area along the y-axis.
     */
    public double getYRange() {
        return ry;
    } //

    /**
     * Sets the range of the focal area along the y-axis.
     * @param ry The focal range for the y-axis, a value between 0 and 1.
     */
    public void setYRange(double ry) {
        this.ry = ry;
    } //

} // end of class BifocalDistortion
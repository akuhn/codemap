package edu.berkeley.guir.prefusex.distortion;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import edu.berkeley.guir.prefuse.ItemRegistry;

/**
 * <p>
 * Creates a graphical fisheye distortion of a graph view. This distortion 
 * allocates more space to items near the layout anchor and less space to 
 * items further away, magnifying space near the anchor and demagnifying 
 * distant space.
 * </p>
 * 
 * <p>
 * For more details on this form of transformation, see Manojit Sarkar and 
 * Marc H. Brown, "Graphical Fisheye Views of Graphs", in Proceedings of 
 * CHI'92, Human Factors in Computing Systems, p. 83-91, 1992. Available
 * online at <a href="http://citeseer.ist.psu.edu/sarkar92graphical.html">
 * http://citeseer.ist.psu.edu/sarkar92graphical.html</a>. 
 * </p>
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class FisheyeDistortion extends Distortion {

    private double  dx, dy;   // distortion factors
    private boolean bx, by;   // is distortion enabled for this dimension?
    private double  sz = 3.0; // size factor
    
    /**
     * Creates a new FisheyeDistortion with default distortion factor.
     */
    public FisheyeDistortion() {
        this(4);
    } //
    
    /**
     * Creates a new FisheyeDistortion with the given distortion factor
     * for use along both the x and y directions.
     * @param dfactor the distortion factor (same for both axes)
     */
    public FisheyeDistortion(double dfactor) {
        this(dfactor, dfactor);
    } //
    
    /**
     * Creates a new FisheyeDistortion with the given distortion factors
     * along the x and y directions.
     * @param xfactor the distortion factor along the x axis
     * @param yfactor the distortion factor along the y axis
     */
    public FisheyeDistortion(double xfactor, double yfactor) {
        this(xfactor, yfactor, false);
    } //
    
    public FisheyeDistortion(final double xfactor, final double yfactor, final boolean useFilteredGraph) {
        super(useFilteredGraph);
        dx = xfactor;
        dy = yfactor;
        bx = dx > 0;
        by = dy > 0;
    }
    
    /**
     * Returns the distortion factor for the x-axis.
     * @return returns the distortion factor for the x-axis.
     */
    public double getXDistortionFactor() {
        return dx;
    } //

    /**
     * Sets the distortion factor for the x-axis.
     * @param d The distortion factor to set.
     */
    public void setXDistortionFactor(double d) {
        this.dx = d;
    } //
    
    /**
     * Returns the distortion factor for the y-axis.
     * @return returns the distortion factor for the y-axis.
     */
    public double getYDistortionFactor() {
        return dy;
    } //

    /**
     * Sets the distortion factor for the y-axis.
     * @param d The distortion factor to set.
     */
    public void setYDistortionFactor(double d) {
        this.dy = d;
    } //
    
    /**
     * Calculates a Cartesian graphical fisheye distortion.
     * @see edu.berkeley.guir.prefusex.distortion.Distortion#transformPoint(java.awt.geom.Point2D, java.awt.geom.Point2D, java.awt.geom.Point2D, java.awt.geom.Rectangle2D)
     */
    protected void transformPoint(Point2D o, Point2D p, 
            Point2D anchor, Rectangle2D bounds)
    {
        double x = o.getX(), y = o.getY();
        if ( bx )
            x = fisheye(x,anchor.getX(),dx,bounds.getMinX(),bounds.getMaxX());
        if ( by )
            y = fisheye(y,anchor.getY(),dy,bounds.getMinY(),bounds.getMaxY());
        p.setLocation(x,y);
    } //
    
    /**
     * Calculates the size scaling factor for a Cartesian 
     *  graphical fisheye distortion.
     * @see edu.berkeley.guir.prefusex.distortion.Distortion#transformSize(java.awt.geom.Rectangle2D, java.awt.geom.Point2D, java.awt.geom.Point2D, java.awt.geom.Rectangle2D)
     */
    protected double transformSize(Rectangle2D bbox, Point2D pf, 
            Point2D anchor, Rectangle2D bounds)
    {   
        if ( !bx && !by ) return 1.;
        double fx=1, fy=1;

        if ( bx ) {
            double ax = anchor.getX();
            double minX = bbox.getX(), maxX = bbox.getMaxX();
            double x = (Math.abs(minX-ax) > Math.abs(maxX-ax) ? minX : maxX);
            if ( x < bounds.getMinX() || x > bounds.getMaxX() )
                x = (x==minX ? maxX : minX);
            fx = fisheye(x,ax,dx,bounds.getMinX(),bounds.getMaxX());
            fx = Math.abs(pf.getX()-fx)/bbox.getWidth();
        }

        if ( by ) {
            double ay = anchor.getY();
            double minY = bbox.getY(), maxY = bbox.getMaxY();
            double y = (Math.abs(minY-ay) > Math.abs(maxY-ay) ? minY : maxY);
            if ( y < bounds.getMinY() || y > bounds.getMaxY() )
                y = (y==minY ? maxY : minY);
            fy = fisheye(y,ay,dy,bounds.getMinY(),bounds.getMaxY());
            fy = Math.abs(pf.getY()-fy)/bbox.getHeight();
        }
        
        double sf = (!by ? fx : (!bx ? fy : Math.min(fx,fy)));
        if (Double.isInfinite(sf) || Double.isNaN(sf)) {
            return 1.;
        } else {
            return sz*sf;
        }
    } //
    
    private double fisheye(double x, double a, double d, double min, double max) {
        if ( d != 0 ) {
            boolean left = x<a;
            double v, m = (left ? a-min : max-a);
            if ( m == 0 ) m = max-min;
            v = Math.abs(x - a) / m;
            v = (d+1)/(d+(1/v));
            return (left?-1:1)*m*v + a;
        } else {
            return x;
        }
    } //
    
    /**
     * Given an original x coordinate, return the distorted x
     * @return
     */
    public double fisheyeMove(final double undistortedX, final ItemRegistry registry) {
        final Rectangle2D bounds = getLayoutBounds(registry);
        final Point2D anchor = correct(getLayoutAnchor(), bounds);
        
		// compute distortion if we have a distortion focus
		if (anchor != null) {
			return fisheye(undistortedX,anchor.getX(),dx,bounds.getMinX(),bounds.getMaxX());
//			if (m_sizeDistorted) {
//				double sz = transformSize(bbox, loc, anchor, bounds);
//				item.setSize(sz * item.getEndSize());
//			}
		}
		return undistortedX;
    }
} // end of class FisheyeDistortion
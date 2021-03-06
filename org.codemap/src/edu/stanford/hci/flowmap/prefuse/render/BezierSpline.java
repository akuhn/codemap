package edu.stanford.hci.flowmap.prefuse.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codemap.util.geom.CubicCurve2D;
import org.codemap.util.geom.Point2D;

import edu.stanford.hci.flowmap.utils.GraphicsGems;

/**
 * This software is distributed under the Berkeley Software Distribution
 * License. Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 * 
 */
public class BezierSpline {

    // given a list of points to interpolate through (catmull rom points),
    // returns a List
    // of CubicCurve2D objects with the curves to interpolate through
    public static Collection<CubicCurve2D> computeSplines(Point2D[] points) {
        List<CubicCurve2D> newCubics = new ArrayList<CubicCurve2D>();
        CubicCurve2D bezierCurve;
        int i;

        // now separate out the points. we assume that there are at least 4
        // points,
        // of which the first and last are dummy points (so there are only 2
        // real points)
        for (i = 1; i < points.length - 2; i++) {
            bezierCurve = new CubicCurve2D.Double();
            computeOneSpline(points[i - 1], points[i], points[i + 1],
                    points[i + 2], bezierCurve);
            newCubics.add(bezierCurve);
        }

        return newCubics;

    }

    public static void computeOneSpline(Point2D v0, Point2D v1, Point2D v2,
            Point2D v3, CubicCurve2D curve) {

        GraphicsGems.checkNaN(v0);
        GraphicsGems.checkNaN(v1);
        GraphicsGems.checkNaN(v2);
        GraphicsGems.checkNaN(v3);

        double ctrlx1 = (-v0.getX() / 6) + v1.getX() + v2.getX() / 6;
        double ctrly1 = (-v0.getY() / 6) + v1.getY() + v2.getY() / 6;

        double ctrlx2 = (v1.getX() / 6) + v2.getX() - v3.getX() / 6;
        double ctrly2 = (v1.getY() / 6) + v2.getY() - v3.getY() / 6;

        curve.setCurve(v1.getX(), v1.getY(), ctrlx1, ctrly1, ctrlx2, ctrly2, v2
                .getX(), v2.getY());
    }
}

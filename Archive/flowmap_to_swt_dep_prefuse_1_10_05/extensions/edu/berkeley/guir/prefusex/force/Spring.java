package edu.berkeley.guir.prefusex.force;

import java.util.ArrayList;

/**
 * Represents a spring in a force simulation.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class Spring {
    private static SpringFactory s_factory = new SpringFactory();
    
    public static SpringFactory getFactory() {
        return s_factory;
    } //
    
    public Spring(ForceItem fi1, ForceItem fi2, float k, float len) {
        item1 = fi1;
        item2 = fi2;
        coeff = k;
        length = len;
    } //
    public ForceItem item1;
    public ForceItem item2;
    public float length;
    public float coeff;
    
    public static final class SpringFactory {
        private int maxSprings = 10000;
        private ArrayList springs = new ArrayList();
        
        public Spring getSpring(ForceItem f1, ForceItem f2, float k, float length) {
            if ( springs.size() > 0 ) {
                Spring s = (Spring)springs.remove(springs.size()-1);
                s.item1 = f1;
                s.item2 = f2;
                s.coeff = k;
                s.length = length;
                return s;
            } else {
                return new Spring(f1,f2,k,length);
            }
        } //
        public void reclaim(Spring s) {
            s.item1 = null;
            s.item2 = null;
            if ( springs.size() < maxSprings )
                springs.add(s);
        } //
    }
} // end of class Spring

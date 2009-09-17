package edu.berkeley.guir.prefusex.force;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Manages a simulation of physical forces acting on bodies. To create a
 * custom ForceSimulator, add the desired force functions and choose an
 * appropriate integrator.
 *
 * @version 1.0
 * @author <a href="http://jheer.org">Jeffrey Heer</a> prefuse(AT)jheer.org
 */
public class ForceSimulator {

    private Set items;
    private Set springs;
    private Force[] iforces;
    private Force[] sforces;
    private int iflen, sflen;
    private Integrator integrator;
    private float speedLimit = 1.0f;
    
    public ForceSimulator() {
        this(new RungeKuttaIntegrator());
    } //

    public ForceSimulator(Integrator integr) {
        integrator = integr;
        iforces = new Force[5];
        sforces = new Force[5];
        iflen = 0;
        sflen = 0;
        items = new HashSet();
        springs = new HashSet();
    } //

    public float getSpeedLimit() {
        return speedLimit;
    } //
    
    public void setSpeedLimit(float limit) {
        speedLimit = limit;
    } //
    
    public void clear() {
        items.clear();
        Iterator siter = springs.iterator();
        Spring.SpringFactory f = Spring.getFactory();
        while ( siter.hasNext() )
            f.reclaim((Spring)siter.next());
        springs.clear();
    } //
    
    public void addForce(Force f) {
        if ( f.isItemForce() ) {
            if ( iforces.length == iflen ) {
                // resize necessary
                Force[] newf = new Force[iflen+10];
                System.arraycopy(iforces, 0, newf, 0, iforces.length);
                iforces = newf;
            }
            iforces[iflen++] = f;
        }
        if ( f.isSpringForce() ) {
            if ( sforces.length == sflen ) {
                // resize necessary
                Force[] newf = new Force[sflen+10];
                System.arraycopy(sforces, 0, newf, 0, sforces.length);
                sforces = newf;
            }
            sforces[sflen++] = f;
        }
    } //
    
    public Force[] getForces() {
        Force[] rv = new Force[iflen+sflen];
        System.arraycopy(iforces, 0, rv, 0, iflen);
        System.arraycopy(sforces, 0, rv, iflen, sflen);
        return rv;
    } //
    
    public void addItem(ForceItem item) {
        items.add(item);
    } //
    
    public boolean removeItem(ForceItem item) {
        return items.remove(item);
    } //

    public Iterator getItems() {
        return items.iterator();
    } //
    
    public Spring addSpring(ForceItem item1, ForceItem item2) {
        return addSpring(item1, item2, -1.f, -1.f);
    } //
    
    public Spring addSpring(ForceItem item1, ForceItem item2, float length) {
        return addSpring(item1, item2, -1.f, length);
    } //
    
    public Spring addSpring(ForceItem item1, ForceItem item2, float coeff, float length) {
        if ( item1 == null || item2 == null )
            throw new IllegalArgumentException("ForceItems must be non-null");
        Spring s = Spring.getFactory().getSpring(item1, item2, coeff, length);
        springs.add(s);
        return s;
    } //
    
    public boolean removeSpring(Spring s) {
        return springs.remove(s);
    }
    
    public Iterator getSprings() {
        return springs.iterator();
    } //
    
    public void runSimulator(long timestep) {
        accumulate();
        integrator.integrate(this, timestep);
    } //
    
    /**
     * Accumulate all forces acting on the items in this simulation
     */
    public void accumulate() {
        for ( int i = 0; i < iflen; i++ )
            iforces[i].init(this);
        for ( int i = 0; i < sflen; i++ )
            sforces[i].init(this);
        Iterator itemIter = items.iterator();
        while ( itemIter.hasNext() ) {
            ForceItem item = (ForceItem)itemIter.next();
            item.force[0] = 0.0f; item.force[1] = 0.0f;
            for ( int i = 0; i < iflen; i++ )
                iforces[i].getForce(item);
        }
        Iterator springIter = springs.iterator();
        while ( springIter.hasNext() ) {
            Spring s = (Spring)springIter.next();
            for ( int i = 0; i < sflen; i++ ) {
                sforces[i].getForce(s);
            }
        }
    } //
    
} // end of class ForceSimulator

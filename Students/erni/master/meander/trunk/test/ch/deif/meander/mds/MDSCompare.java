package ch.deif.meander.mds;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;


public class MDSCompare {
    
    @Test
    public void checkSameOutput() {
        assertArrayEquals(originalMds(), originalMds());
    }
    
    private double[][] originalMds() {
        double[][] input = {{ 0.0, 0.0, 0.0 }, 
                { 1.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0 }, 
                { 1.0, 1.0, 0.0 }, 
                { 0.5, 0.5, 1.0 }};
        
        HitMDSOriginal originalMds = new HitMDSOriginal();
        return originalMds.evaluate(input, -2);        
    }
    
    private double[][] newMDS() {
        double[][] input = {{ 0.0, 0.0, 0.0 }, 
                { 1.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0 }, 
                { 1.0, 1.0, 0.0 }, 
                { 0.5, 0.5, 1.0 }};
        
        HitMDS newMds = new HitMDS().withModRandom();
        return newMds.evaluate(input, -2);        
    }
}

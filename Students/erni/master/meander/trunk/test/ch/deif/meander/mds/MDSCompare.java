package ch.deif.meander.mds;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class MDSCompare {
    
    @Test
    public void checkSameOutput() {
        assertArrayEquals(originalMds(), newMDS());
    }

    private double[][] originalMds() {
        double[][] input = { { 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0 }, { 1.0, 1.0, 0.0 }, { 0.5, 0.5, 1.0 } };

        HitMDSOriginal originalMds = new HitMDSOriginal();
        return originalMds.evaluate(input, -2);
    }

    public static void main(String... args) {
        double[][] newMDS = new MDSCompare().originalMds();
        System.out.println("----------------------");
        for (double[] row : newMDS) {
            System.out.print(row[0]);
            System.out.print(" ");
            System.out.print(row[1]);
            System.out.println();
        }
    }

    private double[][] newMDS() {
        double[][] input = { { 0.0, 0.0, 0.0 }, { 1.0, 0.0, 0.0 },
                { 0.0, 1.0, 0.0 }, { 1.0, 1.0, 0.0 }, { 0.5, 0.5, 1.0 } };

        HitMDS newMds = new HitMDS().withModRandom();
        return newMds.evaluate(input, -2);
    }
}

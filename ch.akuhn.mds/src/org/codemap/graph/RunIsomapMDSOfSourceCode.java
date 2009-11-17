package org.codemap.graph;

import ch.akuhn.codemap.Codemap;
import ch.akuhn.hapax.Hapax;
import ch.akuhn.matrix.Function;
import ch.akuhn.mds.MultidimensionalScaling;
import ch.akuhn.org.ggobi.plugins.ggvis.Viz;
import ch.akuhn.util.Out;

public class RunIsomapMDSOfSourceCode {

    public static void main(String[] args) {
        
        new Codemap(
                //"../ch.akuhn.util",
                //"../ch.akuhn.mds",
                //"../ch.akuhn.hapax",
                //"../maven-artifact",
                //"../javalanche_src",
                //"../org.codemap",
                "../ch.deif.meander")
            .applyIsomap()
            //.applyEuclidianDistance()
            //.multiplyBothMetrics()
            .visuallyRunMDS();
        
    }
    
}

package org.codemap.eclemma;


/**
 * Null implementation that runs when eclemma is not loaded
 * 
 * @author deif
 */
public class NullCoverageListener implements ICodemapCoverage {

    @Override
    public void dispose() {
    }

    @Override
    public void setEnabled(boolean b) {
    }

    @Override
    public boolean isEclemmaPluginAvailable() {
        return false;
    }

}

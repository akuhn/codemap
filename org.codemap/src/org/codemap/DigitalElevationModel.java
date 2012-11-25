package org.codemap;

public class DigitalElevationModel {

    private float[][] DEM;

    public DigitalElevationModel(float[][] DEM) {
       this.DEM = DEM;
    }

    public float[][] asFloatArray() {
        return DEM;
    }

    public int getSize() {
        return DEM.length;
    }

}

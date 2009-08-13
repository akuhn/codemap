package ch.deif.meander;

public class DigitalElevationModel {

    private float[][] DEM;

    public DigitalElevationModel(float[][] DEM) {
       this.DEM = DEM;
    }

    public float[][] asFloatArray() {
        return DEM;
    }

}

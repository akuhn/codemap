package ch.deif.meander;

public class DigitalElevationModel {

	private float[][] DEM;
	
	public DigitalElevationModel(int size) {
		this.DEM = new float[size][size];
	}
	
	public float[][] getModel() {
		return DEM;
	}
	
}

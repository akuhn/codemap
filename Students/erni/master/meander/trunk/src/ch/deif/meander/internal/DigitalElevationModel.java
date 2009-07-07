package ch.deif.meander.internal;

import java.util.concurrent.FutureTask;

import ch.deif.meander.MapInstance;

public class DigitalElevationModel implements Runnable {

	
	
	public final float[][] elevation;
	
	public DigitalElevationModel(MapInstance map) {
		elevation = new float[map.width][map.height];
	}
	
	public void run() {
		
	}
	
	public FutureTask<DigitalElevationModel> makeFuture(MapInstance map) {
		DigitalElevationModel model = new DigitalElevationModel(map);
		return new FutureTask<DigitalElevationModel>(model, model);
	}
	
}

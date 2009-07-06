package ch.deif.aNewMeander;

import static java.lang.Math.PI;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import ch.deif.aNewMeander.MapConfigurationWithSize.Kernel;

/**
 * @see http://edndoc.esri.com/arcobjects/9.2/NET/shared/geoprocessing/ spatial_analyst_tools/how_hillshade_works.htm
 * @see Burrough, P. A. and McDonell, R.A., 1998. Principles of Geographical Information Systems (Oxford University
 *      Press, New York), p. 190.
 */
public class HillshadeAlgorithm implements MapAlgorithm<double[][]> {

	private static final double Z_FACTOR = 0.6e-3;

	@Override
	public double[][] runWith(MapConfigurationWithSize map) {
		// zenith: height of sun over horizon (90 = horizon, 0 = zenith).
		double zenithRad = 45 * PI / 180;
		// azimuth: direction of sun on x-y-plane
		double azimuthRad = (315 - 180) * PI / 180;
		double z_factor = Z_FACTOR * map.getWidth();
		double[][] hillshade = new double[map.width][map.width];
		for (Kernel k: map.kernels()) {
			double dx = (k.topRight + (2 * k.right) + k.bottomRight - (k.topLeft + (2 * k.left) + k.bottomLeft)) / 8;
			double dy = (k.bottomLeft + (2 * k.bottom) + k.bottomRight - (k.topLeft + (2 * k.top) + k.topRight)) / 8;
			double slopeRad = atan(z_factor * sqrt(dx * dx + (dy * dy)));
			double aspectRad = atan2(dy, -dx);
			double shading = (cos(zenithRad) * cos(slopeRad) + (sin(zenithRad) * sin(slopeRad) * cos(azimuthRad
					- aspectRad)));
			hillshade[k.px][k.py] = shading;
		}
		return hillshade;
	}

}

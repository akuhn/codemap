package ch.deif.meander;

import ch.deif.meander.Map.Kernel;

public class HillshadeAlgorithm extends MapAlgorithm {

    public HillshadeAlgorithm(Map map) {
        super(map);
    }

    @Override
    public void run() {
        double zenithRad = 45 * Math.PI / 180;
        double azimuthRad = (315 - 180) * Math.PI / 180;
        double z_factor = 0.3;
        for (Kernel k: map.kernels()) {
            double dx = (k.c + (2 * k.f) + k.i - (k.a + (2 * k.d) + k.g)) / 8 * 1;
            double dy = (k.g + (2 * k.h) + k.i - (k.a + (2 * k.b) + k.c)) / 8 * 1;
            double slopeRad = Math.atan(z_factor * Math.sqrt(dx * dx + (dy * dy)));
            double aspectRad = Math.atan2(dy, -dx);
            if (aspectRad < 0) { aspectRad = 2 * Math.PI + aspectRad; };
            double color = (Math.cos(zenithRad) * Math.cos(slopeRad) 
                      + (Math.sin(zenithRad) * Math.sin(slopeRad) * Math.cos(azimuthRad - aspectRad)));
            k.setHillshade(color);
        }
    }

}

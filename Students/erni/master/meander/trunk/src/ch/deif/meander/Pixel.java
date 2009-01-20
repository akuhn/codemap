package ch.deif.meander;

public class Pixel {

    private Map map;
    private int n;
    private int m;

    public Pixel(Map map, int n, int m) {
        assert n < map.width && m < map.height : n + "," + m;
        this.map = map;
        this.n = n;
        this.m = m;
    }

    public double x() {
        return 2.0 * n / map.width - 1.0; 
    }

    public double y() {
        return 2.0 * m / map.height - 1.0; 
    }

    public void add(double elevation) {
        map.DEM[n][m] += elevation;
    }

    public double elevation() {
        return map.DEM[n][m];
    }
    
}

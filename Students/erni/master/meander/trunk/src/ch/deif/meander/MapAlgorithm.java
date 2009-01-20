package ch.deif.meander;

public abstract class MapAlgorithm implements Runnable {

    private Map map;
    
    public MapAlgorithm(Map map) {
        this.map = map;
    }
    
    public abstract void run();
    
}

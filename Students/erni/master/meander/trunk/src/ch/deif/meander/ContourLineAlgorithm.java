package ch.deif.meander;

import ch.deif.meander.Map.Kernel;

public class ContourLineAlgorithm extends MapAlgorithm {

    public ContourLineAlgorithm(Map map) {
        super(map);
    }

    @Override
    public void run() {
        for (Kernel k: map.kernels()) {
            int top = (int) Math.floor(k.top / 10);
            int left = (int) Math.floor(k.left / 10);
            int here = (int) Math.floor(k.here / 10);
            boolean coastline = here == 0 || left == 0 || top == 0;
            k.setContourLine((top != here || left != here) && !coastline);
        }
    }

}

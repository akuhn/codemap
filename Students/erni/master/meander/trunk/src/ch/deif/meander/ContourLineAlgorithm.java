package ch.deif.meander;

import ch.deif.meander.Map.Kernel;

public class ContourLineAlgorithm extends MapAlgorithm {

    public ContourLineAlgorithm(Map map) {
        super(map);
    }

    @Override
    public void run() {
        for (Kernel k: map.kernels()) {
            int top = (int) Math.floor(k.b / 10);
            int left = (int) Math.floor(k.d / 10);
            int here = (int) Math.floor(k.e / 10);
            //int bottom = (int) Math.floor(k.h / 10);
            //int right = (int) Math.floor(k.f / 10);
            k.setContourLine((top != here || left != here) && here != 0 && left != 0 && top != 0
                    //&& (bottom != here || right != here)
                    );
        }
    }

}

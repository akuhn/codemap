package ch.deif.meander.internal;

import java.util.ArrayList;
import java.util.List;

import ch.deif.meander.MapAlgorithm;
import ch.deif.meander.MapSetting;
import ch.deif.meander.MapInstance.Kernel;
import ch.deif.meander.util.SparseTrueBooleanList;
import ch.deif.meander.util.StopWatch;

public class ContourLineAlgorithm extends MapAlgorithm<List<SparseTrueBooleanList>> {

    public static final MapSetting<Integer> CONTOUR_STEP = MapSetting.define("CONTOUR_STEP", 10);

    @Override
    public List<SparseTrueBooleanList> call() {
        StopWatch stopWatch = new StopWatch("ContourLineAlgorithm").start();
        
        int step = map.get(CONTOUR_STEP);
        boolean[][] contour = new boolean[map.getWidth()][map.getWidth()];
        for (Kernel k: map.kernels()) {
            int top = (int) Math.floor(k.top / step);
            int left = (int) Math.floor(k.left / step);
            int here = (int) Math.floor(k.here / step);
            boolean coastline = here == 0 || left == 0 || top == 0;
            contour[k.px][k.py] = ((top != here || left != here) && !coastline);
        }
        ArrayList<SparseTrueBooleanList> result = new ArrayList<SparseTrueBooleanList>(contour.length);
        for (int row = 0; row < contour.length; row++) {
            // TODO use a simple List of RLE list in worst case
            result.add(new SparseTrueBooleanList(contour[row]));
        }
        stopWatch.printStop();
        return result;
    }

}

package ch.deif.meander.util;

import java.util.Calendar;

public class StopWatch {
    
    private static boolean IS_VERBOSE = true;

    private String name;
    private long before;
    private long after;

    public StopWatch(String n) {
        name = n;
    }

    public StopWatch start() {
        before = Calendar.getInstance().getTimeInMillis();
        return this;
    }

    public StopWatch stop() {
        after = Calendar.getInstance().getTimeInMillis();
        return this;
    }

    public StopWatch printStop() {
        stop();
        if (!IS_VERBOSE) return this;
        System.out.println(name + ": " + (after - before));
        return this;
    }
}

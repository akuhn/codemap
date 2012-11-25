package org.codemap.util;


public class StopWatch {
    
    private static boolean IS_VERBOSE = false;

    private String name;
    private long before;
    private long after;
    private long runtime;    

    public StopWatch(String n) {
        name = n;
    }

    public StopWatch start() {
        before = System.currentTimeMillis();
        return this;
    }

    public StopWatch stop() {
        after = System.currentTimeMillis();
        runtime += after - before;
        return this;
    }

    public StopWatch printStop() {
        stop();
        print();
        return this;
    }

    public StopWatch print() {
        if (!IS_VERBOSE) return this;
        System.out.println(name + ": " + runtime);
        return this;
    }
    
    
}

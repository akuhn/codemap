package ch.deif.meander.mds;


public class MDSCompare {

    public static void main(String... args) {
        double[][] input = {{ 0.0, 0.0, 0.0 }, 
                            { 1.0, 0.0, 0.0 },
                            { 0.0, 1.0, 0.0 }, 
                            { 1.0, 1.0, 0.0 }, 
                            { 0.5, 0.5, 1.0 }};
        HitMDS mds = new HitMDS();
        double[][] evaluate = mds.evaluate(input, -2);
        
        System.out.println("----------------------------------------------");
        System.out.println("results:");
        for (double[] row : evaluate) {
            for (double elem: row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
    }

}

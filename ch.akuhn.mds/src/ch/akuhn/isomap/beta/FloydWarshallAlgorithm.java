package ch.akuhn.isomap.beta;

public class FloydWarshallAlgorithm {

    public static void apply(int[][] path) {
        for (int k = 0; k < path.length; k++) {
            for (int i = 0; i < path.length; i++) {
                int path_i_k = path[i][k];
                for (int j = 0; j < path.length; j++) {
                    path[i][j] = Math.min(path[i][j], path_i_k + path[k][j]);
                }
            }
        }
    }
    
    public static DoubleMatrix apply(double[][] path) {
        for (int k = 0; k < path.length; k++) {
            for (int i = 0; i < path.length; i++) {
                double path_i_k = path[i][k];
                for (int j = 0; j < path.length; j++) {
                    path[i][j] = Math.min(path[i][j], path_i_k + path[k][j]);
                }
            }
        }
        return new DoubleMatrix(path);
    }
    
}

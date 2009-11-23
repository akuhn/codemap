package ch.akuhn.isomap;

import java.util.Arrays;

import ch.akuhn.isomap.beta.DijkstraAlgorithm2;
import ch.akuhn.isomap.beta.Graph;
import ch.akuhn.matrix.SymetricMatrix;
import ch.akuhn.matrix.Function;
import ch.akuhn.org.ggobi.plugins.ggvis.Points;
import ch.akuhn.org.netlib.arpack.Eigenvalues;
import ch.akuhn.util.Stopwatch;

/** Maps n-dimensional data to 2 dimensions.
 *<P> 
 * The Isomap algorithm takes as input the distances d_x(i,j) between all
 * pairs i, j from N data points in the high-dimensional input space X,
 * measured either in the standard Euclidean metric or in some domain-specific
 * metric. The algorithm outputs coordinate vectors y_i in a d-dimensional
 * Euclidean space Y that best represent the intrinsic geometry of the data.
 * The only free parameter (e or k) appears in Step 1.
 *<P>
 *<P> 
 *  
 */
public abstract class Isomap {

    public double e = 0.2;
    public int k = 3;
    public boolean useKNN = true;
    public final int n;
    private SymetricMatrix graph;
    private Points points;
    
    public Isomap(int size) {
        this.n = size;
    }
    
    protected abstract double dist(int i, int j);
    
    public Points getPoints() {
        return points;
    }
    
    /** Step 1: Construct neighborhood graph. Define the graph G over all data
     * points by connecting points i and j if [as measured by d_x(i,j)] they
     * are closer than e (e-Isomap), or if i is one of the k nearest neighbors
     * of j (k-Isomap). Set edge lengths equal to d_x(i,j).
     * 
     */
    public void constructNeighborhoodGraph() {
        if (useKNN) {
            constructKayNearestNeighbeorhoodGraph();
        } else {
            constructCloserThanEpsilonNeighborhoodGraph();
        }
    }
    
    private void constructCloserThanEpsilonNeighborhoodGraph() {
        graph = new SymetricMatrix(n);
        graph.fill(Double.POSITIVE_INFINITY);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (dist(i,j) < e) graph.put(i, j, e);
            }
        }
    }
    
    private void constructKayNearestNeighbeorhoodGraph() {
        graph = new SymetricMatrix(n);
        graph.fill(Double.POSITIVE_INFINITY);
        for (int i = 0; i < n; i++) {
            double[] data = new double[n];
            for (int j = 0; j < n; j++) {
                data[j] = dist(i,j);
            }
            double[] minima = Arrays.copyOf(data, k + 1);
            Arrays.sort(minima);
            for (int j = k + 1; j < data.length; j++) {
                if (data[j] >= minima[k]) continue;
                int n = -1-Arrays.binarySearch(minima, data[j]);
                System.arraycopy(minima, n, minima, n+1, k-n);
                minima[n] = data[j];
            }
            double min = minima[k];
            for (int j = k + 1; j < data.length; j++) {
                if (data[j] <= min) graph.put(i,j,data[j]);
            }
        }
    }
    
    /** Step 2: Compute shortest path. Initialize d_G(i,j) = d_X(i,j) if i, j are
     * linked by an edge; d_G(i,j) = infinity otherwise. Then for each value of
     * k in 1, 2, ..., N in turn, replace all entries d_G(i,j) by min { d_G(i,j),
     * d_G(i,k) + d_G(k,j)}. The matrix of final values D_G = { d_G(i,j) } will
     * contain the shortest path distances between all pairs of points in G.
     *
     */
    public void computeShortestPath() {
        
        SymetricMatrix dist = graph.clone();
        
        Stopwatch.p();
        
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                double path_i_k = graph.get(i,k);
                for (int j = 0; j < n; j++) {
                    graph.put(i,j, Math.min(graph.get(i,j), path_i_k + graph.get(k,j)));
                }
            }
        }

        Stopwatch.p("Floyd-Warschal");
        
        Graph g = new Graph(dist);
        DijkstraAlgorithm2 dijsktra = new DijkstraAlgorithm2();
        for (int i = 0; i < n; i++) {
            double[] cost = dijsktra.apply(g, g.nodes[i], dist);
            for (int j = 0; j < n; j++) {
                assert (Double.isInfinite(graph.get(i,j)) && Double.isInfinite(cost[j])) ||
                    (graph.get(i,j) - cost[j]) < 1e-6 : i + "," + j + " : " + graph.get(i,j) +" vs "+ cost[j];
            }
        }
 
        Stopwatch.p("Dijkstra with queue");
        
    }
    
    public boolean[][] getEdges() {
        boolean[][] edges = new boolean[graph.values.length][];
        for (int i = 0; i < edges.length; i++) edges[i] = new boolean[i];
        for (int i = 0; i < edges.length; i++) {
            for (int j = 0; j < i; j++) {
                edges[i][j] = !Double.isInfinite(graph.values[i][j]);
            }
        }
        return edges;
    }
    
    /** Step 3: Construct d-dimensional embedding. Let λ_p be the p-th eigenvalue
     * (in decreasing order) of the matrix tau(D_G), and v^i_p be the i-th component
     * of the p-th eigenvector. Then set the p-th component of the d-dimensional
     * coordinate vector y_i equal to sqrt of λ_p * v^i_p.
     * 
     */
    public void constructDeeDimensionalEmbedding() {
        this.applyTauOperator();
        Eigenvalues eigen = new Eigenvalues(n, 2) {
            @Override
            protected double[] callback(double[] vector) {
                return graph.mult(vector);
            }
        };
        eigen.run();
        points = new Points(n);
        for (int i = 0; i < n; i++) {
            points.x[i] = Math.sqrt(eigen.value[0]) * eigen.vector[0][i];
            points.y[i] = Math.sqrt(eigen.value[1]) * eigen.vector[1][i];
        }
    }
    
    /** Where the operator tau is defined by tau(D) = -HSH/2, where S is the matrix
     * of squared distances { s_ij = d_ij^2 }, and H is the "centering matrix"
     * { H_ij = d_ij - 1/N }.
     *<P>
     * This leaves open whether the centering matrix of D or S is used, and why they
     * refer to a delta in the centering matrix instead of using unit matrix minus
     * reciprocal N. However, in their matlab script, the isomap authors use
     *<PRE>-.5*(D.^2 - sum(D.^2)'*ones(1,N)/N - ones(N,1)*sum(D.^2)/N + sum(sum(D.^2))/(N^2))</PRE>
     * so I went with the same: first squaring the matrix, then subtracting column-
     * and row-wise mean and adding global mean, and eventually divide by minus two.  
     */
    private void applyTauOperator() {
        graph.apply(Function.SQUARE);
        double mean = graph.mean();
        double[] columnMean = graph.columwiseMean();
        int N = graph.columnCount();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < i; j++) {
                graph.values[i][j] += mean - columnMean[i] - columnMean[j];
            }
        }
        graph.applyMultiplication(-0.5);
    }

    public void run() {
        this.constructNeighborhoodGraph();
        this.computeShortestPath();
        this.constructDeeDimensionalEmbedding();
    }
    
}

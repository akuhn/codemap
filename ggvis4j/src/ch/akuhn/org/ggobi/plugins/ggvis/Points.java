package ch.akuhn.org.ggobi.plugins.ggvis;



public class Points {

    double[] x, y;

    //double[][] vals;
    int nrows, ncols, nels;
    public void arrayd_init_null() { throw null; }
    public Points(int nrows2, int ncols2) {
        nrows = nrows2;
        ncols = ncols2;
        nels = ncols * nrows;
        x = new double[nrows];
        y = new double[nrows];
    }
    public void arrayd_zero() {
        x = new double[nrows]; // LULZ
        y = new double[nrows]; // LULZ
    }
}
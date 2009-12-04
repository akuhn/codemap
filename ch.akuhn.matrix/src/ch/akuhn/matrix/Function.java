package ch.akuhn.matrix;

public abstract class Function {

    public static final Function COSINE_TO_DISSIMILARITY = new Function() {

        @Override
        public double apply(double d) {
            return Math.sqrt(Math.max(0, 1 - d));
        }
        
    };
    
    public static final Function SQUARE = new Function() {

        @Override
        public double apply(double d) {
            return d * d;
        }
        
    };;

    public abstract double apply(double d);

    public static Function IDENTITY = new Function() {
        @Override
        public double apply(double d) {
            return d;
        }
    };

    public final void apply(double[] ds, int length) {
    	for (int n = 0; n < length; n++) ds[n] = apply(ds[n]);
    }
    
    public final void apply(double[] ds) {
    	for (int n = 0; n < ds.length; n++) ds[n] = apply(ds[n]);
    }
    
    public final void apply(double[][] dss) {
    	for (double[] ds: dss) for (int n = 0; n < ds.length; n++) ds[n] = apply(ds[n]);
    }
    
    
    //return d < 1 ? d : (d * 10) - 9;
    //return d < 0.5 ? d * 2 : Math.pow(d * 2, 8);
    //return d > 0.5 ? d / 4 + 0.25 : d;
    //return d > 1.0 ? 1.0 : Math.pow(d, 0.25);
    //return d < 0.5 ? d * 2 : Math.pow(d * 2, 8);
    //double w = Math.abs(1 - d);
    //return w < 0 ? 0 : w > 1 ? 1 : w;
    
}

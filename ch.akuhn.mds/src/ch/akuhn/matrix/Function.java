package ch.akuhn.matrix;

public abstract class Function {

    public static final Function COSINE_TO_DISSIMILARITY = new Function() {

        @Override
        public double apply(double d) {
            return Math.sqrt(Math.max(0, 1 - d));
        }
        
    };

    public abstract double apply(double d);

    public static Function IDENTITY = new Function() {
        @Override
        public double apply(double d) {
            return d;
        }
    };

    
    //return d < 1 ? d : (d * 10) - 9;
    //return d < 0.5 ? d * 2 : Math.pow(d * 2, 8);
    //return d > 0.5 ? d / 4 + 0.25 : d;
    //return d > 1.0 ? 1.0 : Math.pow(d, 0.25);
    //return d < 0.5 ? d * 2 : Math.pow(d * 2, 8);
    //double w = Math.abs(1 - d);
    //return w < 0 ? 0 : w > 1 ? 1 : w;
    
}

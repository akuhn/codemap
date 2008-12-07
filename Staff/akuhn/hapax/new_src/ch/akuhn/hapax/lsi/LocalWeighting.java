package ch.akuhn.hapax.lsi;

public enum LocalWeighting {

    NULL,
    TERM,
    BINARY {
        @Override
        public double weight(double value) {
            return Math.signum(value);
        }
    },
    LOG{
        @Override
        public double weight(double value) {
            return Math.log(value);
        }
    };
    
    public double weight(double value) {
        return value;
    }
    
}

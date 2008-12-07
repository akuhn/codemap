package ch.akuhn.hapax.index;

public enum LocalWeighting {

    BINARY {
        @Override
        public double weight(double value) {
            return Math.signum(value);
        }
    },
    LOG {
        @Override
        public double weight(double value) {
            return Math.log(value);
        }
    },
    NULL, TERM;

    public double weight(double value) {
        return value;
    }

}

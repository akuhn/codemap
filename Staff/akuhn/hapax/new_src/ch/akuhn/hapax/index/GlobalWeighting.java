package ch.akuhn.hapax.index;

import ch.akuhn.hapax.linalg.Vector;
import ch.akuhn.hapax.linalg.Vector.Entry;

public enum GlobalWeighting {

    GFIDF { 
        @Override
        public double weight(Vector term) {
            return globalFrequency(term) / documentFrequency(term);
        }
    },
    IDF {
        @Override
        public double weight(Vector term) {
            return Math.log((term.size() / documentFrequency(term)));
        }
    },
    NULL,
    ENTROPY2 {
        @Override
        public double weight(Vector term) {
            double gf = globalFrequency(term);
            double prop = 0;
            for (Entry each: term.entries()) {
                if (each.value == 0) continue;
                prop += (each.value / gf) * Math.log(each.value * gf); 
            }
            return -prop;
        }
    },
    ENTROPY1 {
        @Override
        public double weight(Vector term) {
            return 1 - (ENTROPY2.weight(term) / Math.log(term.size()));
        }
    };
    
    public int documentFrequency(Vector term) {
        return term.used();
    }
    
    public int globalFrequency(Vector term) {
        return (int) term.sum();
    }
    
    public double weight(Vector term) {
        return 1.0d;
    }
    
}

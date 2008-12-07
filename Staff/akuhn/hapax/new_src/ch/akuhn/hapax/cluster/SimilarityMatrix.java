package ch.akuhn.hapax.cluster;

import ch.akuhn.hapax.linalg.SymetricMatrix;
import ch.akuhn.util.List;


public class SimilarityMatrix<T> extends SymetricMatrix {

    private List<T> elements;

    public SimilarityMatrix(List<T> elements, Similarity<? super T> sim) {
        super(elements.size());
        this.elements = elements;
        for (int row = 0; row < values.length; row++) {
            T element = elements.get(row);
            for (int column = 0; column < values[row].length; column++) {
                values[row][column] = sim.similarity(element, elements.get(column));
            }
        }
    }
    
    public Iterable<T> elements() {
        return elements;
    }
     
}

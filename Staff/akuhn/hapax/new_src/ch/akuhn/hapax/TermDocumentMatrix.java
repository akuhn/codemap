package ch.akuhn.hapax;

import java.util.ArrayList;
import java.util.List;

public class TermDocumentMatrix<T, D> extends SparseMatrix {

	private List<D> documents; // columns
	private List<T> terms; // rows
	
	public TermDocumentMatrix() {
		super(0,0);
		this.terms = new ArrayList<T>();
		this.documents = new ArrayList<D>();
	}
	
	public int addDocument(D document) {
		documents.add(document);
		return addColumn();
	}
	
	public int addTerm(T term) {
		terms.add(term);
		return addRow();
	}
	
}

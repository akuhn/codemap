package ch.akuhn.hapax.util;

import java.util.ArrayList;
import java.util.Collection;

import ch.akuhn.fame.FameDescription;
import ch.akuhn.fame.FamePackage;
import ch.akuhn.fame.FameProperty;
import ch.akuhn.fame.Tower;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.linalg.SVD;

@FamePackage("Hapax")
public class Serialization {

    @FameDescription("SingularValueDecomposition")
    static class MySVD {
        @FameProperty float[] s;
        @FameProperty MyMatrix u;
        @FameProperty MyMatrix v;
    }
    
    @FameDescription("Matrix")
    static class MyMatrix {
        @FameProperty(opposite="parent") Collection<MyDenseColumn> columns;
    }

    @FameDescription("DenseColumn")
    static class MyDenseColumn {
        @FameProperty(container=true,opposite="columns") MyMatrix parent;
        @FameProperty float[] values;
    }
    
    @FameDescription("LatentSemanticIndex")
    static class MyIndex {
        @FameProperty MySVD svd;
        @FameProperty MyTermDocumentMatrix tdm;
    }
    
    @FameDescription("TermDocumentMatrix")
    static class MyTermDocumentMatrix {
        @FameProperty MyCorpus documents;
        @FameProperty Collection<String> terms;
        @FameProperty double[] weightings;
        @FameProperty(opposite="parent") Collection<MySparseColumn> matrix;
    }
    
    @FameDescription("SparseColumn")
    static class MySparseColumn {
        @FameProperty(opposite="matrix") MyTermDocumentMatrix parent;
        @FameProperty int length;
        @FameProperty double[] data;
    }

    @FameDescription("Corpus")
    static class MyCorpus {
        @FameProperty Collection<MyDocument> documents;
    }
    
    @FameDescription("Document")
    static class MyDocument {
        @FameProperty String name;
    }

    public final Tower t = new Tower();
    
    public Serialization() {
        t.metamodel.withAll(
                MySVD.class,
                MyIndex.class,
                MyTermDocumentMatrix.class,
                MyCorpus.class);
    }
    
    public void add(LatentSemanticIndex index) {
        MySVD svd = makeSVD(index.svd);
        //MyTermDocumentMatrix tdm = makeTDM(index.documents, index.terms, index.globalWeighting);
    }
        
    private MySVD makeSVD(SVD svd) {
        MySVD element = new MySVD();
        element.s = svd.s;
        element.u = wrapDenseMatrix(element, svd.Ut);
        element.v = wrapDenseMatrix(element, svd.Vt);
        t.model.add(element);
        return element;
    }
    
    private MyMatrix wrapDenseMatrix(MySVD element, float[][] matrix) {
        MyMatrix m = new MyMatrix();
        m.columns = new ArrayList<MyDenseColumn>();
        for (float[] each: matrix) {
            MyDenseColumn column = new MyDenseColumn();
            column.values = each;
            column.parent = m;
            m.columns.add(column);
        }
        return m;
    }
    
}

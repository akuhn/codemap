package hapax.model;

import ch.akuhn.fame.MetaRepository;

public class HapaxModel {

    public static MetaRepository metamodel() {
        MetaRepository metamodel = new MetaRepository();
        importInto(metamodel);
        return metamodel;
    }

    public static void importInto(MetaRepository metamodel) {
        metamodel.with(Document.class);
    }

    public static void main(String[] args) {
        MetaRepository m2 = HapaxModel.metamodel();
        m2.exportMSE(System.out);
    }

}

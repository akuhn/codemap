package ch.deif.meander.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import ch.akuhn.hapax.corpus.Document;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.akuhn.hapax.index.TermDocumentMatrix;
import ch.akuhn.util.Get;
import ch.deif.meander.MDS;
import ch.deif.meander.Serializer;

public class CreateJunitMse {

    public static void main(String... args) throws FileNotFoundException {
        TermDocumentMatrix tdm = TermDocumentMatrix.readFrom(new Scanner(
                new File("mse/junit.TDM")));
        LatentSemanticIndex lsi = tdm.rejectAndWeight().createIndex();
        MDS mds = MDS.fromCorrelationMatrix(lsi);

        Serializer ser = new Serializer();
        ser.project("JUnit");
        String version = "";
        int index = 0;
        for (Document each : Get.sorted(tdm.documents())) {
            if (!each.version().equals(version)) {
                version = each.version();
                ser.release(version);
            }
            ser.location(mds.x[index], mds.y[index],
                    Math.sqrt(each.termSize()), each);
            index++;
        }
        ser.model().exportMSEFile("mse/junit_with_terms.mse");
    }
}

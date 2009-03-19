package meander.jsme2009.eclipse;

import meander.jsme2009.Serializer;
import meander.jsme2009.junit.ComputeHausdorff;

public class GetHausdorffDistances {

    public static void main(String[] args) {
        Serializer ser = new Serializer();
        ser.model().importMSEFile(EclipseRunLSIAndMDS.MSE_FILE);
        new ComputeHausdorff(ser).run();
    }
    
}

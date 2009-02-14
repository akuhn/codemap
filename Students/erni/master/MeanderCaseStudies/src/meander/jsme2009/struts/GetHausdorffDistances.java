package meander.jsme2009.struts;

import meander.jsme2009.junit.ComputeHausdorff;
import ch.deif.meander.Serializer;

public class GetHausdorffDistances {

    public static void main(String[] args) {
        Serializer ser = new Serializer();
        ser.model().importMSEFile("mse/struts_meander.mse");
        new ComputeHausdorff(ser).run();
    }
    
}

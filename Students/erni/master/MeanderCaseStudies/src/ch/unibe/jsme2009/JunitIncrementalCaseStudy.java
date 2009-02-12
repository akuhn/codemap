package ch.unibe.jsme2009;

import ch.akuhn.util.Get;
import ch.akuhn.util.query.AnySatisfy;
import ch.akuhn.util.query.Count;
import ch.deif.meander.Serializer;
import ch.deif.meander.Serializer.MSEDocument;
import ch.deif.meander.Serializer.MSEProject;
import ch.deif.meander.Serializer.MSERelease;

public class JunitIncrementalCaseStudy {
	
	public static void main(String... args) {
        Serializer ser = new Serializer();
        ser.model().importMSEFile("mse/junit_meander.mse");
        MSEProject proj = ser.model().all(MSEProject.class).iterator().next();
        MSERelease previous = null;
        
        for (MSERelease rel: proj.releases) {
        	if (previous != null) {
        		Count<MSEDocument> count = Count.from(rel.documents);
        		System.out.println(norm(Get.head(previous.documents).name));
        		
        		for (Count<MSEDocument> each : count ) {
        			AnySatisfy<MSEDocument> any = AnySatisfy.from(previous.documents);
        			for (AnySatisfy<MSEDocument> other : any) {
        				other.yield = norm(other.element.name).equals(norm(each.element.name));
        			}
        			count.yield = any.result();
        		}
        		System.out.printf("\t\t\t\t %.2f\n", (1.0*count.result()/rel.documents.size()));
        	}
        	previous = rel;
        }
	}

	private static Object norm(String name) {
		name = name.trim();
		if (name.startsWith("junit2/")) return name.substring("junit2/".length());
		if (name.startsWith("junit2.1/")) return name.substring("junit2.1/".length());
		if (name.startsWith("junit3/")) return name.substring("junit3/".length());
		return name;
	}

}

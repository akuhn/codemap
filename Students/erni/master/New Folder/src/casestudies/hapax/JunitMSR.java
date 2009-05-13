package casestudies.hapax;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.Meander;

public class JunitMSR implements Runnable {
		
	public static void main(String... args) {
		new JunitMSR().run();
	}

	@Override
	public void run() {
		
		Hapax h = Hapax.legomenon()
			.useCamelCaseScanner()
			.useStemming()
			.rejectRareWords()
			.rejectStopwords()
			.useTFIDF()
			.setBaseFolder("../MeanderCaseStudies/data/junit/")
			.makeCorpus();
		for (String each: VERSIONS) {
			//if (each != version) continue;
			h.addFiles(each, "java");
		}
		Meander m = Meander.with(h);
		m.makeMap("junit3.8.2.zip")
			.useHillshading()
			.getVisualization().openApplet();
		m.makeMap("junit4.0.zip")
			.useHillshading()
			.getVisualization().openApplet();
	}
	
    public final static String[] VERSIONS = { 
        "junit2.zip",
        "JUNIT2.1.ZIP",
        "JUNIT3.ZIP",
        "junit3.2.ZIP",
        "junit3.4.zip",
        "junit3.5.zip",
        "junit3.6.zip",
        "junit3.7.zip",
        "junit3.8.zip",
        "junit3.8.1.zip",
        "junit3.8.2.zip",
        "junit4.0.zip",
        "junit4.1.zip",
        "junit4.2.zip",
        "junit4.3.1.zip",
        "junit4.4.zip",
        "junit4.5.zip" };
	
	
}

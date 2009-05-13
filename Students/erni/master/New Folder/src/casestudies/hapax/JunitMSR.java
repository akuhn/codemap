package casestudies.hapax;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.Meander;
import ch.deif.meander.viz.MapVisualization;

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
			h.addFiles(each, "java");
		}
		Meander m = Meander.with(h);
		for (String each: VERSIONS) {
			MapVisualization viz = m.makeMap(each)
				.useHillshading()
				.getVisualization();
			viz.drawToPNG(each);
			if (each == "junit3.8.2.zip") viz.openApplet();
			if (each == "junit4.0.zip") viz.openApplet();
		}
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

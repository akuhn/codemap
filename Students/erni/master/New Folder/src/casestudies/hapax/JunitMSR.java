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
			.ignoreCase()
			.useCamelCaseScanner()
			.rejectRareWords()
			.rejectStopwords()
			.useTFIDF()
			.setBaseFolder("../MeanderCaseStudies/data/junit/")
			.makeCorpus();
		for (String each: VERSIONS) {
			System.out.println("parsing " + each);
			h.addFiles(each, "java");
		}
		h.closeCorpus();
		Meander m = Meander.with(h).pixelWidth(1600);
		float[][] oldDEM = null;
		for (String each: VERSIONS) {
			System.out.println("saving " + each);
			MapVisualization viz = m.makeMap(each)
				.blackAndWhite()
				.useHillshading()
				.alsoShowShoresOf(oldDEM)
				.getVisualization();
			viz.drawToPNG(each);
			oldDEM = viz.map.getDEM();
			// if (each == "junit3.8.2.zip") viz.openApplet();
			// if (each == "junit4.0.zip") viz.openApplet();
		}
	}
	
    public final static String[] VERSIONS = { 
        "junit2.zip",
//        "JUNIT2.1.ZIP",
        "JUNIT3.ZIP",
        "junit3.2.ZIP",
        "junit3.4.zip",
        "junit3.5.zip",
        "junit3.6.zip",
        "junit3.7.zip",
        "junit3.8.zip",
//        "junit3.8.1.zip",
//        "junit3.8.2.zip",
        "junit4.0.zip",
        "junit4.1.zip",
        "junit4.2.zip",
        "junit4.3.1.zip",
        "junit4.4.zip",
        "junit4.5.zip" };
	
	
}

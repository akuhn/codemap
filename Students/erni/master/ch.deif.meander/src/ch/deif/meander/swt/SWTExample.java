package ch.deif.meander.swt;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.MapInstance;
import ch.deif.meander.builder.Meander;

public class SWTExample {

	public static void main(String[] args) {
		MapInstance map = Meander.builder()
			.addCorpus(Hapax.legomenon().makeCorpus("../ch.akuhn.hapax", ".java"))
			.makeMap()
			.withSize(512);
		Background layer = new Background();
		layer.children.add(new WaterBackground());
		layer.children.add(new ShoreLayer());
		layer.children.add(new HillshadeLayer());
		CodemapVisualization visual = new CodemapVisualization(map);
		visual.add(layer);
		visual.add(new LabelOverlay());
		visual.open();
	}

//	private static void startAnimationLoop(final Display display, final Canvas canvas) {
//		Runnable runnable = new Runnable() {
//			@Override
//			public void run() {
//				if (canvas.isDisposed()) return;
//				canvas.redraw();
//				display.timerExec(1000/25, this);
//			}
//		};
//		display.timerExec(1000/25, runnable);
//	}

}

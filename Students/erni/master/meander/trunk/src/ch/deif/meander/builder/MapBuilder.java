package ch.deif.meander.builder;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.MapConfiguration;

public interface MapBuilder {

	MapBuilder addCorpus(Hapax hapax);

	MapConfiguration makeMap();

}

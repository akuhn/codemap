package ch.deif.aNewMeander.builder;

import ch.akuhn.hapax.Hapax;
import ch.deif.aNewMeander.MapConfiguration;

public interface MapBuilder {

	MapBuilder addCorpus(Hapax hapax);

	MapConfiguration makeMap();

}

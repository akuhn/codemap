package ch.deif.meander.builder;

import ch.akuhn.hapax.Hapax;
import ch.deif.meander.Configuration;

public interface MapBuilder {

	MapBuilder addCorpus(Hapax hapax);

	Configuration makeMap();

}

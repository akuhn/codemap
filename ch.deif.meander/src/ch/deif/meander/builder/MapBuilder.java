package ch.deif.meander.builder;

import java.util.Map;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.util.Pair;
import ch.deif.meander.Configuration;

public interface MapBuilder {

	MapBuilder addCorpus(Hapax hapax);

	Configuration makeMap();

	Configuration makeMap(Map<String, Pair<Double, Double>> map);

}

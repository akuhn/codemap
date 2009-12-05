package ch.deif.meander.map;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.codemap.util.MapScheme;

import ch.akuhn.hapax.Hapax;
import ch.akuhn.hapax.index.LatentSemanticIndex;
import ch.deif.meander.Configuration;
import ch.deif.meander.Point;
import ch.deif.meander.internal.LayoutAlgorithm;

/** Not thread-safe.
 * 
 * @author akuhn
 *
 */
public class MapBuilder {
    
    public static final MapScheme<Double> FILE_LENGTH_SQRT = new MapScheme<Double>() {
        @Override
        public Double forLocation(Point location) {
            return Math.sqrt(new File(location.getDocument()).length());
        }
    };    

    private LatentSemanticIndex lsi = null;

    public MapBuilder addCorpus(Hapax hapax) {
        if (this.lsi != null) throw new IllegalStateException();
        this.lsi = hapax.getIndex();
        return this;
    }

    public Configuration makeMap(Configuration initialConfiguration) {
        if (this.lsi == null) throw new IllegalStateException();
        double[] x = new double[lsi.documentCount()];
        double[] y = new double[lsi.documentCount()];
        Map<String, Point> cachedPoints = initialConfiguration.asMap();
        int n = 0;
        for (String document: lsi.documents()) {
            Point p = cachedPoints.get(document);
            if (p == null) p = Point.newRandom(document);
            x[n] = p.x;
            y[n] = p.y;
            n++;
        }
        LayoutAlgorithm mds = LayoutAlgorithm.fromCorrelationMatrix(lsi, x, y);
        mds.normalize();
        Collection<Point> locations = new ArrayList<Point>();
        int index = 0;
        for (String each: lsi.documents()) {
            locations.add(new Point(mds.x[index], mds.y[index], each));
            index++;
        }
        return new Configuration(locations).normalize();
    }

    public MapBuilder addCorpus(LatentSemanticIndex index) {
        if (this.lsi != null) throw new IllegalStateException();
        this.lsi = index;
        return this;
    }
}
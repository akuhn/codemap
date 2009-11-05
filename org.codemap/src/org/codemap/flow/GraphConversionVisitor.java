package org.codemap.flow;

import java.util.HashMap;
import java.util.Map;

import org.codemap.util.Resources;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.corext.callhierarchy.MethodWrapper;

import ch.deif.meander.Location;
import edu.stanford.hci.flowmap.structure.Graph;
import edu.stanford.hci.flowmap.structure.Node;

public class GraphConversionVisitor {
    
    private Map<String, Location> locations;
    private Map<Location, Integer> locationWeights = new HashMap<Location, Integer>();
    private Location root;

    public GraphConversionVisitor(Map<String, Location> locations) {
        this.locations = locations;
    }

    public void visit(MethodCallNode methodCallNode) {
       MethodWrapper sourceMethod = methodCallNode.getSourceMethod();
       ICompilationUnit compilationUnit = sourceMethod.getMember().getCompilationUnit();
       String path = Resources.asPath(compilationUnit);
       Location location = locations.get(path);
       
       if (locationWeights.isEmpty()) {
           root = location;
       }
       
       Integer weight = locationWeights.get(location);
       if (weight == null) {
           weight = 0;
       }
       weight += 1;
       locationWeights.put(location, weight);
    }

    public Graph createGraph() {    
        Graph graph = new Graph();
        for (Location each: locationWeights.keySet()) {
            Integer weight = locationWeights.get(each);
            Node node = new Node(each.px, each.py, weight, each.getDocument());
            graph.addNode(node);
            if (each.equals(root)) {
                graph.setRootNode(node);
            }
        }
        return graph;
    }

}

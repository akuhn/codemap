package org.codemap.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codemap.util.Resources;
import org.eclipse.jdt.core.ICompilationUnit;

import ch.deif.meander.Location;
import edu.stanford.hci.flowmap.structure.Graph;
import edu.stanford.hci.flowmap.structure.Node;

public class GraphConversionVisitor {
    
    private Map<String, Location> locations;
    private ArrayList<Graph> graphs = new ArrayList<Graph>();

    public GraphConversionVisitor(Map<String, Location> locations) {
        this.locations = locations;
    }

    public void visit(MethodCallNode methodCallNode) {
       String rootPath = getPath(methodCallNode);
       // no paht for binary members etc.
       if (rootPath == null) return;
       
       ArrayList<MethodCallNode> children = methodCallNode.getChildren();
       if (children.isEmpty()) return;         
       
       Map<Location, Integer> locationWeights = new HashMap<Location, Integer>();
       for (MethodCallNode each : children) {
           String childPath = getPath(each);
           // child might not have been resolved
           if (childPath == null) continue;
           
           // do not add an edge from the node to itself because
           // * the clustering algorithm does not like it
           // * it won't appear on the map anyway as the edge has a length of 0
           if (childPath.equals(rootPath)) continue;
           
           Location location = locations.get(childPath);
           
           Integer weight = locationWeights.get(location);
           if (weight == null) {
               weight = 0;
           }
           weight += 1;
           locationWeights.put(location, weight);
       }
       
       Graph graph = new Graph();
       
       Location rootLocation = locations.get(rootPath);
       Node rootNode = new Node(rootLocation.px, rootLocation.py, 1, rootLocation.getDocument());
       graph.addNode(rootNode);
       graph.setRootNode(rootNode);
       
       for (Location each: locationWeights.keySet()) {
           Integer weight = locationWeights.get(each);
           Node node = new Node(each.px, each.py, weight, each.getDocument());
           graph.addNode(node);
       }
       graphs.add(graph);
    }

    private String getPath(MethodCallNode methodCallNode) {
        ICompilationUnit compilationUnit = methodCallNode.getSourceMethod().getMember().getCompilationUnit();
        if (compilationUnit == null) return null;
        String path = Resources.asPath(compilationUnit);
        return path;
    }

    public ArrayList<Graph> getGraphs() {
        return graphs;
    }
}

package edu.stanford.hci.flowmap.cluster;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import edu.stanford.hci.flowmap.db.RowSchema;
import edu.stanford.hci.flowmap.structure.Node;
/**
 * Turns a list of structure.Node objects into structure.Node objects that are in clusters
 * and attached to one another.
 * 
 * This software is distributed under the Berkeley Software Distribution License.
 * Please see http://graphics.stanford.edu/~dphan/code/bsd.license.html
 *
 */
public class HierarchicalCluster {

	private ClusterDistance clusDist;
	
	public HierarchicalCluster() {
		clusDist = new ClusterDistance();
	}
	
	/**
	 * Manages distances between clusters
	 * @author dphan
	 */
	private class ClusterDistance {
		
		// sorted set of clusters with shortest distance first
		public TreeSet closestPair;
		
		// set of Clusters that are under consideration
		public HashSet<Cluster> currentClusters;
		
		// maps a cluster to a Set of DistancePairs it appears in
		// we need this association to be able to invalidate some distances
		// when we remove it from the ClusterDistance objects
		public HashMap clusterAssoc;
		
		public ClusterDistance() {
			clusterAssoc = new HashMap();
			closestPair = new TreeSet();
			currentClusters = new HashSet<Cluster>();
		}
		
		public void clear() {
			clusterAssoc.clear();
			closestPair.clear();
			currentClusters.clear();
		}
		
		public int numClusters() {
			return currentClusters.size();
		}
		
		public Collection<Cluster> getClusters() {
			return currentClusters;
		}
		
		public void addDistPairCollection(Collection c) {
			// add the crossProduct to ClusterDistance
			Iterator i = c.iterator();
			while (i.hasNext()) {
				addDistPair((DistancePair) i.next());
			}
		}
		
		public void addDistPair(DistancePair dp) {
			closestPair.add(dp);
			
			// update cluster association list
			addClusterAssoc((Cluster) dp.one, dp);
			addClusterAssoc((Cluster) dp.two, dp);
			
			currentClusters.add((Cluster) dp.one);
			currentClusters.add((Cluster) dp.two);
		}
		
		/**
		 * Adds a new pair and distance to the data structure
		 * @param one
		 * @param two
		 */
		public void addDist(Cluster one, Cluster two) {
			double dist = one.distTo(two);
			DistancePair p = new DistancePair(one, two, dist);
			addDistPair(p);			
		}
		
		/**
		 * Returns and removes the closest pair
		 * @return the closest pair from the data structure or null if none exists
		 */
		public DistancePair removeClosest() {
			if (closestPair.size() == 0)
				return null;
			else {
				DistancePair dp = (DistancePair) closestPair.first();
				closestPair.remove(dp);
				
				// update association info
				removeClusterAssoc((Cluster) dp.one);
				removeClusterAssoc((Cluster) dp.two);
				
				// update current cluster info
				currentClusters.remove(dp.one);
				currentClusters.remove(dp.two);
				
				return dp;
			}
		}
		
		/**
		 * If the association does not yet exist, associates key with value
		 * @param key Cluster to associate
		 * @param value DistancePair to be associated
		 */
		private void addClusterAssoc(Cluster key, DistancePair value) {
			// lookup association in Map
			Object o = clusterAssoc.get(key);
			HashSet assocSet;
			if (o != null) {
				assocSet = (HashSet)o;
			} else {
				assocSet = new HashSet();
				clusterAssoc.put(key, assocSet);
			}
			assocSet.add(value);
			//System.out.println("ClusterAssoc: " + "key: " + key + ", " + assocSet);
		}
		
		/**
		 * Removes all information associated with that cluster. 
		 * @param c The cluster to be removed
		 */
		private void removeClusterAssoc(Cluster c) {
			
			// first remove the association
			Object o = clusterAssoc.get(c);
			clusterAssoc.remove(c);
			
			// now update the closestPair Set
			HashSet assocSet;
			
			if (o == null)
				return;
			
			assocSet = (HashSet)o;
			//System.out.println("RemoveClusterAssoc: " + "key: " + c + ", " + assocSet);
			Iterator i = assocSet.iterator();
			while(i.hasNext()) {
				closestPair.remove(i.next());
			}
			
			
		}
		
		
	}
	
	/**
	 * Generates cross product of the two lists of Clusters, 
	 * but ASSUMES a and b are the same list
	 * @return A collection of all possible DistancePair objects between a and b
	 */
	private Collection<DistancePair> crossSameCluster(Collection<Cluster> a, Collection<Cluster> b) {
		LinkedList<DistancePair> list = new LinkedList<DistancePair>();
		
		// records how many times we have run the outer loop; 
		int count = 0;
		int currCount = 0;
		
		for(Cluster one : a) {
			currCount = 0;
			for(Cluster two : b) {
				if (one.equals(two))
					continue;
				if (currCount < count) {
					currCount++;
					continue;
				}
				
				double dist = one.distTo(two);
				DistancePair dp = new DistancePair(one, two, dist);
				//System.out.println("Added " + dp);
				list.add(dp);
			}
			count++;
		}
		return list;
	}
	
	private Collection<DistancePair> crossDiffCluster(Cluster clus, Collection<Cluster> a) {
		LinkedList<DistancePair> list = new LinkedList<DistancePair>();
		Iterator i = a.iterator();
		
		Cluster one;
		while(i.hasNext()) {
			one = (Cluster)i.next();
			if (clus.equals(one))
				continue;
			double dist = clus.distTo(one);
			DistancePair dp = new DistancePair(clus, one, dist);
			list.add(dp);
		}
		return list;
	}

	
	/**
	 * This method performs hierarchical clustering on a collection of nodes
	 * where one node is designated the root. The idea is that we keep the root cluster
	 * in the collection of nodes we are clustering. Then, as we cluster, if we ever include
	 * the root cluster, we consider the cluster that was paired with the root to be 
	 * a finished cluster. We store that finished cluster.
	 * 
	 * We then reintroduce the root cluster into the mix and continue the process above until
	 * we run out of things to cluster. At the end we return a collection of all the 
	 * finished clusters.
	 * 
	 * @param rootNode the root node of the cluster
	 * @param destNodes a collection of destination nodes
	 * @return a collection of clusters that don't include the root node
	 */
	private Collection<Cluster> rootHierarchicalCluster(Node rootNode, Collection<Node> allNodes) {
		RowSchema rowSchema = rootNode.getQueryRow().getRowSchema();
		//System.out.println("HierarchicalCluster.rootHierarchicalCluster rowSchema: " + rowSchema);
		
		LinkedList<Cluster> clusterCollection = new LinkedList<Cluster>();
		LinkedList<Cluster> leafClusters = new LinkedList<Cluster>();
		LinkedList<Cluster> copyClusters = new LinkedList<Cluster>();
		
		// create a rootCluster and add it to the leafClusters and copyClusters
		Cluster rootCluster = new Cluster(rootNode, rowSchema);
		leafClusters.add(rootCluster);
		copyClusters.add(rootCluster);
		
		// create Clusters for each node except the root node
		for(Node n : allNodes) {
			if (n == rootNode) // don't create two clusters for the root
				continue;
			Cluster c = new Cluster(n, rowSchema);
			//System.out.println("adding new cluster: " + n.getName() + " " + n.getID());
			leafClusters.add(c);
			copyClusters.add(c);
		}
		
		Collection crossProd = crossSameCluster(leafClusters, copyClusters);
		clusDist.addDistPairCollection(crossProd);		
		
		// start of clustering method
		DistancePair pair;
		Cluster newClus, oneClus, twoClus;
		newClus = oneClus = twoClus = null;
		
		while (clusDist.numClusters() > 0) {
			
			// get the two closest clusters 
			pair = clusDist.removeClosest();
			assert(pair != null);
			oneClus = (Cluster)pair.one;
			twoClus = (Cluster)pair.two;
			
			// if the either element of the pair includes the rootCluster,
			// add the other element of the pair to the clusterCollection
			if (rootCluster.equals(oneClus) || rootCluster.equals(twoClus)) {
				if (rootCluster.equals(oneClus)) {
					clusterCollection.add(twoClus);
				} else {
					clusterCollection.add(oneClus);
				}
				newClus = rootCluster;
			} else {
				newClus = new Cluster(oneClus, twoClus, rowSchema);
			}
			
			crossProd = crossDiffCluster(newClus, clusDist.getClusters());;
			clusDist.addDistPairCollection(crossProd);		
		}
		
		/*System.out.println("HierarchicalCluster got: ");
		Iterator clusterIter = clusterCollection.iterator();
		while (clusterIter.hasNext()) {
			System.out.println("Clusters are " + clusterIter.next());
		}
		*/
		// the last cluster will be the newClus created in the previous iteration
		return clusterCollection;
	}
	
	public Collection<Cluster> doCluster(Node rootN, Collection<Node> allNodes) {
		return rootHierarchicalCluster(rootN, allNodes);	
	}
	
}

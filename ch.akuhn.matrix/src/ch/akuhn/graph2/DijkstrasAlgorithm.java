package ch.akuhn.graph2;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class DijkstrasAlgorithm {

	private static final int INFINITY = Integer.MAX_VALUE;

	public int[][] apply(Node[] nodes) {
		int[][] dist = new int[nodes.length][];
		for (int n = 0; n < nodes.length; n++) {
			dist[n] = dijkstra(nodes, n);
		}
		return dist;
	}

	public int[] dijkstra(Node[] nodes, int curr) {
		// 1) Assign to every node a tentative distance value: set it to zero
		// for our initial node and to infinity for all other nodes.
		// 2) Mark all nodes unvisited. Set the initial node as current. Create
		// a set of the unvisited nodes called the unvisited set consisting of
		// all the nodes except the initial node.
		// 3) For the current node, consider all of its unvisited neighbors and
		// calculate their tentative distances. For example, if the current node
		// A is marked with a tentative distance of 6, and the edge connecting
		// it with a neighbor B has length 2, then the distance to B (through A)
		// will be 6+2=8. If this distance is less than the previously recorded
		// tentative distance of B, then overwrite that distance. Even though a
		// neighbor has been examined, it is not marked as "visited" at this
		// time, and it remains in the unvisited set.
		// 4) When we are done considering all of the neighbors of the current
		// node, mark the current node as visited and remove it from the
		// unvisited set. A visited node will never be checked again; its
		// distance recorded now is final and minimal.
		// 5) If the destination node has been marked visited (when planning a
		// route between two specific nodes) or if the smallest tentative
		// distance among the nodes in the unvisited set is infinity (when
		// planning a complete traversal), then stop. The algorithm has
		// finished.
		// 6) Set the unvisited node marked with the smallest tentative distance
		// as the next "current node" and go back to step 3.
		int[] dist = new int[nodes.length];
		Arrays.fill(dist, INFINITY);
		dist[curr] = 0;
		boolean[] done = new boolean[nodes.length];
		while (true) {
			int a = findNext(dist, done);
			if (a == -1) break;
			for (int b = 0; b < nodes.length; b++) {
				if (done[b]) continue;
				int d = nodes[a].cost[b];
				if (d == INFINITY) continue;
				d += dist[a];
				if (d < dist[b]) dist[b] = d;
			}
			done[a] = true;
		}
		return dist;
	}

	private int findNext(int[] dist, boolean[] done) {
		int found = -1;
		int cost = INFINITY;
		for (int i = 0; i < dist.length; i++) {
			if (done[i]) continue;
			if (dist[i] < cost) {
				cost = dist[i];
				found = i;
			}
		}
		return found;
	}

	public static class Examples {

		@Test
		public void shouldFindShortestPath() {
			String s = "6 7\n1 2 20\n1 3 5\n1 4 10\n2 3 8\n2 4 15\n3 4 2\n5 6 9";
			Node[] nodes = Node.parse(s);
			int[][] dist = new DijkstrasAlgorithm().apply(nodes);
			assertEquals(6, dist.length);
		}
	}

}

package ch.akuhn.graph2;

public class Edge implements Comparable<Edge> {

	int a, b;
	int cost;

	public Edge(int a, int b, int cost) {
		super();
		this.a = a;
		this.b = b;
		this.cost = cost;
	}

	@Override
	public int compareTo(Edge that) {
		return this.cost - that.cost;
	}

	@Override
	public String toString() {
		return String.format("cost(%d,%d) = %d", a, b, cost);
	}

}

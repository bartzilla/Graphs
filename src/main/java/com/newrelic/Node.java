package com.newrelic;

import java.util.HashSet;
import java.util.Set;

public class Node implements Comparable {

    private String label;
    private Set<Edge> edges;
    private int distance;

    public Node(final String label) {
        this.label = label;
        this.edges = new HashSet<>();
    }

    public Set<Node> getNeighbors() {
        final Set<Node> neighbors = new HashSet<>();

        for (final Edge edge : edges) {
            neighbors.add(edge.getNextNode(this));
        }
        return neighbors;
    }

    public void addEdge(final Edge edge) {
        edges.add(edge);
    }

    public String getLabel() {
        return label;
    }

    public Set<Edge> getEdges() {
        return edges;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(final int distance) {
        this.distance = distance;
    }

    public int compareTo(final Object o) {
        final Node m = (Node) o;
        return Integer.compare(this.getDistance(), m.getDistance());
    }

}

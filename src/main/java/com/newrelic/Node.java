package com.newrelic;

import java.util.HashSet;
import java.util.Set;

class Node implements Comparable {

    private String label;
    private Set<Edge> edges;
    private int distance;

    Node(final String label) {
        this.label = label;
        this.edges = new HashSet<>();
    }

    Set<Node> getNeighbors() {
        final Set<Node> neighbors = new HashSet<>();

        for (final Edge edge : edges) {
            neighbors.add(edge.getNextNode(this));
        }
        return neighbors;
    }

    void addEdge(final Edge edge) {
        edges.add(edge);
    }

    String getLabel() {
        return label;
    }

    Set<Edge> getEdges() {
        return edges;
    }

    int getDistance() {
        return distance;
    }

    void setDistance(final int distance) {
        this.distance = distance;
    }

    public int compareTo(final Object o) {
        final Node m = (Node) o;
        return Integer.compare(this.getDistance(), m.getDistance());
    }
}

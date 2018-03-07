package com.bartzilla.graph;

import java.util.HashSet;
import java.util.Set;

/*
 *  Class representing graph nodes. Each node is town
 *  in Nerdland's train system. And each node contains
 *  a set of weighted edges representing train lines.
 *
 *  @author Cipriano Sanchez
 */
class Node implements Comparable<Node> {

    private String label;
    private Set<Edge> edges;
    private int distance;

    Node(final String label) {
        this.label = label;
        this.edges = new HashSet<>();
    }

    Set<Node> getNeighbors() {
        final Set<Node> neighbors = new HashSet<>();

        edges.forEach(e->neighbors.add(e.getNextNode(this)));

        return neighbors;
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

    void addEdge(final Edge edge) {
        edges.add(edge);
    }

    @Override
    public int compareTo(Node o) {
        return Integer.compare(this.getDistance(), o.getDistance());
    }
}

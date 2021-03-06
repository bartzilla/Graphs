package com.bartzilla.graph;

/*
 *  Class representing edges. This class is used to model
 *  train lines. Each edge contains information about the the edge starting node,
 *  ending node, and its weight.
 *
 *  @author Cipriano Sanchez
 */
class Edge {

    private int weight;
    private Node start;
    private Node end;

    Edge(final Node n1, final Node n2, final int weight) {
        this.start = n1;
        this.end = n2;
        this.weight = weight;
    }

    Node getEnd() {
        return end;
    }

    int getWeight() {
        return weight;
    }

    Node getNextNode(final Node node) {
        if (node.equals(start)) {
            return end;
        } else if (node.equals(end)) {
            return start;
        }
        throw new IllegalArgumentException("Next node is not in this edge");
    }
}

package com.newrelic;

import java.util.HashSet;
import java.util.Set;

public class MapNode implements Comparable {

    private String name;
    private Set<MapEdge> edges;
    private double distance;
    public boolean visited;

    /**
     * Create a new MapNode at a given Geographic location
     * @param name the location of this node
     */
    MapNode(String name)
    {
        this.name = name;
        edges = new HashSet<>();
    }

    /**
     * Return the neighbors of this MapNode
     * @return a set containing all the neighbors of this node
     */
    Set<MapNode> getNeighbors()
    {
        Set<MapNode> neighbors = new HashSet<MapNode>();
        for (MapEdge edge : edges) {
            neighbors.add(edge.getOtherNode(this));
        }
        return neighbors;
    }

    /**
     * Add an edge that is outgoing from this node in the graph
     * @param edge The edge to be added
     */
    void addEdge(MapEdge edge)
    {
        edges.add(edge);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MapEdge> getEdges() {
        return edges;
    }

    public void setEdges(Set<MapEdge> edges) {
        this.edges = edges;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    // Code to implement Comparable
    public int compareTo(Object o) {
        // convert to map node, may throw exception
        MapNode m = (MapNode)o;
        return ((Double)this.getDistance()).compareTo((Double) m.getDistance());
    }

    public String toString()
    {
        String toReturn = "[NODE at location (" + name + ")";
        toReturn += "]";
        return toReturn;
    }
}

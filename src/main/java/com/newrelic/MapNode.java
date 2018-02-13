package com.newrelic;

import com.newrelic.MapEdge;

import java.util.HashSet;
import java.util.Set;

public class MapNode {

    private String name;
    private Set<MapEdge> edges;

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
}

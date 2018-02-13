package com.newrelic;

import com.newrelic.MapEdge;

import java.util.*;

public class MapGraph {

    private Map<String, MapNode> vertices;
    private Set<MapEdge> edges;

    public MapGraph()
    {
        vertices = new HashMap<>();
        edges = new HashSet<>();
    }

    /** Add a node corresponding to an intersection at a Geographic Point
     * If the location is already in the graph or null, this method does
     * not change the graph.
     * @param name  The name of the intersection
     * @return true if a node was added, false if it was not (the node
     * was already in the graph, or the parameter is null).
     */
    public boolean addVertex(String name)
    {
        if (name == null) {
            return false;
        }

        MapNode n = vertices.get(name);
        if (n == null) {
            n = new MapNode(name);
            vertices.put(name, n);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds a directed edge to the graph from pt1 to pt2.
     * Precondition: Both GeographicPoints have already been added to the graph
     * @param from The starting point of the edge
     * @param to The ending point of the edge
     * @param length The length of the road, in km
     * @throws IllegalArgumentException If the points have not already been
     *   added as nodes to the graph, if any of the arguments is null,
     *   or if the length is less than 0.
     */
    public void addEdge(String from, String to, int length) throws IllegalArgumentException {

        MapNode n1 = vertices.get(from);
        MapNode n2 = vertices.get(to);

        // check nodes are valid
        if (n1 == null)
            throw new NullPointerException("addEdge: pt1:"+from+"is not in graph");
        if (n2 == null)
            throw new NullPointerException("addEdge: pt2:"+to+"is not in graph");

        MapEdge edge = new MapEdge(n1, n2, length);
        edges.add(edge);
        n1.addEdge(edge);

    }



    public Map<String, MapNode> getVertices() {
        return vertices;
    }

    public void setVertices(Map<String, MapNode> vertices) {
        this.vertices = vertices;
    }

//    public void setRoutes(String from, MapNode ) {
//        this.routes = routes;
//    }

    public int getDistance(String[] nodeKeys) {

        int distance = 0;
        int size = 0;
        for (int i = 0; i<nodeKeys.length - 1; i++) {

            if (vertices.containsKey(nodeKeys[i])) {
                Set<MapEdge> edges = vertices.get(nodeKeys[i]).getEdges();

                for (MapEdge edge : edges) {
                    if (edge.getEnd().getName().equals(nodeKeys[i + 1])) {
                        distance += edge.getWeight();
                        size++;
                        break;
                    }
                }
            } else {
                throw new RuntimeException("NO SUCH ROUTE");
            }
        }

        if (size != nodeKeys.length - 1) {
            throw new RuntimeException("NO SUCH ROUTE");
        }

        return distance;
    }

    public Set<MapEdge> getEdges() {
        return edges;
    }

    public void setEdges(Set<MapEdge> edges) {
        this.edges = edges;
    }
}

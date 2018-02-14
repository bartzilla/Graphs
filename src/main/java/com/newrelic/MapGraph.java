package com.newrelic;

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


    public List<String> getNumberOfTripsWithMaximumStops(String start, String goal, int maxStops) {

        // Setup - check validity of inputs
        if (start == null || goal == null)
            throw new NullPointerException("Cannot find route from or to null node");
        MapNode startNode = vertices.get(start);
        MapNode endNode = vertices.get(goal);
        if (startNode == null) {
            System.err.println("Start node " + start + " does not exist");
            return null;
        }
        if (endNode == null) {
            System.err.println("End node " + goal + " does not exist");
            return null;
        }

        List<List<MapNode>> paths = new ArrayList<>();

        recursive(0, startNode, endNode, paths, new LinkedHashSet<>());


        return null;
    }

    private void recursive(int iterations,  MapNode start, MapNode goal, List<List<MapNode>> paths, LinkedHashSet<MapNode> path) {
        path.add(start);

        if (!(iterations == 0) && start.equals(goal)) {
            paths.add(new ArrayList<>(path));
            path.remove(start);
            return;
        }

        Set<MapNode> neighbors = start.getNeighbors();
        for (MapNode neighbor : neighbors) {
//            if (!path.contains(neighbor)) {
                iterations++;
                recursive (iterations, neighbor, goal, paths, path);
//            }
        }

        path.remove(start);
    }

    public int dijkstraCoursera(String start, String goal) {

        // Initialize data structures
        Map<MapNode,MapNode> parentMap = new HashMap<>();
        PriorityQueue<MapNode> priorityQueue = new PriorityQueue<>();
        Set<MapNode> visited = new HashSet<>();

        for (MapNode n : vertices.values()) {
            n.setDistance(Double.POSITIVE_INFINITY);
        }

        MapNode startNode = vertices.get(start);

        startNode.setDistance(0);
        priorityQueue.add(startNode);
        int iterations = 0;
        while (!priorityQueue.isEmpty()) {
            MapNode current = priorityQueue.remove();
            iterations++;
            if (!visited.contains(current)) {

                if (!(iterations == 1) && start.equals(goal)) {
                    visited.add(current);
                }

                Set<MapEdge> edges = current.getEdges();
                for (MapEdge edge : edges) {

                    MapNode neighbor = edge.getEnd();
                    if (!visited.contains(neighbor)) {

                        double currDist = edge.getWeight() + current.getDistance();

                        if(currDist < neighbor.getDistance() || neighbor.getDistance() == 0 ){
                            parentMap.put(neighbor, current);
                            neighbor.setDistance(currDist);
                            priorityQueue.add(neighbor);
                        }
                    }

                }

            }

        }


        return (int) vertices.get(goal).getDistance();
    }


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
}

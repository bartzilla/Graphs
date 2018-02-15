package com.newrelic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Graph {

    private Map<String, Node> vertices;
    private Set<Edge> edges;

    private static final String NO_SUCH_ROUTE_ERROR_MESSAGE = "NO SUCH ROUTE";

    public Graph() {
        this.vertices = new HashMap<>();
        this.edges = new HashSet<>();
    }

    public void addVertex(final String label) {
        final Node n = new Node(label);
        vertices.put(label, n);
    }

    /**
     * Adds a directed edge to the graph from pt1 to pt2.
     * Precondition: Both GeographicPoints have already been added to the graph
     *
     * @param from   The starting point of the edge
     * @param to     The ending point of the edge
     * @param length The length of the road, in km
     * @throws IllegalArgumentException If the points have not already been
     *                                  added as nodes to the graph, if any of the arguments is null,
     *                                  or if the length is less than 0.
     */
    public void addEdge(final String from, final String to, final int length) throws IllegalArgumentException {

        final Node n1 = vertices.get(from);
        final Node n2 = vertices.get(to);

        if (n1 == null) {
            throw new IllegalArgumentException("Node \"from\": " + from + " is not in graph");
        }

        if (n2 == null) {
            throw new IllegalArgumentException("Node \"to\":" + to + "is not in graph");
        }

        final Edge edge = new Edge(n1, n2, length);
        edges.add(edge);
        n1.addEdge(edge);
    }

    public List<String> getNumberOfTripsWithMaximumStops(String start, String goal, int maxStops) {

        // Setup - check validity of inputs
        if (start == null || goal == null)
            throw new NullPointerException("Cannot find route from or to null node");
        Node startNode = vertices.get(start);
        Node endNode = vertices.get(goal);
        if (startNode == null) {
            System.err.println("Start node " + start + " does not exist");
            return null;
        }
        if (endNode == null) {
            System.err.println("End node " + goal + " does not exist");
            return null;
        }

        List<List<Node>> paths = new ArrayList<>();

        recursive(0, startNode, endNode, paths, new LinkedHashSet<>());

        return null;
    }

    private void recursive(Integer iterations, Node start, Node goal, List<List<Node>> paths, LinkedHashSet<Node> path) {
        path.add(start);

        if (start.equals(goal)) {
            paths.add(new ArrayList<>(path));
            Set<Node> neighbors = goal.getNeighbors();
            path.clear();
        } else if (iterations == 4) {
//            paths.add(new ArrayList<>(path));
            List<Node> nodes = new ArrayList<>(paths.get(paths.size() - 1));
            nodes.add(start);
            paths.add(new ArrayList<>(nodes));
            path.remove(start);
//            return;
        }

        Set<Node> neighbors = start.getNeighbors();
        for (Node neighbor : neighbors) {
            if (!path.contains(neighbor) && iterations < 4) {
                iterations++;
                recursive(iterations, neighbor, goal, paths, path);
            }
        }

        path.remove(start);
        iterations = 0;
    }

    public int diferentRoutes(final String start, final String goal, final int maxStops) {
        return diferentRoutes(start, goal, 0, maxStops);
    }

    private int diferentRoutes(final String start, final String goal, int distance, final int maxStops) {
        final Set<Edge> edges = this.vertices.get(start).getEdges();

        int routes = 0;
        for (final Edge edge : edges) {
            distance += edge.getWeight();

            if(distance < maxStops) {
                if(edge.getEnd().getLabel().equals(goal)) {
                    routes++;
                    routes += diferentRoutes(edge.getEnd().getLabel(), goal, distance, maxStops);
                }
                else {
                    routes += diferentRoutes(edge.getEnd().getLabel(), goal, distance, maxStops);
                    distance -= edge.getWeight();
                }
            }
        }

        return routes;
    }

    public int getShortestRoute(final String start, final String goal) {

        // Initialize data structures
        final Map<Node, Node> parentMap = new HashMap<>();
        final PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        final Set<Node> visited = new HashSet<>();

        for (final Node n : vertices.values()) {
            n.setDistance((int) Double.POSITIVE_INFINITY);
        }

        final Node startNode = vertices.get(start);

        startNode.setDistance(0);
        priorityQueue.add(startNode);
        int depth = 0;

        while (!priorityQueue.isEmpty()) {
            final Node current = priorityQueue.remove();
            depth++;
            if (!visited.contains(current)) {

                if (!(depth == 1) && start.equals(goal)) {
                    visited.add(current);
                }

                final Set<Edge> edges = current.getEdges();

                for (final Edge edge : edges) {

                    final Node neighbor = edge.getEnd();
                    if (!visited.contains(neighbor)) {

                        final int currentDistance = edge.getWeight() + current.getDistance();

                        if (currentDistance < neighbor.getDistance() || neighbor.getDistance() == 0) {
                            parentMap.put(neighbor, current);
                            neighbor.setDistance(currentDistance);
                            priorityQueue.add(neighbor);
                        }
                    }
                }
            }
        }

        return vertices.get(goal).getDistance();
    }

    public int getDistance(final String[] nodeKeys) {
        int distance = 0;
        int size = 0;
        for (int i = 0; i < nodeKeys.length - 1; i++) {

            if (vertices.containsKey(nodeKeys[i])) {
                final Set<Edge> edges = vertices.get(nodeKeys[i]).getEdges();

                for (Edge edge : edges) {
                    if (edge.getEnd().getLabel().equals(nodeKeys[i + 1])) {
                        distance += edge.getWeight();
                        size++;
                    }
                }
            } else {
                throw new RuntimeException(NO_SUCH_ROUTE_ERROR_MESSAGE);
            }
        }

        if (size != nodeKeys.length - 1) {
            throw new RuntimeException(NO_SUCH_ROUTE_ERROR_MESSAGE);
        }

        return distance;
    }
}

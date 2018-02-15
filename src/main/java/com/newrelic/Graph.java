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

    public void addEdge(final String start, final String end, final int weight) throws IllegalArgumentException {

        final Node startNode = validate(start);
        final Node endNode = validate(end);

        final Edge edge = new Edge(startNode, endNode, weight);
        edges.add(edge);
        startNode.addEdge(edge);
    }

    public int getNumberOfTrips(final String start, final String end, final int maxStops) {

        final Node startNode = validate(start);
        final Node endNode = validate(end);

        List<List<Node>> paths = new ArrayList<>();

        recursiveBSF(0, startNode, endNode, paths, new LinkedHashSet<>());

        int trips = 0;
        for (List<Node> path : paths) {
            if (path.size() <= maxStops) {
                trips++;
            }
        }

        return trips;
    }

    public int getAmountOfRoutes(final String start, final String end, final int maxStops) {

        final Node startNode = validate(start);
        final Node endNode = validate(end);

        return amountOfRoutes(startNode, endNode, 0, maxStops);
    }

    public int getShortestRoute(final String start, final String goal) {

        // Initialize data structures
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

    private void recursiveBSF(int depth, final Node startNode, final Node endNode, final List<List<Node>> paths, final LinkedHashSet<Node> path) {
        path.add(startNode);

        if (!(depth == 0) && startNode.equals(endNode)) {
            paths.add(new ArrayList<>(path));
            path.remove(startNode);
            return;
        }

        final Set<Node> neighbors = startNode.getNeighbors();

        for (final Node neighbor : neighbors) {
            depth++;
            recursiveBSF(depth, neighbor, endNode, paths, path);
        }

        path.remove(startNode);
    }

    private Node validate(final String nodeLabel) {
        if (nodeLabel == null) {
            throw new IllegalArgumentException("Cannot find route or null node");
        }

        final Node node = vertices.get(nodeLabel);

        if (node == null) {
            throw new IllegalArgumentException("Node:" + nodeLabel + "is not in graph");
        }

        return node;
    }

    private int amountOfRoutes(final Node startNode, final Node endNode, int distance, final int maxStops) {

        final Set<Edge> edges = startNode.getEdges();

        int routes = 0;
        for (final Edge edge : edges) {
            distance += edge.getWeight();

            if (distance < maxStops) {
                if (edge.getEnd().equals(endNode)) {
                    routes++;
                    routes += amountOfRoutes(edge.getEnd(), endNode, distance, maxStops);
                } else {
                    routes += amountOfRoutes(edge.getEnd(), endNode, distance, maxStops);
                    distance -= edge.getWeight();
                }
            }
        }

        return routes;
    }
}
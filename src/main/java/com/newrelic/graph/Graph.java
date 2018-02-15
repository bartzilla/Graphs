package com.newrelic.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 *  Class representing graphs of towns in Nerdland.
 *  Each vertex in the graph is a particular town.
 *
 *  @author Cipriano Sanchez
 */
public class Graph {

    private Map<String, Node> vertices;

    private static final String NO_SUCH_ROUTE_ERROR_MESSAGE = "NO SUCH ROUTE";

    /**
     * Constructor for creating empty Graphs
     */
    public Graph() {
        this.vertices = new HashMap<>();
    }

    /**
     * Add an empty node to the graph with the given label.
     */
    public void addVertex(final String label) {
        final Node n = new Node(label);
        vertices.put(label, n);
    }

    /**
     * Add a directed edge to the graph from start to end.
     * @param start The starting point of the edge
     * @param end The ending point of the edge
     * @throws IllegalArgumentException If the points have not already been
     * added as nodes to the graph. Or if input is invalid e.g. null.
     */
    public void addEdge(final String start, final String end, final int weight) throws IllegalArgumentException {
        final Node startNode = validate(start);
        final Node endNode = validate(end);

        final Edge edge = new Edge(startNode, endNode, weight);
        startNode.addEdge(edge);
    }

    /**
     * Calculates the possible amount of trips between two nodes with maximum stops.
     * @param start The starting node of the trip
     * @param end The ending node of the trip
     * @param maxStops The maximum amount of stops
     * @throws IllegalArgumentException If  <code>start</code> or <code>end</code> are invalid inputs or
     * do not exist in the graph.
     */
    public int getNumberOfTrips(final String start, final String end, final int maxStops) {
        final Node startNode = validate(start);
        final Node endNode = validate(end);

        List<List<Node>> paths = new ArrayList<>();

        recursiveBSF(0, startNode, endNode, paths, new LinkedHashSet<>());

        int trips = 0;

        // get all paths with less than maximum amount of stops
        for (List<Node> path : paths) {
            if (path.size() <= maxStops) {
                trips++;
            }
        }

        // return the computed amount of trips
        return trips;
    }

    /**
     * Returns the number of possible routes between two nodes within up to a maximum of the provided distance
     * @param start The starting node of the trip
     * @param end The ending node of the trip
     * @throws IllegalArgumentException If  <code>start</code> or <code>end</code> are invalid inputs or
     * do not exist in the graph.
     */
    public int getAmountOfRoutes(final String start, final String end, final int maxDistance) {

        // Validate input and get node objects for given keys.
        final Node startNode = validate(start);
        final Node endNode = validate(end);

        return amountOfRoutes(startNode, endNode, 0, maxDistance);
    }

    /**
     * Calculates the shortest possible route (in terms of distance) between two nodes. This is an implementation of
     * dijkstra's algorithm.
     * @param start The starting node of the trip
     * @param end The ending node of the trip
     * @throws IllegalArgumentException If  <code>start</code> or <code>end</code> are invalid inputs or
     * do not exist in the graph.
     */
    public int getShortestRoute(final String start, final String end) {

        // Initialize priority queue and visited set.
        final PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        final Set<Node> visited = new HashSet<>();

        // Initialize all distances between nodes to infinity.
        vertices.values().forEach(n -> n.setDistance((int) Double.POSITIVE_INFINITY));

        final Node startNode = vertices.get(start);

        // Enqueue the first node.
        startNode.setDistance(0);
        priorityQueue.add(startNode);
        int depth = 0;

        while (!priorityQueue.isEmpty()) {
            final Node current = priorityQueue.remove();
            depth++;

            // If current node is not in the queue then it hasn't been evaluated, then compute..
            if (!visited.contains(current)) {

                // Collect the visited nodes, unless start and end are equal in the first iteration.
                if (!(depth == 1) && start.equals(end)) {
                    visited.add(current);
                }

                final Set<Edge> edges = current.getEdges();

                // For current node's edges calculate distances
                for (final Edge edge : edges) {

                    final Node neighbor = edge.getEnd();
                    if (!visited.contains(neighbor)) {

                        final int currentDistance = edge.getWeight() + current.getDistance();

                        // If path through current node is shorter update distance.
                        if (currentDistance < neighbor.getDistance() || neighbor.getDistance() == 0) {
                            neighbor.setDistance(currentDistance);
                            priorityQueue.add(neighbor);
                        }
                    }
                }
            }
        }

        // Each vertex will contain the shortest distances from start.
        // Now retrieve the one for the required node.
        return vertices.get(end).getDistance();
    }

    /*
     * Obtains the total distance between a sequence of key nodes representing a route.
     * e.g. {A, B, C}
     * @param nodeKeys Array containing the keys of the desired route
     * @throws RuntimeException If the route does not exist in the graph.
     */
    public int getDistance(final String[] nodeKeys) {
        int distance = 0;
        int nodesVisited = 0;

        // Traverse each required key of the path to check if it exists as vertex in the graph.
        for (int i = 0; i < nodeKeys.length - 1; i++) {
            if (vertices.containsKey(nodeKeys[i])) {

                final Set<Edge> edges = vertices.get(nodeKeys[i]).getEdges();

                // Check if the next key path is at the end of the current edge.
                // If it is, then move and increment the distance until final destination has been reached.
                for (final Edge edge : edges) {
                    if (edge.getEnd().getLabel().equals(nodeKeys[i + 1])) {
                        distance += edge.getWeight();
                        nodesVisited++;
                    }
                }
            } else {
                throw new RuntimeException(NO_SUCH_ROUTE_ERROR_MESSAGE);
            }
        }

        // Nodes visited should match amount of keys, otherwise one was missing
        if (nodesVisited != nodeKeys.length - 1) {
            throw new RuntimeException(NO_SUCH_ROUTE_ERROR_MESSAGE);
        }

        return distance;
    }

    private void recursiveBSF(int depth, final Node startNode, final Node endNode, final List<List<Node>> paths, final LinkedHashSet<Node> path) {
        path.add(startNode);

        // In case start and end are the same in the first iteration do not include the path,
        // it is necessary to traverse further
        if (!(depth == 0) && startNode.equals(endNode)) {
            // update new path
            paths.add(new ArrayList<>(path));
            path.remove(startNode);

            //step out of recursion and continue with next node
            return;
        }

        final Set<Node> neighbors = startNode.getNeighbors();

        // Per neighbor of a current node start traverse further until recursiveBSF finds the endNode,
        for (final Node neighbor : neighbors) {
            depth++;
            recursiveBSF(depth, neighbor, endNode, paths, path);
        }

        path.remove(startNode);
    }

    private int amountOfRoutes(final Node startNode, final Node endNode, int distance, final int maxDistance) {
        final Set<Edge> edges = startNode.getEdges();

        int amount = 0;

        for (final Edge edge : edges) {
            distance += edge.getWeight();

            // traverse and add distance as long as the count is less than desired maximum.
            if (distance < maxDistance) {
                if (edge.getEnd().equals(endNode)) {
                    amount++;
                    amount += amountOfRoutes(edge.getEnd(), endNode, distance, maxDistance);
                } else {
                    amount += amountOfRoutes(edge.getEnd(), endNode, distance, maxDistance);
                    distance -= edge.getWeight();
                }
            }
        }

        return amount;
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
}
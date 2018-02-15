package com.newrelic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphTest {

    private Graph graph;

    @BeforeEach
    void myFirstTest() {

        graph = new Graph();

        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");

        graph.addEdge("A", "B", 5);
        graph.addEdge("A", "E", 7);
        graph.addEdge("A", "D", 5);

        graph.addEdge("B", "C", 4);

        graph.addEdge("C", "E", 2);
        graph.addEdge("C", "D", 8);

        graph.addEdge("D", "C", 8);
        graph.addEdge("D", "E", 6);

        graph.addEdge("E", "B", 3);
    }

    @Test
    void calculateDistanceABC() {
        final String[] nodeKeys = {"A", "B", "C"};
        assertEquals(9, graph.getDistance(nodeKeys));
    }

    @Test
    void calculateDistanceAD() {
        final String[] nodeKeys = {"A", "D"};
        assertEquals(5, graph.getDistance(nodeKeys));
    }

    @Test
    void calculateDistanceADC() {
        final String[] nodeKeys = {"A", "D", "C"};
        assertEquals(13, graph.getDistance(nodeKeys));
    }

    @Test
    void calculateDistanceAEBCD() {
        final String[] nodeKeys = {"A", "E", "B", "C", "D"};
        assertEquals(22, graph.getDistance(nodeKeys));
    }

    @Test
    void calculateDistance_AED() {
        assertThrows(RuntimeException.class, () -> {
            final String[] nodeKeys = {"A", "E", "D"};
            graph.getDistance(nodeKeys);
        });
    }

    @Test
    void numberOfTrips_Max_3Stops_CC() {
        assertEquals(2, graph.getNumberOfTrips("C", "C", 3));
    }

    @Test
    void numberOfTrips_Max_4Stops_AC() {
        assertEquals(3, graph.getNumberOfTrips("A", "C", 4));
    }

    @Test
    void shortestRouteLengthAC() {
        assertEquals(9, graph.getShortestRoute("A", "C"));
    }

    @Test
    void shortestRouteLengthBB() {
        assertEquals(9, graph.getShortestRoute("B", "B"));
    }

    @Test
    void numberOfRoutesDistanceLessThan30() {
        assertEquals(7, graph.getAmountOfRoutes("C", "C", 30));
    }
}

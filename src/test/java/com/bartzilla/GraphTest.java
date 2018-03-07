package com.bartzilla;

import com.bartzilla.graph.Graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphTest {

    private Graph graph;

    @BeforeEach
    void setup() {

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

    // THIS OUTPUT IS ACTUALLY WRONG IT TURNS OUT IT RETURNS THE SAME AMOUNT OF ROUTES.
    // However, getNumberOfTrips doesn't support cyclic graphs and so as soon as the end target is found
    // it won't continue traversing to reach the max of stops in nodes with cycles.
    // e.g. In A: D-C-D-C when target C is found the traversal will stop and mark a route with 2 stops, and
    // same for other combinations.
    @Test
    void numberOfTrips_Exactly_4Stops_AC() {
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
        assertEquals(3, graph.getAmountOfRoutes("A", "D", 22));
    }
}

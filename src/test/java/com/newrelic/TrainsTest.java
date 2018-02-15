package com.newrelic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrainsTest {

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
    public void calculateDistanceABC() {
        final String[] nodeKeys = {"A", "B", "C"};
        assertEquals(9, graph.getDistance(nodeKeys));
    }

    @Test
    public void calculateDistanceAD() {
        final String[] nodeKeys = {"A", "D"};
        assertEquals(5, graph.getDistance(nodeKeys));
    }

    @Test
    public void calculateDistanceADC() {
        final String[] nodeKeys = {"A", "D", "C"};
        assertEquals(13, graph.getDistance(nodeKeys));
    }

    @Test
    public void calculateDistanceAEBCD() {
        final String[] nodeKeys = {"A", "E", "B", "C", "D"};
        assertEquals(22, graph.getDistance(nodeKeys));
    }

    @Test
    public void calculateDistanceAED() {
        assertThrows(RuntimeException.class, ()-> {
                    final String[] nodeKeys = {"A", "E", "D"};
                    graph.getDistance(nodeKeys);
                });
    }
}

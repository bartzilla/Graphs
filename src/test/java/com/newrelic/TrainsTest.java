package com.newrelic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainsTest {

    @Test
    void myFirstTest() {

        MapGraph mapGraph = new MapGraph();

        mapGraph.addVertex("A");
        mapGraph.addVertex("B");
        mapGraph.addVertex("C");
        mapGraph.addVertex("D");
        mapGraph.addVertex("E");

        mapGraph.addEdge("A", "B", 5);
        mapGraph.addEdge("A", "E", 7);
        mapGraph.addEdge("A", "D", 5);

        mapGraph.addEdge("B", "C", 4);

        mapGraph.addEdge("C", "E", 2);
        mapGraph.addEdge("C", "D", 8);

        mapGraph.addEdge("D", "C", 8);
        mapGraph.addEdge("D", "E", 6);

        mapGraph.addEdge("E", "B", 3);

        String[] nodeKeys = {"A", "B", "C"};
        assertEquals(9, mapGraph.getDistance(nodeKeys));

        String[] nodeKeys2 = {"A", "D", };
        assertEquals(5, mapGraph.getDistance(nodeKeys2));

        String[] nodeKeys3 = {"A", "D", "C"};
        assertEquals(13, mapGraph.getDistance(nodeKeys3));

        String[] nodeKeys4 = {"A", "E", "B", "C", "D"};
        assertEquals(22, mapGraph.getDistance(nodeKeys4));

//        String[] nodeKeys5 = {"A", "E", "D"};
//        assertEquals(22, mapGraph.getDistance(nodeKeys5));

//        String[] nodeKeys6 = {"C", "E", "C"};
//        assertEquals(22, mapGraph.getDistance(nodeKeys6));
    }
}

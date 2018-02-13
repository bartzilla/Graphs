package com.newrelic;

public class HarnessTest {

    public static void main(String[] args) {

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

        System.out.println(mapGraph.getVertices());
    }
}

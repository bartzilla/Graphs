package com.newrelic.server;

import com.newrelic.graph.Graph;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *  Class representing a mock server to access the application remotely
 *
 *  @author Cipriano Sanchez
 */
public class Server {

    private final Graph graph = new Graph();

    public static void main(String[] args) throws IOException {

        Server server = new Server();
        server.loadData();

        Map<Integer, String> options = Server.getOptions();

        ServerSocket serverSocket = new ServerSocket(12345);

        Socket client  = serverSocket.accept();

        PrintWriter out = new PrintWriter(client.getOutputStream());
        // Info message for the server
        System.out.println("[LOG] Server running on: localhost:12345");

        // Info message for the client
        out.println("Server running on: localhost:12345");
        out.println("----------------------------------------------------------------");
        out.println(" Welcome to Nerdland's Train System");
        out.println("----------------------------------------------------------------");
        out.println("----------------------------------------------------------------");

        options.forEach((k, v) -> out.println("[" + k + "] - " + v ));

        out.print("Chose your option: ");
        out.flush();

        Scanner scanner = new Scanner(client.getInputStream());

        scanner.hasNextLine();
        String line = scanner.nextLine();
        System.out.println("[LOG] User's option was: " + line);
        int result = server.routeOption(Integer.parseInt(line));
        System.out.println("[LOG] Result for option is: " + result);


        out.println("Your serlected option is: " + line);
        out.println("Your result: " + result);
        out.println("----------------------------------------------------------------");
        out.println(" Thanks for using our service!");
        out.println("----------------------------------------------------------------");

        out.flush();
        out.close();
    }

    private int routeOption(final int option) {

        int result = 0;
        switch (option) {
            case 1:
                result = graph.getDistance(new String[]{"A", "B", "C"});
                break;
            case 2:
                result = graph.getDistance(new String[]{"A", "D"});
                break;
            case 3:
                result = graph.getDistance(new String[]{"A", "D", "C"});
                break;
            case 4:
                result = graph.getDistance(new String[]{"A", "E", "B", "C", "D"});
                break;
            case 5:
                result = graph.getDistance(new String[]{"A", "E", "D"});
                break;
            case 6:
                result = graph.getNumberOfTrips("C", "C", 3);
                break;
            case 7:
                result = graph.getNumberOfTrips("A", "C", 4);
                break;
            case 8:
                result = graph.getShortestRoute("A", "C");
                break;
            case 9:
                result = graph.getShortestRoute("B", "B");
                break;
            case 10:
                result = graph.getAmountOfRoutes("C", "C", 30);
                break;
            default:
                throw new RuntimeException("Unknown Option!");
        }

        return result;
    }

    private void loadData() {

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

    private static Map<Integer, String> getOptions() {
        final Map<Integer, String> options = new HashMap<>();

        options.put(1, "calculateDistanceABC");
        options.put(2, "calculateDistanceAD");
        options.put(3, "calculateDistanceADC");
        options.put(4, "calculateDistanceAEBCD");
        options.put(5, "calculateDistance_AED");
        options.put(6, "numberOfTrips_Max_3Stops_CC");
        options.put(7, "numberOfTrips_Max_4Stops_AC");
        options.put(8, "shortestRouteLengthAC");
        options.put(9, "shortestRouteLengthBB");
        options.put(10, "numberOfRoutesDistanceLessThan30");

        return options;
    }
}

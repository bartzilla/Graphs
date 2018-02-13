package com.newrelic;

public class MapEdge {

    private int weight;
    private MapNode start;
    private MapNode end;

    MapEdge(MapNode n1, MapNode n2, int weight)
    {
        this.start = n1;
        this.end = n2;
        this.weight = weight;
    }

    /**
     * Given one of the nodes involved in this edge, get the other one
     * @param node The node on one side of this edge
     * @return the other node involved in this edge
     */
    MapNode getOtherNode(MapNode node)
    {
        if (node.equals(start))
            return end;
        else if (node.equals(end))
            return start;
        throw new IllegalArgumentException("Looking for " +
                "a point that is not in the edge");
    }

    public MapNode getStart() {
        return start;
    }

    public void setStart(MapNode start) {
        this.start = start;
    }

    public MapNode getEnd() {
        return end;
    }

    public void setEnd(MapNode end) {
        this.end = end;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

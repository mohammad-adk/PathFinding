package models;

public class Edge {
    private Node source;
    private Node destination;
    private int weight1_2 = 1;
    private int weight2_1 = Integer.MAX_VALUE;

    public Edge(Node source, Node destination){
        this.source = source;
        this.destination = destination;
    }

    public Node getSourceNode(){
        return source;
    }

    public Node getDestinationNode(){
        return destination;
    }

    public void setWeight(int weight1, int weight2){
        this.weight1_2 = weight1;
        this.weight2_1 = weight2;
    }

    public int getWeight(Node source){
        if(this.source == source)
            return weight1_2;
        else
            return weight2_1;
    }
    
    public int getWeight(int source){
        if(source == 1)
            return weight1_2;
        else
            return weight2_1;
    }

    public boolean hasNode(Node node){
        return source==node || destination==node;
    }

    public boolean equals(Edge edge) {
        return (source ==edge.source && destination ==edge.destination) || (source ==edge.destination && destination ==edge.source) ;
    }
    
    public String toString(int source) {
        if(source == 1)
            return getSourceNode().getId() + " - "
                   + getDestinationNode().getId();
        else
            return getDestinationNode().getId() + " - "
                   + getSourceNode().getId();
    }
}

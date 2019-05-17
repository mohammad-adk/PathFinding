package models;

public class Edge {
    private Node one;
    private Node two;
    private int weight1_2 = 1;
    private int weight2_1 = Integer.MAX_VALUE;

    public Edge(Node one, Node two){
        this.one = one;
        this.two = two;
    }

    public Node getNodeOne(){
        return one;
    }

    public Node getNodeTwo(){
        return two;
    }

    public void setWeight(int weight1, int weight2){
        this.weight1_2 = weight1;
        this.weight2_1 = weight2;
    }

    public int getWeight(Node node){
        if(one == node)
            return weight1_2;
        else
            return weight2_1;
    }
    
    public int getWeight(int in){
        if(in == 1)
            return weight1_2;
        else
            return weight2_1;
    }

    public boolean hasNode(Node node){
        return one==node || two==node;
    }

    public boolean equals(Edge edge) {
        return (one ==edge.one && two ==edge.two) || (one ==edge.two && two ==edge.one) ;
    }

//    @Override
//    public String toString() {
//        return "Edge ~ "
//                + getNodeOne().getId() + " - "
//                + getNodeTwo().getId();
//    }
    
    public String toString(int path) {
        if(path == 1)
            return "Edge ~ "
                    + getNodeOne().getId() + " - "
                    + getNodeTwo().getId();
        else
            return "Edge ~ "
                    + getNodeTwo().getId() + " - "
                    + getNodeOne().getId();
    }
}

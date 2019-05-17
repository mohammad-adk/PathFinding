package algo;

import models.Edge;
import models.Graph;
import models.Node;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DijkstraAlgorithm {
    private boolean safe = false;
    private String message = null;

    private Graph graph;
    private Map<Node, Node> predecessors;
    private Map<Node, Integer> distances;

    private PriorityQueue<Node> unvisited;
    private HashSet<Node> visited;

    public class NodeComparator implements Comparator<Node>  {
        @Override
        public int compare(Node node1, Node node2) {
            return distances.get(node1) - distances.get(node2);
        }
    };

    public DijkstraAlgorithm(Graph graph){
        this.graph = graph;
        predecessors = new HashMap<>();
        distances = new HashMap<>();

        for(Node node : graph.getNodes()){
            distances.put(node, Integer.MAX_VALUE);
        }
        visited = new HashSet<>();

        safe = evaluate();
    }

    private boolean evaluate(){
        if(graph.getSource()==null){
            message = "Source must be present in the graph";
            return false;
        }

        if(graph.getDestination()==null){
            message = "Destination must be present in the graph";
            return false;
        }

        for(Node node : graph.getNodes()){
            if(!graph.isNodeReachable(node)){
                message = "Graph contains unreachable nodes";
                return false;
            }
        }

        return true;
    }

    public void run() throws IllegalStateException {
        if(!safe) {
            throw new IllegalStateException(message);
        }

        unvisited = new PriorityQueue<>(graph.getNodes().size(), new NodeComparator());

        Node source = graph.getSource();
        distances.put(source, 0);
        visited.add(source);

        try {
            for (Edge neighbor : getNeighbors(source)){
                Node adjacent = getAdjacent(neighbor, source);
                if(adjacent==null)
                    continue;
                
                distances.put(adjacent, neighbor.getWeight(source));
                predecessors.put(adjacent, source);
                unvisited.add(adjacent);
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }

        while (!unvisited.isEmpty()){
            Node current = unvisited.poll();

            updateDistance(current);

            unvisited.remove(current);
            visited.add(current);
        }

        for(Node node : graph.getNodes()) {
            node.setPath(getPath(node));
        }

        graph.setSolved(true);
        
    }

    private void updateDistance(Node node){
        int distance = distances.get(node);

        try {
            for (Edge neighbor : getNeighbors(node)){
                Node adjacent = getAdjacent(neighbor, node);
                if(visited.contains(adjacent))
                    continue;
                
                int current_dist = distances.get(adjacent);
                int new_dist = distance + neighbor.getWeight(node);
                
                if(new_dist < current_dist) {
                    distances.put(adjacent, new_dist);
                    predecessors.put(adjacent, node);
                    unvisited.add(adjacent);
                }
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    private Node getAdjacent(Edge edge, Node node) {
        if(edge.getNodeOne()!=node && edge.getNodeTwo()!=node)
            return null;

        return node==edge.getNodeTwo()?edge.getNodeOne():edge.getNodeTwo();
    }

    private List<Edge> getNeighbors(Node node) throws Exception {
        List<Edge> neighbors = new ArrayList<>();

        for(Edge edge : graph.getEdges()){
            System.out.println(edge.getWeight(2));
            if((edge.getNodeOne()==node || edge.getNodeTwo()==node))
                {       neighbors.add(edge);
                }
            else{ 
                throw new Exception("There's no path from source to destination");
                }
        }
        

        return neighbors;
    }

    public Integer getDestinationDistance(){
        return distances.get(graph.getDestination());
    }

    public Integer getDistance(Node node){
        return distances.get(node);
    }

    public List<Node> getDestinationPath() {
        return getPath(graph.getDestination());
    }

    public List<Node> getPath(Node node){
        List<Node> path = new ArrayList<>();

        Node current = node;
        path.add(current);
        while (current!=graph.getSource()){
            current = predecessors.get(current);
            path.add(current);
        }

        Collections.reverse(path);

        return path;
    }

}

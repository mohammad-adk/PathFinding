package algo;

import java.util.*;
import models.*;

public class DPAlgorithmTSP {
  
    private final int N;
    private final int START_NODE;
    private final int FINISHED_STATE;
    
    private boolean safe = false;
    private String message = null;

    private UndirectedGraph graph;
    private int[][] distance;
    private Map<Integer, Node> nodes;
    private double minTourCost = Double.POSITIVE_INFINITY;

    private List<Node> path = new ArrayList<>();
    private boolean ranSolver = false;

    public DPAlgorithmTSP(UndirectedGraph graph) {
        this(graph.getSource(), graph);
    }

    public DPAlgorithmTSP(Node startNode, UndirectedGraph graph) {
        this.graph = graph;
        N = graph.getNodes().size();
        Map<Node, Integer> tempNodes = new HashMap<>();
        nodes = new HashMap<>();
        distance = new int[N][N];
        for(int[] row : distance) java.util.Arrays.fill(row, Integer.MAX_VALUE);
        int i = 0; 
        for(Node node : graph.getNodes()){
            tempNodes.put(node, i);
            nodes.put(i, node);
            i++;
        }
        
        for(Edge edge : graph.getEdges()){
            i = tempNodes.get(edge.getSourceNode());
            int j = tempNodes.get(edge.getDestinationNode());
            distance[i][j] = distance[j][i] = edge.getWeight();
        }
        
        START_NODE = tempNodes.get(startNode);

        // Validate inputs.
        safe = evaluate(graph);

        // The finished state is when the finished state mask has all bits are set to
        // one (meaning all the tempNodes have been visited).
        FINISHED_STATE = (1 << N) - 1;
    }
    
    private boolean evaluate(UndirectedGraph graph){
        if(N <= 2){
            message = "TSP on 0, 1 or 2 nodes doesn't make sense.";
            return false;
        }

        if(N > 24){
            message = "Graph too large! A graph that size for the DP TSP problem with a time complexity of O(n^2*2^n) requires way too much computation for any modern home computer to handle";
            return false;
        }

        for(Node node : graph.getNodes()){
            if(!graph.isNodeReachable(node)){
                message = "Graph contains unreachable nodes";
                return false;
            }
        }
        
        int[][] temp = new int[distance.length][distance.length];
        for(int i = 0; i< distance.length; i++){
            for(int j = 0; j< distance.length; j++){
                temp[i][j] = distance[i][j] == Integer.MAX_VALUE ? 0 : distance[i][j];
            }
        }
        if(!hamCycle(temp)){
            message = "This graph doesn't have any hamiltonian cycle";
            return false;
        }

        return true;
    }
    
    boolean isSafe(int v, int graph[][], int path[], int pos) 
    { 
        /* Check if this vertex is an adjacent vertex of 
           the previously added vertex. */
        if (graph[path[pos - 1]][v] == 0) 
            return false; 
  
        /* Check if the vertex has already been included. 
           This step can be optimized by creating an array 
           of size graph.length */
        for (int i = 0; i < pos; i++) 
            if (path[i] == v) 
                return false; 
  
        return true; 
    } 
    
    boolean hamCycleUtil(int graph[][], int path[], int pos) 
    { 
        int v = graph.length;
        /* base case: If all vertices are included in 
           Hamiltonian Cycle */
        if (pos == v) 
        { 
            // And if there is an edge from the last included 
            // vertex to the first vertex 
            if (graph[path[pos - 1]][path[0]] == 1)
                return true;
            else
                return false;
        } 
  
        // Try different vertices as a next candidate in 
        // Hamiltonian Cycle. We don't try for 0 as we 
        // included 0 as starting point in in hamCycle() 
        for (int i = 1; i < v; i++) 
        { 
            /* Check if this vertex can be added to Hamiltonian 
               Cycle */
            if (isSafe(i, graph, path, pos)) 
            { 
                path[pos] = i; 
  
                /* recur to construct rest of the path */
                if (hamCycleUtil(graph, path, pos + 1) == true) 
                    return true; 
  
                /* If adding vertex i doesn't lead to a solution, 
                   then remove it */
                path[pos] = -1; 
            } 
        } 
  
        /* If no vertex can be added to Hamiltonian Cycle 
           constructed so far, then return false */
        return false; 
    } 
  
    // Solves the Hamiltonian Cycle problem using Backtracking
    boolean hamCycle(int graph[][]) 
    { 
        int v = graph.length;
        int[] path = new int[v]; 
        for (int i = 0; i < v; i++) 
            path[i] = -1; 
  
        /* Let us put vertex 0 as the first vertex in the path. 
           If there is a Hamiltonian Cycle, then the path can be 
           started from any point of the cycle as the graph is 
           undirected */
        path[0] = 0; 
        if (hamCycleUtil(graph, path, 1) == false) 
        { 
            return false; 
        } 
  
        return true; 
    } 

    // Returns the optimal tour for the traveling salesman problem.
    public List<Node> getPath(){
        if (!ranSolver) run();
        return path;
    }

    // Returns the minimal tour cost.
    public double getTourCost() {
        if (!ranSolver) run();
        return minTourCost;
    }
    

    public void run() {
        if(!safe) {
            throw new IllegalStateException(message);
        }
        
        // Run the solver    
        int state = 1 << START_NODE;
        Double[][] memo = new Double[N][1 << N];
        Integer[][] prev = new Integer[N][1 << N];
        minTourCost = tsp(START_NODE, state, memo, prev);

        // Regenerate path
        int index = START_NODE;
        while (true) {
            path.add(nodes.get(index));
            Integer nextIndex = prev[index][state];
            if (nextIndex == null) break;
            int nextState = state | (1 << nextIndex);
            state = nextState;
            index = nextIndex;
        }
        path.add(nodes.get(START_NODE));
        ranSolver = true;
        graph.setSolved(true);
    }

    private double tsp(int i, int state, Double[][] memo, Integer[][] prev) {
        // Done this tour. Return cost of going back to start node.
        if (state == FINISHED_STATE) return distance[i][START_NODE];

        // Return cached answer if already computed.
        if (memo[i][state] != null) return memo[i][state];

        double minCost = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int next = 0; next < N; next++) {
            // Skip if the next node has already been visited.
            if ((state & (1 << next)) != 0) continue;

            int nextState = state | (1 << next);
            double newCost = distance[i][next] + tsp(next, nextState, memo, prev);
            if (newCost < minCost) {
                minCost = newCost;
                index = next;
            }
        }

        prev[i][state] = index;
        return memo[i][state] = minCost;
    }
}
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class B08_03 {
    
    static class UndirectedGraph {
        private HashMap<Integer, HashSet<Integer>> adjacencyList;
        
        public UndirectedGraph() {
            adjacencyList = new HashMap<>();
        }
        
        public void addVertex(int vertex) {
            adjacencyList.putIfAbsent(vertex, new HashSet<>());
        }
        
        public void removeVertex(int vertex) {
            if (!adjacencyList.containsKey(vertex)) {
                return;
            }
            
            for (int neighbor : adjacencyList.get(vertex)) {
                adjacencyList.get(neighbor).remove(vertex);
            }
            
            adjacencyList.remove(vertex);
        }
        
        public void addEdge(int vertex1, int vertex2) {
            addVertex(vertex1);
            addVertex(vertex2);
            
            adjacencyList.get(vertex1).add(vertex2);
            adjacencyList.get(vertex2).add(vertex1);
        }
        
        public void removeEdge(int vertex1, int vertex2) {
            if (adjacencyList.containsKey(vertex1)) {
                adjacencyList.get(vertex1).remove(vertex2);
            }
            if (adjacencyList.containsKey(vertex2)) {
                adjacencyList.get(vertex2).remove(vertex1);
            }
        }
        
        public Set<Integer> getNeighbors(int vertex) {
            return adjacencyList.getOrDefault(vertex, new HashSet<>());
        }
        
        public boolean hasVertex(int vertex) {
            return adjacencyList.containsKey(vertex);
        }
        
        public boolean hasEdge(int vertex1, int vertex2) {
            return adjacencyList.containsKey(vertex1) && 
                   adjacencyList.get(vertex1).contains(vertex2);
        }
        
        public void printGraph() {
            for (int vertex : adjacencyList.keySet()) {
                System.out.print(vertex + " -> ");
                System.out.println(adjacencyList.get(vertex));
            }
        }
    }
    
    public static void main(String[] args) {
        UndirectedGraph graph = new UndirectedGraph();
        
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        
        System.out.println("Graph after adding vertices and edges:");
        graph.printGraph();
        
        System.out.println("\nRemoving edge (1, 3):");
        graph.removeEdge(1, 3);
        graph.printGraph();
        
        System.out.println("\nRemoving vertex 4:");
        graph.removeVertex(4);
        graph.printGraph();
        
        System.out.println("\nNeighbors of vertex 2: " + graph.getNeighbors(2));
    }
}


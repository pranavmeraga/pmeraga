package helper;

import java.util.*;

public class Graph {
    private Map<Integer, Set<Integer>> graph;
    public Graph() {
        graph = new HashMap<>();
    }

    public void addNode(Integer node) {
        if (!graph.containsKey(node)) {
            graph.put(node, new HashSet<>());
        }
    }

    public boolean containsKey(Integer node) {
        return graph.containsKey(node);
    }

    public void addEdge(Integer id, Integer edge) {
        addNode(id);
        addNode(edge);
        graph.get(id).add(edge);
    }

    public void addNeighbor(Integer id, Set<Integer> n) {
        if (graph.get(id) != null) {
            graph.put(id, n);
        } else {
            for (Integer w : n) {
                graph.get(id).add(w);
            }
        }
    }

    public Set<Integer> getEdges() {
        return graph.keySet();
    }

    public Set<Integer> getNeighbors(Integer id) {
        return graph.getOrDefault(id, new HashSet<>());
    }
}

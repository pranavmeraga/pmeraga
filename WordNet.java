package helper;

import browser.NgordnetQueryType;
import edu.princeton.cs.algs4.In;

import java.util.*;

public class WordNet {
    private Graph graph;

    private Map<Integer, Set<String>> synsetId;

    private Map<Integer, Set<Integer>> hyponymRelations;

    private Graph ancestors;

    public WordNet(String s, String h) {
        this.graph = new Graph();
        this.hyponymRelations = new HashMap<>();
        this.synsetId = new HashMap<>();
        this.ancestors = new Graph();
        parseSynset(s);
        parseHyponym(h);
        mapSynsetToGraph();
    }

    public void parseSynset(String synsetsFile) {
        In in = new In(synsetsFile);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] split = line.split(",");
            String id = split[0];
            int intId = Integer.parseInt(id);
            String[] words = split[1].split(" ");
            Set<String> newWord = new HashSet<>();
            for (String w : words) {
                newWord.add(w);
            }
            synsetId.put(intId, newWord);
            ancestors.addNode(intId);
        }
    }

    public void parseHyponym(String hyponymsFile) {
        In in = new In(hyponymsFile);
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] split = line.split(",");
            String id = split[0];
            int sId = Integer.parseInt(id);
            Set<Integer> hyponyms = hyponymRelations.getOrDefault(sId, new HashSet<>());
            for (int i = 1; i < split.length; i++) {
                hyponyms.add(Integer.parseInt(split[i]));
                ancestors.addEdge(Integer.parseInt(split[i]), sId);
            }
            hyponymRelations.put(sId, hyponyms);
        }

    }

    public Set<String> makeHyponym(String word, NgordnetQueryType type) {
        if (type == NgordnetQueryType.ANCESTORS) {
            Set<String> ancestor = new HashSet<>();
            Set<Integer> initialIds = getID(word);
            Set<Integer> visitedIds = new HashSet<>(initialIds);
            Queue<Integer> queue = new LinkedList<>(initialIds);

            while (!queue.isEmpty()) {
                int currentId = queue.poll();
                ancestor.addAll(synsetId.getOrDefault(currentId, Collections.emptySet()));
                for (int ancestorId : ancestors.getNeighbors(currentId)) {
                    if (visitedIds.add(ancestorId)) {
                        queue.add(ancestorId);
                    }
                }
            }

            return ancestor;
        } else {
            Set<String> hyponyms = new HashSet<>();
            Set<Integer> initialIds = getID(word);
            Set<Integer> visitedIds = new HashSet<>(initialIds);

            // https://stackoverflow.com/questions/1963806/is-there-a-fixed-sized-queue-which-removes-excessive-elements
            // I used this resource after I was running into runtime errors with my code
            // I chose to use a Breadth-First Search approach to optimize runtime
            Queue<Integer> queue = new LinkedList<>(initialIds);

            while (!queue.isEmpty()) {
                int currentId = queue.poll();
                hyponyms.addAll(synsetId.getOrDefault(currentId, Collections.emptySet()));
                for (int neighborId : graph.getNeighbors(currentId)) {
                    if (visitedIds.add(neighborId)) {
                        queue.offer(neighborId);
                    }
                }
            }

            return hyponyms;
        }
    }

    /*public Set<String> calculateAncestors(String word) {
        Set<Integer> wordId = getID(word);
        Set<Integer> ancestorsIds = new HashSet<>(wordId);
        for (Integer id : wordId) {
            ancestorsIds.addAll(ancestors.getOrDefault(id, Collections.emptySet()));
        }
        return findID(ancestorsIds);
    }*/

    private Set<Integer> getID(String word) {
        Set<Integer> id = new HashSet<>();
        for (Map.Entry<Integer, Set<String>> entry : synsetId.entrySet()) {
            Set<String> words = entry.getValue();
            int syns = entry.getKey();
            if (words.contains(word)) {
                id.add(syns);
            }
        }
        return id;
    }
    public Set<String> findID(Set<Integer> ids) {
        Set<String> idList = new HashSet<>();
        for (Map.Entry<Integer, Set<String>> entry : synsetId.entrySet()) {
            Set<String> words = entry.getValue();
            int syns = entry.getKey();
            for (int y : ids) {
                if (syns == y) {
                    idList.addAll(words);
                }
            }
        }
        return idList;
    }

    public Graph getGraph() {
        return graph;
    }

    private void mapSynsetToGraph() {
        for (Map.Entry<Integer, Set<Integer>> entry : hyponymRelations.entrySet()) {
            Integer id = entry.getKey();
            Set<Integer> hyponyms = entry.getValue();
            if (!graph.containsKey(id)) {
                graph.addNode(id);
                //System.out.println(graph.getNeighbors(id));
            }
            for (Integer hyponym : hyponyms) {
                graph.addEdge(id, hyponym);
                /*Set<Integer> ancestor = ancestors.getOrDefault(hyponym, new HashSet<>());
                ancestor.add(id);
                Set<Integer> parentAncestors = ancestors.getOrDefault(id, new HashSet<>());
                ancestor.addAll(parentAncestors);
                for (String word : synsetId.getOrDefault(id, Collections.emptySet())) {
                    Set<Integer> wordIds = getID(word);
                    ancestor.addAll(wordIds);
                }
                ancestors.put(hyponym, ancestor);*/
            }
        }
    }
}

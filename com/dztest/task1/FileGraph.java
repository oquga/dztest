package com.dztest.task1;

import java.util.*;

public class FileGraph {
    //Graph
    private Map<String, Set<String>> graph = new HashMap<>();
    public void addDependency(String file, String dependency) {
        //TODO:возможно придется убрать computeIfAbsent
        graph.computeIfAbsent(file, k -> new HashSet<>()).add(dependency);
        graph.putIfAbsent(dependency, new HashSet<>());
    }

    public void addIndependentFile(String file) {graph.putIfAbsent(file, new HashSet<>());}
    public Set<String> getFilesOfMap(){
        return graph.keySet();
    }
    public Set<String> getDependencies(String file) {
        return graph.getOrDefault(file, new HashSet<>());
    }
    public void printGraph() {
        for (String file : graph.keySet()) {
            System.out.println(file + " -> " + graph.get(file));
        }
    }


    //Topological sorting
    private Set<String> visited = new HashSet<>();
    private Stack<String> sortStack = new Stack<>();
    private List<String> topoSortedFileList = new ArrayList<>();
    public List<String> getTopoSortedFileList() {
        return topoSortedFileList;
    }

    public boolean topologicalSort() {
        for (String file : getFilesOfMap()) {
            if (!visited.contains(file)) {
                dfsDetectAndSort(file);
            }
        }

        if (isCyclic) return false;


        while (!sortStack.isEmpty()) {
            topoSortedFileList.add(sortStack.pop());
        }
        return true;
    }


    //Cyclic dependency detection
    private Set<String> recStack = new HashSet<>();
    private boolean isCyclic = false;
    private Map<String,List<String>> cyclicFilesChain = new HashMap<>();
    public Map<String, List<String>> getCyclicFilesChain() {
        return cyclicFilesChain;
    }


    private void dfsDetectAndSort(String file) {
        visited.add(file);
        recStack.add(file);

         for (String dependency : getDependencies(file)) {
            if (!visited.contains(dependency)) {  //если ноду не посещали то очевидно что она не может быть в рекурсивном стеке
                dfsDetectAndSort(dependency);
            } else if (recStack.contains(dependency)) {  //если нода была посещена а также была посщена в текущем пути обхода
                isCyclic = true;
                cyclicFilesChain.putIfAbsent(file, Collections.singletonList(dependency));

            }
        }

        recStack.remove(file);
        sortStack.push(file);
    }


}

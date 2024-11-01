package com.dztest.task1;

import java.io.File;
import java.util.*;

public class FileDependencyMap {
    private Map<String, Set<String>> graph = new HashMap<>();


    private Set<String> visited = new HashSet<>();
    private Stack<String> sortStack = new Stack<>();
    private List<String> topoSortedFileList = new ArrayList<>();
    public List<String> getTopoSortedFileList() {
        return topoSortedFileList;
    }




    private Set<String> recStack = new HashSet<>();
    private boolean isCyclic = false;
    //private List<String> cyclicFilesChain = new ArrayList<>();
//    public List<String> getCyclicFilesChain() {
//        return cyclicFilesChain;
//    }
    private Map<String,List<String>> cyclicFilesChain = new HashMap<>();
    public Map<String, List<String>> getCyclicFilesChain() {
        return cyclicFilesChain;
    }

    public void addDependency(String file, String dependency) {

        //check is such file exist in real

        //возможно придется убрать computeIfAbsent
        graph.computeIfAbsent(file, k -> new HashSet<>()).add(dependency);


        graph.putIfAbsent(dependency, new HashSet<>());
    }

    public void addIndependentFile(String file) {
        // Add the file with no dependencies
        // if graph is disconnected It will be presented in alphabetic order
        graph.putIfAbsent(file, new HashSet<>());
    }

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

   // private static void dfs(FileDependencyMap graph, String file, Set<String> visited, Stack<String> stack) {
    private void dfs(String file) {

        visited.add(file);

        for (String dependency : getDependencies(file)) {
            if (!visited.contains(dependency)) {
                //dfs(graph, dependency, visited, sortStack);
                //dfs(graph, dependency);
                dfs(dependency);
            }
        }

        sortStack.push(file);
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
}

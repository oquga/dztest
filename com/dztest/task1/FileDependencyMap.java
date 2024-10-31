package com.dztest.task1;

import java.util.*;

public class FileDependencyMap {
    private Map<String, Set<String>> graph = new HashMap<>();

    public void addDependency(String file, String dependency) {

        //check is such file exist in real

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
}

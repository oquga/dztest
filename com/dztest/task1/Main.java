package com.dztest.task1;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Main {
    public static final String ROOT_DIR = "resources/";

    public static final String CAT_DIR = "cat/";
    public static final String CAT_FILE="cat.txt";

    public static final String CAT_PATH=CAT_DIR+CAT_FILE;

    public static void main(String[] args) {

        Path rootDir = Paths.get(ROOT_DIR);
        FileDependencyMap fileDependencyMap = new FileDependencyMap();
        try {

            FileUtils.scanDirectory(rootDir, fileDependencyMap);

            fileDependencyMap.printGraph();

            List<String> sortedOrder = topologicalSort(fileDependencyMap);
            System.out.println("Topological Sort Order: " + sortedOrder);

            FileUtils.concatenateSortedTextFiles(sortedOrder,CAT_PATH);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //TODO: Check for cyclic dependencies after realization of file concatination
    public static List<String> topologicalSort(FileDependencyMap graph) {
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();

        for (String file : graph.getFilesOfMap()) {
            if (!visited.contains(file)) {
                dfs(graph, file, visited, stack);
            }
        }

        List<String> tSortedFileList = new ArrayList<>();
        while (!stack.isEmpty()) {
            tSortedFileList.add(stack.pop());
        }
        return tSortedFileList;
    }

    private static void dfs(FileDependencyMap graph, String file, Set<String> visited, Stack<String> stack) {
        visited.add(file);

        for (String dependency : graph.getDependencies(file)) {
            if (!visited.contains(dependency)) {
                dfs(graph, dependency, visited, stack);
            }
        }

        stack.push(file);
    }



}

package com.dztest.task1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static final Pattern requirePattern = Pattern.compile("require\\s+'[^']+'");

    public static String ROOT_DIR = "resources";

    public static void main(String[] args) {

        Path rootDir = Paths.get(ROOT_DIR);

        FileDependencyMap fileDependencyMap = new FileDependencyMap();

        try {
            graphAllFiles(rootDir, fileDependencyMap);

            fileDependencyMap.printGraph();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



//        fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 1/File 1-1");
//        fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 2/File 2-1");
//
//        fileDependencyMap.addDependency("Folder 1/File 1-1", "Folder 2/File 2-1");

        //fileDependencyMap.addDependency("Folder 1/File 1-1", "Folder 2/File 2-2"); //make cyclic

       //fileDependencyMap.addIndependentFile("Folder 0/File 0-0");

//        fileDependencyMap.addIndependentFile("Folder 0/File 0-0");
//       // fileDependencyMap.addDependency("Folder 1/File 1-1", "Folder 0/File 0-0");
//        fileDependencyMap.addDependency("Folder 1/File 1-2", "Folder 1/File 1-1");
//        fileDependencyMap.addDependency("Folder 2/File 2-1", "Folder 1/File 1-2");
//        fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 2/File 2-1");
//       // fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 0/File 0-0");
//
//        fileDependencyMap.addDependency("Folder 0/File 0-0", "Folder 2/File 2-2");

//        fileDependencyMap.printGraph();

//        List<String> sortedOrder = topologicalSort(fileDependencyMap);
//        System.out.println("Topological Sort Order: " + sortedOrder);
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

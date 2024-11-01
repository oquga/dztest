package com.dztest.task1;

import javax.activation.MimetypesFileTypeMap;
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
    public static final String ROOT_DIR = "resources/";

    public static void main(String[] args) {

        Path rootDir = Paths.get(ROOT_DIR);
        FileDependencyMap fileDependencyMap = new FileDependencyMap();
        FileUtils fileUtils = new FileUtils();


        try {

            fileUtils.scanDirectory(rootDir, fileDependencyMap);

            fileDependencyMap.printGraph();

            List<String> sortedOrder = topologicalSort(fileDependencyMap);
            System.out.println("Topological Sort Order: " + sortedOrder);
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

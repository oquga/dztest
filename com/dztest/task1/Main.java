package com.dztest.task1;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) {

      //  Path rootPath = Paths.get("resources");

       // List<File> allFiles = new ArrayList<>();

        // listAllFiles(rootPath, allFiles);
        FileDependencyMap fileDependencyMap = new FileDependencyMap();


        fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 1/File 1-1");
        fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 2/File 2-1");

        fileDependencyMap.addDependency("Folder 1/File 1-1", "Folder 2/File 2-1");
        fileDependencyMap.addDependency("Folder 1/File 1-1", "Folder 2/File 2-2");

       //fileDependencyMap.addIndependentFile("Folder 0/File 0-0");

//        fileDependencyMap.addIndependentFile("Folder 0/File 0-0");
//       // fileDependencyMap.addDependency("Folder 1/File 1-1", "Folder 0/File 0-0");
//        fileDependencyMap.addDependency("Folder 1/File 1-2", "Folder 1/File 1-1");
//        fileDependencyMap.addDependency("Folder 2/File 2-1", "Folder 1/File 1-2");
//        fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 2/File 2-1");
//       // fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 0/File 0-0");
//
//        fileDependencyMap.addDependency("Folder 0/File 0-0", "Folder 2/File 2-2");

        fileDependencyMap.printGraph();

        List<String> sortedOrder = topologicalSort(fileDependencyMap);
        System.out.println("Topological Sort Order: " + sortedOrder);
    }

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

    private static void listAllFiles(Path currentPath, List<File> allFiles) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)){
            for (Path i : stream){
                if(Files.isDirectory(i)){
                    listAllFiles(i, allFiles);
                } else {
                    allFiles.add(i.toFile());
                }
            }
        }
    }
}

package com.dztest.task1;


import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

      //  Path rootPath = Paths.get("resources");

       // List<File> allFiles = new ArrayList<>();

        // listAllFiles(rootPath, allFiles);
        FileDependencyMap fileDependencyMap = new FileDependencyMap();


        fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 1/File 1-1");
        fileDependencyMap.addDependency("Folder 2/File 2-2", "Folder 2/File 2-1");

        fileDependencyMap.addDependency("Folder 1/File 1-1", "Folder 2/File 2-1");

        fileDependencyMap.addIndependentFile("Folder 0/File 0-0");

        fileDependencyMap.printGraph();

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

package com.dztest.task1;


import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        Path rootPath = Paths.get("resources");

        List<File> allFiles = new ArrayList<>();
        listAllFiles(rootPath, allFiles);


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

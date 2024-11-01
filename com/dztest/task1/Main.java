package com.dztest.task1;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    public static final String ROOT_DIR = "resources/";

    public static final String CAT_DIR = "cat/";
    public static final String CAT_FILE="cat.txt";

    public static final String CAT_PATH=CAT_DIR+CAT_FILE;

    public static void main(String[] args) {

//        Scanner in = new Scanner(System.in);
//
//        System.out.println("DZ task1 File Dependency Resolver WELCOMES YOU!!");
//
//        System.out.println("Enter root directory path where the files are located please:\n");
//        String rootPath = in.nextLine();
//
//        System.out.println("Enter output directory path where result");



        Path rootDir = Paths.get(ROOT_DIR);
        FileGraph fileGraph = new FileGraph();
        try {

            FileUtils.scanDirectory(rootDir, fileGraph);

            //fileGraph.printGraph();


            if (!fileGraph.topologicalSort()){
                System.out.println("Topological sorting impossible, detected  cyclic dependencies of files:\n" +
                        fileGraph.getCyclicFilesChain());
                return;
            }

            System.out.println("Topological Sort Order: " + fileGraph.getTopoSortedFileList());

            FileCat.concatenateSortedTextFiles(fileGraph.getTopoSortedFileList(),CAT_PATH);


        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

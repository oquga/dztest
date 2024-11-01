package com.dztest.task1;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    public static final String ROOT_DIR = "resources/";

    public static final String CAT_DIR = "cat/";
    public static final String CAT_FILE="cat.txt";

    public static final String CAT_PATH=CAT_DIR+CAT_FILE;

    public static void main(String[] args) {

        Path rootDir = Paths.get(ROOT_DIR);
        FileGraph fileGraph = new FileGraph();
        try {

            FileUtils.scanDirectory(rootDir, fileGraph);

            fileGraph.printGraph();


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

package com.dztest.task1;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    public static final String ROOT_DIR = "inputDir/";
    public static final String CAT_PATH = "outputDir/cat.txt";

    public static void main(String[] args) {
        try {
            FileGraph fileGraph = new FileGraph();
            FileUtils.scanDirectory(Paths.get(ROOT_DIR), fileGraph);

            //fileGraph.printGraph();

            if (!fileGraph.topologicalSort()){
                System.out.println("Topological sorting impossible, detected  cyclic dependencies of files:\n" +
                        fileGraph.getCyclicFilesChain());
                return;
            }

            //System.out.println("Topological Sort Order: " + fileGraph.getTopoSortedFileList());
            //Print Topological order during concat
            FileCat.concatenateSortedTextFiles(fileGraph.getTopoSortedFileList());

            //FileCat.simpleConcatenateFiles(fileGraph.getTopoSortedFileList());

            System.out.println("\n\t\t  FINISH!!!\nTopologically concatenated files in: \""+CAT_PATH+"\"");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

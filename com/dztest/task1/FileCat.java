package com.dztest.task1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.dztest.task1.FileUtils.extractRequiredFilePath;
import static com.dztest.task1.FileUtils.isValidRequireStatement;
import static com.dztest.task1.Main.CAT_PATH;
import static com.dztest.task1.Main.ROOT_DIR;

//Class responsible for concatenation part
public class FileCat {

    public static void simpleConcatenateFiles(List<String> tSortedFiles) throws IOException {
        System.out.println("Topologically sorted list:");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAT_PATH))) {
            for (String fileName : tSortedFiles) {
                System.out.println(fileName);
                try (BufferedReader reader = new BufferedReader(new FileReader(ROOT_DIR+fileName))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        }
    }

    public static void concatenateSortedTextFiles(List<String> tSortedFileList) throws IOException {
        if (tSortedFileList.isEmpty()) {
            System.out.println("No files to concatenate.");
            return;
        }

        System.out.println("Topologically sorted list:");
        copyFile(tSortedFileList.get(0));

        for (int i = 1; i < tSortedFileList.size(); i++) {
            System.out.println(tSortedFileList.get(i));
            replaceLineWithContent(CAT_PATH,tSortedFileList.get(i));
        }

    }

    private static void copyFile(String sourceFile) throws IOException {
        Path sourcePath = Paths.get(ROOT_DIR+sourceFile);
        Path targetPath = Paths.get(CAT_PATH);

        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        System.out.println(sourceFile);
    }

    public static void replaceLineWithContent(String sourceFilePath, String lineToReplace ) throws IOException {
        // Read all lines from the source file
        //String requireLine = "require ‘"+lineToReplace+"’";
        String replacementFilePath = ROOT_DIR+lineToReplace;
        StringBuilder fileContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath))) {
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // Replace specified line with new content if it matches
                if (isValidRequireStatement(currentLine) &&
                        extractRequiredFilePath(currentLine).equals(lineToReplace)) {
                    // Read replacement content from another file
                    // Read whole file
                    String replacementContent = new String(Files.readAllBytes(Paths.get(replacementFilePath)));
                    //and put it into this line of "required"
                    fileContent.append(replacementContent).append(System.lineSeparator());
                } else {
                    // Keep original line
                    fileContent.append(currentLine).append(System.lineSeparator());
                }
            }
        }

        // Write modified content back to the source file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFilePath))) {
            writer.write(fileContent.toString());
        }
    }
}

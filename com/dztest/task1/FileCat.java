package com.dztest.task1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static com.dztest.task1.FileUtils.extractRequiredFilePath;
import static com.dztest.task1.FileUtils.isValidRequireStatement;
import static com.dztest.task1.Main.ROOT_DIR;

//Class responsible for concatenation part
public class FileCat {
    public static void concatenateSortedTextFiles(List<String> tSortedFileList, String outputFile) throws IOException {
        for (int i = 0; i < tSortedFileList.size(); i++) {
            if (i==0){
                copyAndRenameFile(tSortedFileList.get(i), outputFile);
            } else {
                replaceLineWithContent(outputFile,tSortedFileList.get(i));
            }
            System.out.println(tSortedFileList.get(i));
        }
    }

    private static void copyAndRenameFile(String sourceFile, String outputFile) throws IOException {
        Path sourcePath = Paths.get(ROOT_DIR+sourceFile);
        Path targetPath = Paths.get(outputFile);

        // Copy the file
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

        // Optionally rename or move the copied file
        Path renamedPath = Paths.get(outputFile);
        Files.move(targetPath, renamedPath, StandardCopyOption.REPLACE_EXISTING);

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
                    String replacementContent = new String(Files.readAllBytes(Paths.get(replacementFilePath)));
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

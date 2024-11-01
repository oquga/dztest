package com.dztest.task1;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
    private static final String ROOT_DIR = "resources/"; // Define your root directory here
    private static final char OPEN_QUOTE = '‘';
    private static final char CLOSE_QUOTE = '’';
    private static final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();


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

        //System.out.println("File copied and renamed successfully to: " + renamedPath);
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

    public static void scanDirectory(Path currentPath, FileDependencyMap fileDependencyMap) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)){
            for (Path path : stream){
                if(Files.isDirectory(path)){
                    scanDirectory(path, fileDependencyMap);
                } else {
                        if (isTextFile(path.toFile())){
                            processTextFile(path.toFile(), fileDependencyMap);
                        }
                }
            }
        }
    }

    private static boolean isTextFile(File file) {
        return fileTypeMap.getContentType(file).startsWith("text/");
       // return "text/plain".equals(fileTypeMap.getContentType(file));
    }


    private static String cutRootDir(String fileName, String rootDir) {
        return fileName.startsWith(rootDir) ? fileName.substring(rootDir.length()) : fileName;
    }

    private static void processTextFile(File file, FileDependencyMap fileDependencyMap) {
        String relativePath = cutRootDir(file.getPath(), ROOT_DIR);
        fileDependencyMap.addIndependentFile(relativePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("require")) {  // Quick check before regex
                    String requiredFile = processRequireLine(relativePath, line);
                    if (requiredFile != null) { // Proceed only with valid text files
                        fileDependencyMap.addDependency(relativePath, requiredFile);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + file);
            e.printStackTrace();
        }
    }

    //is extracted to add dependency
    private static String processRequireLine(String relativePath, String line) throws FileNotFoundException, IllegalArgumentException {
        if (!isValidRequireStatement(line)) {
            return null; // Invalid require statement
        }

        String requiredFilePath = extractRequiredFilePath(line);
        File requiredFile = new File(ROOT_DIR, requiredFilePath);

        validateRequiredFile(relativePath, line, requiredFile);

        return requiredFilePath;
    }

    private static boolean isValidRequireStatement(String line) {
        line = line.trim();
        return line.startsWith("require") && line.endsWith(String.valueOf(CLOSE_QUOTE));
    }

    private static String extractRequiredFilePath(String line) {
        line = line.trim();
        int startIndex = line.indexOf(OPEN_QUOTE) + 1;
        int endIndex = line.lastIndexOf(CLOSE_QUOTE);
        return line.substring(startIndex, endIndex).trim();
    }
    private static void validateRequiredFile(String relativePath, String line, File requiredFile) throws FileNotFoundException {
        if (!requiredFile.exists()) {
            throw new FileNotFoundException(relativePath + ": Required file in line '" + line + "' does not exist");
        }

        if (!isTextFile(requiredFile)) {
            throw new IllegalArgumentException(relativePath + ": Required file '" + requiredFile.getName() + "' is not a text file");
        }
    }
}

package com.dztest.task1;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    private static final String ROOT_DIR = "resources/"; // Define your root directory here
    private static final char OPEN_QUOTE = '‘';
    private static final char CLOSE_QUOTE = '’';
    private static final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();



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
        return "text/plain".equals(fileTypeMap.getContentType(file));
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
//    private static void processTextFile(File file, FileDependencyMap fileDependencyMap) {
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//
//            //cut rootDir naming from beggining of fileName
//            String relativePath = cutRootDir(file.getPath().toString(), ROOT_DIR);
//
//            //add Independent File as Key
//            fileDependencyMap.addIndependentFile(relativePath);
//
//
//            while ((line = reader.readLine()) != null) {
//                if (line.contains("require")) {  // Quick check before regex
//
//                    String requiredFile = processRequireLine(relativePath, line);
//                    if (requiredFile!=null) { // do not proceed non text file or invalid files
//                        fileDependencyMap.addDependency(relativePath, requiredFile);
//                    }
//                }
//            }
//        }catch (FileNotFoundException e) {
//            System.err.println(e.getMessage());
//            e.printStackTrace();
//        } catch (IOException e) {
//            System.err.println("Error reading file: " + file);
//            e.printStackTrace();
//        }
//    }



//    private static String processRequireLine(String relativePath, String line) throws FileNotFoundException {
//
//        line = line.trim();
//
//        if (!line.startsWith("require") || !line.endsWith("’")) {
//            return null; // Invalid require statement
//        }
//
//        // Extract the file path from  bracets
//        String requiredFilePath = line.substring(line.indexOf(8216) + 1, line.lastIndexOf(8217)).trim();
//
//
//        File requiredFile = new File(ROOT_DIR, requiredFilePath);
//
//        if (!requiredFile.exists()) {
//            throw new FileNotFoundException(relativePath+": Required file in line '"+ line+"' does not exist");
//           // return null; // File does not exist
//        }
//
//        // Check if it is a text file
//        if (!isTextFile(requiredFile)) {
//            return null; // Not a text file
//        }
//
//        // Return the string of text file path
//        return requiredFilePath;
//    }

    private static String processRequireLine(String relativePath, String line) throws FileNotFoundException, IllegalArgumentException {
        line = line.trim();

        if (!isValidRequireStatement(line)) {
            return null; // Invalid require statement
        }

        String requiredFilePath = extractRequiredFilePath(line);
        File requiredFile = new File(ROOT_DIR, requiredFilePath);

        validateRequiredFile(relativePath, line, requiredFile);

        return requiredFilePath;
    }

    private static boolean isValidRequireStatement(String line) {
        return line.startsWith("require") && line.endsWith(String.valueOf(CLOSE_QUOTE));
    }

    private static String extractRequiredFilePath(String line) {
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

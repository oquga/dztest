package com.dztest.task1;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.*;


//Перевод из файлов в граф структуру данных
public class FileUtils {
    //TODO: Сделать более читабельными пути

    public static String getRelativePath(Path filePathFromRoot){
        String filePathFromRootStr = filePathFromRoot.toString();
        return filePathFromRootStr.startsWith(ROOT_DIR) ? //if
                filePathFromRootStr.substring(ROOT_DIR.length()) : //else
                filePathFromRootStr;
    }
    private static final String ROOT_DIR = "resources/";
    private static final char OPEN_QUOTE = '‘';
    private static final char CLOSE_QUOTE = '’';

    //проверяет директория с файлами
    public static void scanDirectory(Path currentPath, FileGraph fileGraph) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)){
            for (Path path : stream){
                if(Files.isDirectory(path)){
                    scanDirectory(path, fileGraph);
                } else {
                        if (isTextFile(path.toFile())){  //проверяет текстовый ли файл
                            //Находит файл добавляет его как ноду
                            fileGraph.addIndependentFile(getRelativePath(path));

                            processTextFile(path, fileGraph);
                        }
                }
            }
        }
    }

    //Проверка тесктового файла на наличие зависимостей от других текстовых файлов
    private static void processTextFile(Path filePath, FileGraph fileGraph) {
        File file = filePath.toFile();
        //потом добавит зависимости если они есть
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("require")) {
                    String requiredFile = processRequireLine(getRelativePath(filePath), line);
                    if (requiredFile != null) { // Proceed only with valid text files
                        fileGraph.addDependency(getRelativePath(filePath), requiredFile);
                    }
                }
            }
        } catch (FileNotFoundException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + file);
            e.printStackTrace();
        }
    }

    private static String processRequireLine(String relativePath, String line) throws FileNotFoundException, IllegalArgumentException {
        if (!isValidRequireStatement(line)) {
            return null; // Invalid require statement
        }
        String requiredFilePath = extractRequiredFilePath(line);
        File requiredFile = new File(ROOT_DIR, requiredFilePath);

        validateRequiredFile(relativePath, line, requiredFile);

        return requiredFilePath;
    }
    public static boolean isValidRequireStatement(String line) {
        line = line.trim();
        if (line.startsWith("require") && line.endsWith(String.valueOf(CLOSE_QUOTE))){
            line = line.substring("require".length()).trim();
            if (line.startsWith(String.valueOf(OPEN_QUOTE)) && line.endsWith(String.valueOf(CLOSE_QUOTE))){
                return true;
            }
        }
        return false;
    }

    public static String extractRequiredFilePath(String line) {
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
            throw new IllegalArgumentException(relativePath + ": Required file '" + line + "' is not a text file");
        }
    }

    private static boolean isTextFile(File file) {
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        return mimeType != null && (mimeType.startsWith("text/") || mimeType.equals("application/json"));
    }

}

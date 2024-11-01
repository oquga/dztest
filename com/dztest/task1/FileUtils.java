package com.dztest.task1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {

    //Рекрсивно пройтись по директории, собрать все текстоые файлы в HaspMap  в KeySet
    //Пока прохожусь по файлам, за одно проверить require (существует ли такой файл и текстовый ли он)
    //если существует добавить в dependency HashSet этого Key
    private static void scanDirectory(Path currentPath, FileDependencyMap fileDependencyMap) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(currentPath)){
            for (Path i : stream){
                if(Files.isDirectory(i)){
                    scanDirectory(i, fileDependencyMap);
                } else {
                    //check is it txt file
                    fileDependencyMap.addIndependentFile(i.toString());
                    processTxtFile(i.toFile(), fileDependencyMap);

                }
            }
        }
    }
    private static void processTxtFile(File file, FileDependencyMap fileDependencyMap) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            //add Independent File as Key
            //cut rootDir naming from beggining of fileName
            fileDependencyMap.addIndependentFile(file.toString());
            while ((line = reader.readLine()) != null) {
                if (line.contains("require")) {  // Quick check before regex

                    //processRequireLine(file, line);
                    //fileDependencyMap.addDependency(file.toString(), line);

                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + file);
            e.printStackTrace();
        }
    }


}

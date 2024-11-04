# dztest task1

### Input Directory  '/inputDir'

* Прежде чем запустить программу убедитесь что все зависимые файлы помещены в директорию /inputDir.

* Файлы должны быть тестового формата, с mimetype "text/", в программе проверка файлы реализована посредством этого.

* В "require" зависимостях указывать полное названиие файла с раширением. 

### Output Directory '/outputDir'

* Канкатинированный файл при успешном запуске программы будет выведен в /outputDir

# RUN

### Step 1: Clone the Repository and checkout branch
```
git clone https://github.com/oquga/dztest.git
```
```
git checkout task1
```

### Step 2: Build the Project
```bash
javac --release 8 -d build com/dztest/task1/Main.java
```

### Step 3: Create a JAR File
```bash
jar --create --file cat.jar --main-class com.dztest.task1.Main -C build/ .
 ```

### Step 4: Run the JAR File
```bash
java -jar cat.jar
 ```

## Features

* Конкатенация выполнена так что программа считывает все линии запроса других файлов 
и заменяет их контентом этих файлов


* В коде также прописана реализация простой канкатенации 
где просто совмещается текста 
по топологическому порядку;
{Смотреть файл FileCat.java}

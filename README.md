# dztest task1

A brief description of your project, what it does, and its purpose.


## Features

- List the key features of your project.
- Highlight what makes your project unique or useful.

# Installation

## Prerequisites

### Input Directory
- List any prerequisites required to run the project (e.g., Java version, libraries).

### Output Directory

# Steps

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

# dztest task1


`javac --release 8 -d build com/dztest/task1/Main.java`

`jar --create --file cat.jar --main-class com.dztest.task1.Main -C build/ .`

`java -jar cat.jar `
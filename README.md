# Hades
Educational tool for teaching programming. 
For programming, the tool currently supports creating, and solving tasks in C programming language, but thanks to the underlying structure it can be easily extended to support more! 
The user interface currently supports Hungarian, and English language files.

# Examples (from the Hungarian version)

Built-in IDE for solving tasks

![alt text](showcase/1.png)  

Add more chapters, and tasks

![alt text](showcase/2.png)  

Get instant feedback, and progress

![alt text](showcase/3.png)  

# Building requirements
- JDK 8
- MinGW (32 bit edition for windows), gcc for linux

## Building
>./gradlew.bat jar

The gradle task will create a jar file in build/libs

## Setting up
- Copy the jar file to the root directory (hades)
- Open config/paths.conf
- Edit the the compiler_c path to point to...
  - Windows: the MinGW folder
  - Linux: The folder containing the bin folder where gcc is located. (For example, to /usr if gcc is located in /usr/bin/gcc)

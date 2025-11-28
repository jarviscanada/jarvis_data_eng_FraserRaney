# JavaGrep

## Introduction

JavaGrep is a lightweight, cross-platform command-line tool built with Java 8 in IntelliJ IDEA and is containerised with a Dockerfile, published on Docker Hub for easy deployment. It recursively walks a directory tree (using the java.nio API), streams lines from each file, filters them based on a user-provided regex, and writes matches to an output file. The build is managed by Maven, testing is done with JUnit 5, and logging is handled via SLF4J with a Log4j backend. Lambdas and streams are also used.

## Quick Start

```bash
# Using Docker
docker run --rm \
-v /path/to/data:/data -v /path/to/log:/log \
fraserraney/grep "your-regex" /path/to/root /path/to/output.txt

# Example
docker run --rm \
-v `pwd`/data:/data -v `pwd`/log:/log \
fraserraney/grep .*Romeo.*Juliet.* /data /log/grep.out

# Build the project (run this from your project root)
mvn clean package

# Run the program (after packaging)
java -jar target/JavaGrep-1.0.jar "your-regex" /path/to/root /path/to/output.txt

#Example
java -jar ./target/JavaGrep-1.0.jar .*IllegalArgumentException.* ./src /dev/stdout
```

## Implementation

The project is structured using Git for version control and Maven as the build tool. Java 8 is used to leverage modern features such as Streams. Logging is handled with SLF4J (backed by Log4j). JUnit 5 was used for testing, including file listing, reading, matching, and writing. File reading was done with `BufferedReader` wrapped around a `FileReader`. File writing was done with `FileOutputStream` to write raw bytes to the file, wrapped by an `OutputStreamWriter` and a `BufferedWriter`.

### Pseudocode
```java
matchedLines = []
for file in listFilesRecursively(rootDir)
  for line in readLines(file)
      if containsPattern(line)
        matchedLines.add(line)
writeToFile(matchedLines)
```

### Performance Issue
At present, JavaGrep is unable to process huge data (e.g. bigger than your heap memory size). It would be possible to do so by eliminating the collect-to-list functions and instead using Streams for the return type of the readLines method. Similarly, a problem could arise in the use of List as it is passed for writing output in the writeToFile method. This could be solved similarly.

## Files

**1. JavaGrep.java**
The interface that defines the contract (methods such as `process()`, `listFiles()`, `readLines()`, etc.).

**2. JavaGrepImpl.java**
The concrete implementation of the JavaGrep interface. This class handles directory traversal, file reading, regex matching, and file writing.

**3. JavaGrepImplTest.java**
Unit tests covering the main functionalities: listing files, reading lines, matching patterns, writing output, and the full end-to-end `process()` method.

## Testing
Testing is done with JUnit 5. Unit tests create temporary directories and files (using JUnit's `@TempDir`) to simulate file I/O. The `process()` method is tested end-to-end by creating input files, setting a regex, and verifying the contents of the output file. All tests pass.

## Deployment
To produce an executable (fat) JAR, the project uses the Maven Shade Plugin configured in pom.xml. 
Running:
```bash
mvn clean package
```
produces a shaded JAR under the target/ directory. You can run that JAR directly with:
```bash
java -jar target/JavaGrep-1.0.jar "regex" /input/dir /output/file.txt
```
To build the Dockerfile:
```bash
docker build -t my-local-image/grep .
```
The image can also be pulled from Docker Hub:
```bash
docker run --rm \
-v /path/to/root:/path/to/root -v /path/to/output:/path/to/output \
fraserraney/grep "your-regex" /path/to/root /path/to/output.txt
```

## Improvements

**1. Performace Issue**
Modify the code to further use streams to avoid collecting lists of the input data.

**2. Regex Documentation**
Provide documentation to help non-expert users use common patterns more easily.

**3. Performance Testing**
Add testing for large file trees or large files and track I/O.

**4. Configurable Output Formats**
Allow specifying output matches in different formats (e.g., JSON, CSV) for integration with other tools.
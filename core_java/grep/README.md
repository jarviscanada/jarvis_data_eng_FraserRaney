# JavaGrep

## Introduction

JavaGrep is a simple command-line grep-like utility written in Java 8. It allows users to search for lines in files under a given directory that match a user-provided regular expression (regex), then write matching lines to an output file. This tool is useful for developers or data analysts who want a lightweight, cross-platform text-search utility. Under the hood, JavaGrep is built with **Maven**, uses **Java 8** streams and the **java.nio** API for file traversal, **JUnit 5** for tests, and **SLF4J** (with backing Log4j) for logging.

## Quick Start

```bash
# Build the project (run this from your project root)
mvn clean package

# Run the program (after packaging)
java -jar target/JavaGrep-1.0.jar "your-regex" /path/to/root /path/to/output.txt

#Example
java -jar ./target/JavaGrep-1.0.jar .*IllegalArgumentException.* ./src /dev/stdout
```

## Implementation

The project is structured using Git for version control and Maven as the build tool. Java 8 is used to leverage modern features such as Streams. Logging is handled with SLF4J (backed by Log4j). JUnit 5 was used for testing, including file listing, reading, matching, and writing. File reading was done with `BufferedReader` wrapped around a `FileReader`. File writing was done with `FileOutputStream` to write raw bytes to the file, wrapped by an `OutputStreamWriter` and a `BufferedWriter`.

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

## Improvements

**1. Regex Documentation**
Provide documentation to help non-expert users use common patterns more easily.

**2. Performance Testing**
Add testing for large file trees or large files and track I/O.

**3. Configurable Output Formats**
Allow specifying output matches in different formats (e.g., JSON, CSV) for integration with other tools.
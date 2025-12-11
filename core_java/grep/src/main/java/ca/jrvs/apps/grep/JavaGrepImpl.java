package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImpl implements JavaGrep {

  final Logger LOGGER = LoggerFactory.getLogger(JavaGrepImpl.class);

  private String rootPath;
  private String outFile;
  private String regex;
  private Pattern pattern;

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }
    // Use default logger config
    BasicConfigurator.configure();

    JavaGrepImpl javaGrepImpl = new JavaGrepImpl();
    javaGrepImpl.setRegex(args[0]);
    javaGrepImpl.setRootPath(args[1]);
    javaGrepImpl.setOutFile(args[2]);

    try {
      javaGrepImpl.process();
    } catch (Exception ex) {
      javaGrepImpl.LOGGER.error("Unable to process", ex);
    }
  }

  /**
   * Top level search workflow
   *
   * @throws IOException
   */
  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<>();

    listFiles(getRootPath())
        .stream()
        .forEach(file -> {
          readLines(file)
              .stream()
              .filter(this::containsPattern)
              .forEach(matchedLines::add);
        });

    writeToFile(matchedLines);
  }

  /**
   * Traverse a given directory and return all files
   * Uses the walk method of {@link Files} to return a DFS list of the files
   * in the rootDir.
   *
   * @param rootDir input directory
   * @return files under the rootDir
   */
  @Override
  public List<File> listFiles(String rootDir) {
    try (Stream<Path> paths = Files.walk(Paths.get(rootDir))) {
      return paths
          .filter(Files::isRegularFile)
          .map(Path::toFile)
          .collect(Collectors.toList());
    } catch (IOException ex) {
      this.LOGGER.error("Unable to list files", ex);
      return Collections.emptyList();
    }
  }

  /**
   * Read a file and return all the lines
   * <p>
   * This method uses a {@link BufferedReader} wrapped around a {@link FileReader}. The
   * {@code FileReader} is a character-stream reader that reads directly from the file using the
   * platform's default character encoding. {@link BufferedReader} adds buffering (8 KB by
   * default).
   * </p>
   * <p>
   * Note: This implementation does not expose encoding choice, it assumes default encoding.
   * </p>
   *
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  @Override
  public List<String> readLines(File inputFile) {
    if (inputFile == null || !inputFile.isFile()) {
      throw new IllegalArgumentException("inputFile is not a valid file: " + inputFile);
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
      return reader
          .lines()
          .collect(Collectors.toList());
    } catch (IOException ex) {
      this.LOGGER.error("Unable to read lines from file: " + inputFile, ex);
    }

    return Collections.emptyList();
  }

  /**
   * check if a line contains the regex patter (passed by the user)
   *
   * @param line input string
   * @return true if there is a match
   */
  @Override
  public boolean containsPattern(String line) {
    return pattern.matcher(line).matches();
  }

  /**
   * Write lines to a file
   * <p>
   * This method uses {@link FileOutputStream} to write raw bytes to the file, wrapped by an
   * {@link OutputStreamWriter} to convert characters into bytes using a character encoding. By
   * default, the {@code OutputStreamWriter} uses the platform?s default charset. A
   * {@link BufferedWriter} reduces the number of I/O writes by holding characters in memory and
   * flushing them in larger chunks
   * </p>
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  @Override
  public void writeToFile(List<String> lines) throws IOException {
    if (this.getOutFile() == null) {
      throw new IllegalArgumentException("outFile is not set");
    }

    File file = new File(this.getOutFile());

    try (FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        BufferedWriter writer = new BufferedWriter(osw)) {

      lines
          .stream()
          .forEach(line -> {
            try {
              writer.write(line);
              writer.newLine();
            } catch (IOException ex) {
              this.LOGGER.error("Unable to write line to file: " + line, ex);
            }
          });
    }

  }

  @Override
  public String getRootPath() {
    return this.rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return this.regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
    this.pattern = Pattern.compile(regex);
  }

  @Override
  public String getOutFile() {
    return this.outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}

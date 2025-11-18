package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {

  /**
   * Top level search workflow
   * @throws IOException
   */
  void process() throws IOException;

  /**
   * Traverse a given directory and return all files
   * @param rootDir input directory
   * @return files under the rootDir
   */
  List<File> listFiles(String rootDir);

  /**
   * Read a file and return all the lines
   * <p>
   * This method uses a {@link java.io.BufferedReader} wrapped
   * around a {@link java.io.FileReader}. The {@code FileReader} is a
   * character-stream reader that reads directly from the file using the
   * platform's default character encoding. {@link java.io.BufferedReader}
   * adds buffering (8 KB by default).
   * </p>
   * <p>
   * Note: This implementation does not expose encoding choice ? it assumes default encoding.
   * </p>
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  List<String> readLines(File inputFile);

  /**
   * check if a line contains the regex patter (passed by the user)
   * @param line input string
   * @return true if there is a match
   */
  boolean containsPattern(String line);

  /**
   * Write lines to a file
   * <p>
   * This method uses {@link java.io.FileOutputStream} to write raw bytes to the
   * file, wrapped by an {@link java.io.OutputStreamWriter} to convert
   * characters into bytes using a character encoding. By default, the
   * {@code OutputStreamWriter} uses the platform?s default charset. A
   * {@link java.io.BufferedWriter} reduces the number of I/O writes by holding
   * characters in memory and flushing them in larger chunks
   * </p>
   * @param lines matched line
   * @throws IOException if write failed
   */
  void writeToFile(List<String> lines) throws IOException;

  String getRootPath();

  void setRootPath(String rootPath);

  String getRegex();

  String setRegex(String regex);

  String getOutFile();

  String setOutFile(String outFile);
}

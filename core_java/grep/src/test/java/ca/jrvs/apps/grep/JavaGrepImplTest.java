package ca.jrvs.apps.grep;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class JavaGrepImplTest {

  private JavaGrepImpl javaGrep;

  @BeforeEach
  void setUp() {
    javaGrep = new JavaGrepImpl();
  }

  @Test
  void process(@TempDir Path tempDir) throws IOException {
    Path dir = tempDir.resolve("data");
    Files.createDirectory(dir);

    Path file1 = dir.resolve("f1.txt");
    Path file2 = dir.resolve("f2.txt");

    List<String> content1 = Arrays.asList("apple", "banana", "apple pie");
    List<String> content2 = Arrays.asList("car", "dog", "elephant");
    Files.write(file1, content1);
    Files.write(file2, content2);

    javaGrep.setRootPath(dir.toString());
    javaGrep.setRegex(".*apple.*");

    Path output = tempDir.resolve("match.txt");
    javaGrep.setOutFile(output.toString());

    javaGrep.process();

    List<String> matched = Files.readAllLines(output);
    assertEquals(Arrays.asList("apple", "apple pie"), matched);
  }

  @Test
  void listFiles(@TempDir Path tempDir) throws IOException {
    Path subdir = tempDir.resolve("sub");
    Files.createDirectory(subdir);
    Path file1 = tempDir.resolve("a.txt");
    Path file2 = subdir.resolve("b.txt");
    Files.write(file1, Arrays.asList("line1"));
    Files.write(file2, Arrays.asList("line2"));

    List<File> files = javaGrep.listFiles(tempDir.toString());

    List<String> names = files.stream()
        .map(File::getName)
        .sorted()
        .collect(Collectors.toList());

    assertEquals(Arrays.asList("a.txt", "b.txt"), names);
  }

  @Test
  void readLines(@TempDir Path tempDir) throws IOException {
    Path file = tempDir.resolve("test.txt");
    List<String> content = Arrays.asList("first line", "second line", "third");
    Files.write(file, content);

    List<String> lines = javaGrep.readLines(file.toFile());

    assertEquals(content, lines);
  }

  @Test
  void containsPattern() {
    javaGrep.setRegex("foo.*");  // matches any string starting with "foo"
    assertTrue(javaGrep.containsPattern("foobar"));
    assertFalse(javaGrep.containsPattern("barfoo"));
  }

  @Test
  void writeToFile(@TempDir Path tempDir) throws IOException {
    // Prepare lines
    List<String> lines = Arrays.asList("line A", "line B", "line C");

    Path out = tempDir.resolve("out.txt");
    javaGrep.setOutFile(out.toString());

    javaGrep.writeToFile(lines);

    List<String> written = Files.readAllLines(out);

    assertEquals(lines, written);
  }
}
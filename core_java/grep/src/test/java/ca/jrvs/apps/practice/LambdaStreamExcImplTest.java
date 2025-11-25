package ca.jrvs.apps.practice;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LambdaStreamExcImplTest {
  private LambdaStreamExcImpl lse;

  @BeforeEach
  void setUp() {
    lse = new LambdaStreamExcImpl();
  }

  @Test
  void createStrStream() {
    Stream<String> stream = lse.createStrStream("a", "b", "c");
    List<String> result = stream.collect(Collectors.toList());
    assertEquals(Arrays.asList("a", "b", "c"), result);
  }

  @Test
  void toUpperCase() {
    Stream<String> uppers = lse.toUpperCase("hello", "world");
    List<String> result = uppers.collect(Collectors.toList());
    assertEquals(Arrays.asList("HELLO", "WORLD"), result);
  }

  @Test
  void filter() {
    Stream<String> s = lse.createStrStream("apple", "banana", "apricot");
    Stream<String> filtered = lse.filter(s, "^a.*");
    List<String> result = filtered.collect(Collectors.toList());
    assertEquals(Arrays.asList("banana"), result);
  }

  @Test
  void createIntStream() {
    IntStream is = lse.createIntStream(new int[]{1, 2, 3});
    int[] arr = is.toArray();
    assertArrayEquals(new int[]{1, 2, 3}, arr);
  }

  @Test
  void toList() {
    Stream<String> s = Stream.of("x", "y", "z");
    List<String> list = lse.toList(s);
    assertEquals(Arrays.asList("x", "y", "z"), list);
  }

  @Test
  void testToList() {
    IntStream is = IntStream.of(4, 5, 6);
    List<Integer> list = lse.toList(is.boxed());
    assertEquals(Arrays.asList(4, 5, 6), list);
  }

  @Test
  void testCreateIntStream() {
    IntStream is = lse.createIntStream(5, 8);
    int[] arr = is.toArray();
    assertArrayEquals(new int[]{5, 6, 7, 8}, arr);
  }

  @Test
  void squareRootIntStream() {
    IntStream is = IntStream.of(1, 4, 9);
    DoubleStream ds = lse.squareRootIntStream(is);
    double[] results = ds.toArray();
    assertArrayEquals(new double[]{1.0, 2.0, 3.0}, results);
  }

  @Test
  void getOdd() {
    IntStream is = IntStream.of(1, 2, 3, 4, 5, -3);
    IntStream odd = lse.getOdd(is);
    int[] result = odd.toArray();
    assertArrayEquals(new int[]{1, 3, 5, -3}, result);
  }

  @Test
  void getLambdaPrinter() {
    Consumer<String> myPrinter = lse.getLambdaPrinter("a", "b");
    myPrinter.accept("X");
  }

  @Test
  void printMessages() {
    String[] messages = {"one", "two", "three"};
    lse.printMessages(messages, lse.getLambdaPrinter("start>", "<end"));
  }

  @Test
  void printOdd() {
    IntStream is = lse.createIntStream(0, 5);
    lse.printOdd(is, lse.getLambdaPrinter("start>", "<end"));
  }

  @Test
  void flatNestedInt() {
    List<Integer> list1 = Arrays.asList(1, 2, 3);
    List<Integer> list2 = Arrays.asList(4, 5);
    Stream<List<Integer>> nested = Stream.of(list1, list2);
    Stream<Integer> flat = lse.flatNestedInt(nested);
    List<Integer> result = flat.collect(Collectors.toList());
    assertEquals(Arrays.asList(1, 4, 9, 16, 25), result);
  }
}
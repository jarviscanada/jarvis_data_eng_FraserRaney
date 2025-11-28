package ca.jrvs.apps.practice;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegexExcImplTest {

  @org.junit.jupiter.api.Test
  void matchJpeg() {
    assertTrue(new RegexExcImpl().matchJpeg("photo.jpg"));
    assertTrue(new RegexExcImpl().matchJpeg("photo.jpeg"));
    assertTrue(new RegexExcImpl().matchJpeg("PHOTO.JpG"));
    assertFalse(new RegexExcImpl().matchJpeg("photo.png"));
    assertFalse(new RegexExcImpl().matchJpeg("photojpg"));
    assertFalse(new RegexExcImpl().matchJpeg("archive.jpg.txt"));
  }

  @org.junit.jupiter.api.Test
  void matchIp() {
    assertTrue(new RegexExcImpl().matchIp("0.0.0.0"));
    assertTrue(new RegexExcImpl().matchIp("999.999.999.999"));
    assertTrue(new RegexExcImpl().matchIp("192.168.1.1"));
    assertFalse(new RegexExcImpl().matchIp("1234.1.1.1"));
    assertFalse(new RegexExcImpl().matchIp("1.1.1"));
    assertFalse(new RegexExcImpl().matchIp("1.1.1.1.1"));
  }

  @org.junit.jupiter.api.Test
  void isEmptyLine() {
    assertTrue(new RegexExcImpl().isEmptyLine(""));
    assertTrue(new RegexExcImpl().isEmptyLine("   "));
    assertTrue(new RegexExcImpl().isEmptyLine("\t"));
    assertFalse(new RegexExcImpl().isEmptyLine(" a "));
    assertFalse(new RegexExcImpl().isEmptyLine("line"));
  }
}
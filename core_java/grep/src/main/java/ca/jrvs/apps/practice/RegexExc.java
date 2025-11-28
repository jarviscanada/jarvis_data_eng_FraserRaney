package ca.jrvs.apps.practice;

public interface RegexExc {

  /**
   * returns true if filename extension is jpeg or jpg (case insensitve)
   *
   * @param filename
   * @return
   */
  public boolean matchJpeg(String filename);

  /**
   * return true is ip is valid to simplfy the problem, IP address is 0.0.0.0 to 999.999.999.999
   *
   * @param ip
   * @return
   */
  public boolean matchIp(String ip);

  /**
   * return true if line is empty (e.g. empty, white space, tabs, etc...)
   *
   * @param line
   * @return
   */
  public boolean isEmptyLine(String line);
}

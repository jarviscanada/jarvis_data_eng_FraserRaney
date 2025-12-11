package ca.jrvs.apps.stockquote.data.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtils.class);
  private static final String sqlExceptionFormat = "exception in %s, message %s, code: %s";
  private static final String exceptionFormat = "exception in %s, message %s";

  private static Connection connection;

  public static Connection getConnection() {
    Map<String, String> properties = new HashMap<>();
    try (BufferedReader br = new BufferedReader(
        new FileReader("src/main/resources/properties.txt"))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] tokens = line.split(":");
        properties.put(tokens[0], tokens[1]);
      }
    } catch (FileNotFoundException e) {
      handleException("Main.main", e, LOGGER);
    } catch (IOException e) {
      handleException("Main.main", e, LOGGER);
    }

    try {
      Class.forName(properties.get("db-class"));
    } catch (ClassNotFoundException e) {
      handleException("Main.main", e, LOGGER);
    }

    String url = "jdbc:postgresql://localhost:" + properties.get("port") + "/"
        + properties.get("database");

    if (connection == null) {
      synchronized (DatabaseUtils.class) {
        if (connection == null) {
          try {
            connection = DriverManager.getConnection(url, properties.get("username"),
                properties.get("password"));
          } catch (SQLException e) {
            handleSqlException("DatabaseUtils.getConnection", e, LOGGER);
          }
        }
      }
    }
    return connection;
  }

  public static void handleSqlException(String method, SQLException e, Logger log) {
    log.warn(String.format(sqlExceptionFormat, method, e.getMessage(), e.getErrorCode()));
  }

  public static void handleException(String method, Exception e, Logger log) {
    log.warn(String.format(exceptionFormat, method, e.getMessage()));
  }
}
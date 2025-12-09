package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.controller.StockQuoteController;
import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.util.QuoteHttpHelper;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static Logger LOGGER = LoggerFactory.getLogger(Main.class);
  private static final String exceptionFormat = "exception in %s, message %s";

  public static void main(String[] args) {
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
    OkHttpClient client = new OkHttpClient();
    String url =
        "jdbc:postgresql://" + properties.get("server") + ":" + properties.get("port") + "/"
            + properties.get("database");
    try (Connection c = DriverManager.getConnection(url, properties.get("username"),
        properties.get("password"))) {
      QuoteDao qRepo = new QuoteDao(c);
      PositionDao pRepo = new PositionDao(c);
      QuoteHttpHelper rcon = new QuoteHttpHelper(properties.get("api-key"), client);
      QuoteService sQuote = new QuoteService(qRepo, rcon);
      PositionService sPos = new PositionService(pRepo);
      StockQuoteController con = new StockQuoteController(sQuote, sPos);
      con.initClient(args);
    } catch (SQLException e) {
      handleException("Main.main", e, LOGGER);
    }
  }

  public static void handleException(String method, Exception e, Logger log) {
    log.warn(String.format(exceptionFormat, method, e.getMessage()));
  }

}


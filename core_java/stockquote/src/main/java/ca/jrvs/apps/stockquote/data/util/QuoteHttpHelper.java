package ca.jrvs.apps.stockquote.data.util;

import ca.jrvs.apps.practice.JsonParser;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuoteHttpHelper {

  private String apiKey;
  private static Logger LOGGER = LoggerFactory.getLogger(QuoteHttpHelper.class);
  private static final String httpExceptionFormat = "exception in %s, message %s, cause: %s";
  private final String EMPTY_RESP = "{\"c\":0,\"d\":null,\"dp\":null,\"h\":0,\"l\":0,\"o\":0,\"pc\":0,\"t\":0}";
  private static final String exceptionFormat = "exception in %s, message %s";

  private OkHttpClient client;

  public QuoteHttpHelper(String apiKey, OkHttpClient client) {
    this.apiKey = apiKey;
    this.client = client;
  }

  public static String getApiKey() {
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
    return properties.get("api-key");
  }

  public QuoteHttpHelper() {
    this(getApiKey(), new OkHttpClient());
  }

  /**
   * Fetch latest quote data from finnhub endpoint
   *
   * @param symbol
   * @return Quote with latest data
   * @throws IllegalArgumentException - if no data was found for the given symbol
   */
  public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {

    Request request = new Request.Builder()
        .url("https://finnhub.io/api/v1/quote?symbol=" + symbol)
        .addHeader("X-Finnhub-Token", apiKey)
        .build();

    try (Response response = client.newCall(request).execute()) {
      String body = response.body().string();
      if (body.equals(EMPTY_RESP) || body.contains("error")) {
        throw new IllegalArgumentException(
            "USAGE: no data was found for the given symbol (US stocks only)");
      }
      Quote quote = JsonParser.toObjectFromJson(body,
          ca.jrvs.apps.stockquote.data.entity.Quote.class);
      quote.setTicker(symbol);
      quote.setTimestamp(new Timestamp(System.currentTimeMillis()));
      quote.setLatestTradingDay(new Timestamp(quote.getLatestTradingDay().getTime() * 1000L));
      return quote;
    } catch (IOException e) {
      handleHttpException("QuoteHttpHelp.fetchQuoteInfo", e, LOGGER);
    }
    return null;
  }

  public static void handleHttpException(String method, IOException e, Logger log) {
    log.warn(String.format(httpExceptionFormat, method, e.getMessage(), e.getCause()));
  }

  public static void handleException(String method, Exception e, Logger log) {
    log.warn(String.format(exceptionFormat, method, e.getMessage()));
  }

}

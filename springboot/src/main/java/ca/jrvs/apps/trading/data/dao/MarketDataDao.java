package ca.jrvs.apps.trading.data.dao;

import ca.jrvs.apps.trading.data.config.MarketDataConfig;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import ca.jrvs.apps.trading.data.util.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public class MarketDataDao {

  @Value("${finnhub.api.key}")
  private String apiKey;

  @Value("${finnhub.base.url}")
  private String baseUrl;

  private static Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
  private final String EMPTY_RESP = "{\"c\":0,\"d\":null,\"dp\":null,\"h\":0,\"l\":0,\"o\":0,\"pc\":0,\"t\":0}";
  private static final String httpExceptionFormat = "exception in %s, message %s, cause: %s";
  private static final String exceptionFormat = "exception in %s, message %s";

  private final HttpClient httpClient;

  public MarketDataDao(PoolingHttpClientConnectionManager cm, MarketDataConfig mc) {
    this.httpClient = HttpClients.custom()
        .setConnectionManager(cm)
        .build();

    this.baseUrl = mc.getHost();
    this.apiKey = mc.getToken();
  }

  public MarketDataDao() {
    this.httpClient = HttpClients.custom()
        .build();
  }

  protected HttpClient getHttpClient() {
    return this.httpClient;
  }

  /**
   * Execute a GET request and return http entity/body as a string Tip: use EntitiyUtils.toString to
   * process HTTP entity
   *
   * @param url resource URL
   * @return http response body or Optional.empty for 404 response
   * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
   */
  private Optional<String> executeHttpGet(String url) {
    HttpGet request = new HttpGet(url);
    String body = null;
    try {
      HttpResponse response = getHttpClient().execute(request);

      int status = response.getStatusLine().getStatusCode();
      if (status == HttpStatus.NOT_FOUND.value()) {
        return Optional.empty();
      }

      HttpEntity entity = response.getEntity();
      if (entity != null) {
        body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
      }

      if (body != null && (body.equals(EMPTY_RESP) || body.contains("error"))) {
        throw new IllegalArgumentException(
            "USAGE: no data was found for the given symbol (US stocks only)");
      }

      if (body != null) {
        return Optional.of(body);
      }

      throw new DataRetrievalFailureException("HTTP request failed with status " + status);
    } catch (IOException e) {
      handleException("MarketDataDao.executeHttpGet", e, logger);
      throw new DataRetrievalFailureException(
          "HTTP request failed: " + e.getMessage(), e);
    }
  }


  public FinnhubQuote findQuoteByTicker(String ticker) {

    String url = String.format(
        "%s/quote?symbol=%s&token=%s",
        baseUrl, ticker, apiKey
    );
    try {
      Optional<String> body = executeHttpGet(url);
      if (body.isPresent()) {
        return JsonParser.toObjectFromJson(body.get(), FinnhubQuote.class);
      }
      throw new DataRetrievalFailureException("HTTP request failed with status 404");
    } catch (IOException e) {
      handleException("MarketDataDao.findQuoteByTicker.parseJson", e, logger);
      throw new DataRetrievalFailureException("Error parsing Json: " + ticker);
    } catch (Exception e) {
      handleException("MarketDataDao.findQuoteByTicker", e, logger);
      throw e;
    }

  }

  /**
   * Get an FinnhubQuote
   *
   * @param ticker
   * @throws IllegalArgumentException      if a given ticker is invalid
   * @throws DataRetrievalFailureException if HTTP request failed
   */
  public Optional<FinnhubQuote> findById(String ticker) {
    if (ticker == null || ticker.isEmpty()) {
      throw new IllegalArgumentException("Ticker cannot be null or empty");
    }

    try {
      FinnhubQuote quote = findQuoteByTicker(ticker);
      if (quote != null) {
        return Optional.of(quote);
      }
    } catch (Exception e) {
      handleException("MarketDataDao.findById", e, logger);
      throw e;
    }
    return Optional.empty();

  }

  /**
   * Get quotes from Finnhub
   *
   * @param tickers is a list of tickers
   * @return a list of IexQuote objects
   * @throws IllegalArgumentException      if a given ticker is invalid
   * @throws DataRetrievalFailureException if HTTP request failed
   */
  public List<FinnhubQuote> findAllById(Iterable<String> tickers) {
    try {
      return StreamSupport.stream(tickers.spliterator(), false)
          .map(this::findById)
          .map(Optional::get)
          .collect(Collectors.toList());
    } catch (NoSuchElementException ne) {
      handleException("MarketDataDao.findAllById.optionalGet", ne, logger);
      return Collections.emptyList();
    } catch (Exception e) {
      handleException("MarketDataDao.findAllById", e, logger);
      throw e;
    }
  }


  public static void handleHttpException(String method, IOException e, Logger log) {
    log.warn(String.format(httpExceptionFormat, method, e.getMessage(), e.getCause()));
  }

  public static void handleException(String method, Exception e, Logger log) {
    log.warn(String.format(exceptionFormat, method, e.getMessage()));
  }
}


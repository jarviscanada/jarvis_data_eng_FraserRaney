package ca.jrvs.apps.trading.data.dao;

import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import ca.jrvs.apps.trading.data.util.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataRetrievalFailureException;
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

  protected HttpClient getHttpClient() {
    return HttpClients.custom()
        .build();
  }

  private String getResponseBodyFromUrl(String url) throws IOException {
    HttpGet request = new HttpGet(url);
    String body = null;
    try {
      HttpResponse response = getHttpClient().execute(request);
      HttpEntity entity = response.getEntity();
      if (entity != null) {
        body = EntityUtils.toString(entity, StandardCharsets.UTF_8);
      }
      if (body.equals(EMPTY_RESP) || body.contains("error")) {
        throw new IllegalArgumentException(
            "USAGE: no data was found for the given symbol (US stocks only)");
      }
      if (body == null) {
        throw new DataRetrievalFailureException("Error parsing response body");
      }
      return body;
    } catch (IOException e) {
      handleException("MarketDataDao.getRsponseBodyFromUrl", e, logger);
      throw e;
    }
  }

  public FinnhubQuote findQuoteByTicker(String ticker) {

    String url = String.format(
        "%s/quote?symbol=%s&token=%s",
        baseUrl, ticker, apiKey
    );
    try {
      String body = getResponseBodyFromUrl(url);
      return JsonParser.toObjectFromJson(body, FinnhubQuote.class);
    } catch (IOException e) {
      handleException("MarketDataDao.findQuoteByTicker", e, logger);
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


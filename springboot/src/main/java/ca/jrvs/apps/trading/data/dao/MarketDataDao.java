package ca.jrvs.apps.trading.data.dao;

import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class MarketDataDao {

  @Value("${finnhub.api.key}")
  private String apiKey;

  @Value("${finnhub.base.url}")
  private String baseUrl;


  private final RestTemplate restTemplate;

  public MarketDataDao(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public FinnhubQuote findQuoteByTicker(String ticker) {
    String url = String.format(
        "%s/quote?symbol=%s&token=%s",
        baseUrl, ticker, apiKey
    );

    return restTemplate.getForObject(url, FinnhubQuote.class);
  }
}


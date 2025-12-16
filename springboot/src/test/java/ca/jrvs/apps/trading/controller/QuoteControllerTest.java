package ca.jrvs.apps.trading.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.data.dao.MarketDataDao;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import ca.jrvs.apps.trading.service.QuoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

class QuoteControllerTest {

  @Mock
  private RestTemplate mockRestTemplate;

  private MarketDataDao marketDataDao;
  private QuoteService quoteService;
  private QuoteController quoteController;

  @Value("finnhub.api.key")
  private String apiKey;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    marketDataDao = new MarketDataDao(mockRestTemplate);
    quoteService = new QuoteService(marketDataDao);
    quoteController = new QuoteController(quoteService);
  }

  @Test
  void getQuote() {
    String ticker = "AAPL";

    when(mockRestTemplate.getForObject(anyString(), eq(FinnhubQuote.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    assertThrows(ResponseStatusException.class,
        () -> quoteController.getQuote(ticker));

    when(mockRestTemplate.getForObject(anyString(), eq(FinnhubQuote.class)))
        .thenThrow(new IllegalArgumentException());

    assertThrows(ResponseStatusException.class,
        () -> quoteController.getQuote(ticker));
  }
}
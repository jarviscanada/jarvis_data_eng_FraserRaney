package ca.jrvs.apps.trading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.data.dao.MarketDataDao;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

class QuoteServiceTest {

  @Mock
  private RestTemplate mockRestTemplate;

  private MarketDataDao marketDataDao;
  private QuoteService quoteService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    marketDataDao = new MarketDataDao(mockRestTemplate);
    quoteService = new QuoteService(marketDataDao);
  }

  @Test
  void findFinnhubQuoteByTicker_throws() {
    String ticker = "AAPL";

    when(mockRestTemplate.getForObject(anyString(), eq(FinnhubQuote.class)))
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    assertThrows(HttpClientErrorException.class,
        () -> quoteService.findFinnhubQuoteByTicker(ticker));


  }

  @Test
  void findFinnhubQuoteByTicker_success() {
    String ticker = "AAPL";
    FinnhubQuote fakeQuote = new FinnhubQuote();
    fakeQuote.setC(123.45);
    fakeQuote.setT(1000000L);

    when(mockRestTemplate.getForObject(anyString(), eq(FinnhubQuote.class)))
        .thenReturn(fakeQuote);

    FinnhubQuote quote = quoteService.findFinnhubQuoteByTicker(ticker);

    assertNotNull(quote);
    assertEquals(123.45, quote.getC());
    assertEquals(1000000L, quote.getT());
  }
}
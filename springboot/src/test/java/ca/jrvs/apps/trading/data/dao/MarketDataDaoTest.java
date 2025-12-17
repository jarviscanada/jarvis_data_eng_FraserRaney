package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

class MarketDataDaoTest {

  @Mock
  private RestTemplate mockRestTemplate;

  private MarketDataDao marketDataDao;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    marketDataDao = new MarketDataDao(mockRestTemplate);
  }

  @Test
  void findQuoteByTicker() {
    String ticker = "AAPL";
    FinnhubQuote fakeQuote = new FinnhubQuote();
    fakeQuote.setC(123.45);
    fakeQuote.setT(1000000L);

    when(mockRestTemplate.getForObject(anyString(), eq(FinnhubQuote.class)))
        .thenReturn(fakeQuote);

    FinnhubQuote quote = marketDataDao.findQuoteByTicker(ticker);

    assertNotNull(quote);
    assertEquals(123.45, quote.getC());
    assertEquals(1000000L, quote.getT());
  }
}
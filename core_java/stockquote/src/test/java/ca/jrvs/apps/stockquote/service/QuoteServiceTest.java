package ca.jrvs.apps.stockquote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import ca.jrvs.apps.stockquote.data.util.QuoteHttpHelper;
import java.sql.Timestamp;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuoteServiceTest {

  private QuoteDao mockDao;
  private QuoteHttpHelper mockHelper;
  private QuoteService quoteService;

  @BeforeEach
  void setUp() {
    mockDao = mock(QuoteDao.class);
    mockHelper = mock(QuoteHttpHelper.class);
    quoteService = new QuoteService(mockDao, mockHelper);
  }

  @Test
  void fetchQuoteDataFromAPI_succes() {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    String symbol = "AAPL";
    Quote fakeQuote = new Quote();
    fakeQuote.setTicker(symbol);
    fakeQuote.setPrice(100.0);
    fakeQuote.setOpen(10.0);
    fakeQuote.setHigh(10.0);
    fakeQuote.setLow(10.0);
    fakeQuote.setChange(0.9);
    fakeQuote.setChangePercent(0.9);
    fakeQuote.setTimestamp(now);
    fakeQuote.setLatestTradingDay(now);
    fakeQuote.setPreviousClose(9);

    when(mockHelper.fetchQuoteInfo(symbol)).thenReturn(fakeQuote);
    when(mockDao.save(fakeQuote)).thenReturn(fakeQuote);

    Optional<Quote> result = quoteService.fetchQuoteDataFromAPI(symbol);

    assertTrue(result.isPresent());
    assertEquals(symbol, result.get().getTicker());
    assertEquals(100.0, result.get().getPrice());
  }

  @Test
  void fetchQuoteDataFromAPI_null_throws() {
    assertThrows(IllegalArgumentException.class, () -> quoteService.fetchQuoteDataFromAPI(null));
  }

}
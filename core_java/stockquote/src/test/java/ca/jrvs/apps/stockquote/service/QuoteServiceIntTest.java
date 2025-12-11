package ca.jrvs.apps.stockquote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import ca.jrvs.apps.stockquote.data.util.QuoteHttpHelper;
import java.sql.Timestamp;
import java.util.Optional;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuoteServiceIntTest {

  private QuoteDao dao = new QuoteDao();
  private QuoteService quoteService_live = new QuoteService();
  private QuoteHttpHelper mockHelper;

  private QuoteService quoteService_local;

  @BeforeAll
  public static void setUpClass() {
    BasicConfigurator.configure();
  }

  @BeforeEach
  void setUp() {
    mockHelper = mock(QuoteHttpHelper.class);
    quoteService_local = new QuoteService(dao, mockHelper);
  }

  @AfterEach
  void tearDown() {
    dao.deleteAll();
  }

  @Test
  void fetchQuoteDataFromAPI_local() {
    Quote q = new Quote();
    q.setTicker("TESTSYM");
    q.setOpen(123.45);
    q.setHigh(130.00);
    q.setLow(120.00);
    q.setPrice(125.00);
    Timestamp now = new Timestamp(System.currentTimeMillis());
    q.setLatestTradingDay(now);
    q.setPreviousClose(122.00);
    q.setChange(3.00);
    q.setChangePercent(2.46);
    q.setTimestamp(new Timestamp(System.currentTimeMillis()));

    when(mockHelper.fetchQuoteInfo("TESTSYM")).thenReturn(q);

    Optional<Quote> result = quoteService_local.fetchQuoteDataFromAPI("TESTSYM");

    Optional<Quote> local = dao.findById("TESTSYM");

    assertTrue(result.isPresent());
    assertTrue(local.isPresent());
    assertEquals("TESTSYM", result.get().getTicker());
    assertEquals("TESTSYM", local.get().getTicker());
    assertEquals(125.0, result.get().getPrice());
    assertEquals(125.0, local.get().getPrice());

  }

  @Test
  void fetchQuoteDataFromAPI_live() {

    String symbol = "MSFT";
    Optional<Quote> result = quoteService_live.fetchQuoteDataFromAPI(symbol);
    Optional<Quote> local = dao.findById(symbol);

    assertTrue(result.isPresent());
    assertTrue(local.isPresent());
    assertEquals(local.get().getTicker(), result.get().getTicker());
    assertEquals(result.get().getPrice(), local.get().getPrice(), 0.005);
    assertEquals(result.get().getHigh(), local.get().getHigh(), 0.005);
    assertEquals(result.get().getLow(), local.get().getLow(), 0.005);
    assertEquals(result.get().getTimestamp(), local.get().getTimestamp());

  }

}

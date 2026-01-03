package ca.jrvs.apps.trading.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.jrvs.apps.trading.data.entity.Quote;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class QuoteControllerIntegrationTest {

  @Autowired
  private QuoteController quoteController;

  @Test
  void quoteControllerIntegrationTest() {
    String ticker = "AAPL";

    Quote quote = quoteController.createQuote(ticker);

    assertNotNull(quote, "Response body should not be null");

    assertNotNull(quote.getTicker(), "Ticker should not be null");
    assertNotNull(quote.getLastPrice(), "Current price should not be null");
  }

  @Test
  void dailyList() {
    quoteController.createQuote("AAPL");
    quoteController.createQuote("MSFT");

    List<Quote> quotes = quoteController.getDailyList();

    assertFalse(quotes.isEmpty());
    assertEquals(2, quotes.size());

  }

}
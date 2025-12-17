package ca.jrvs.apps.trading.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuoteControllerIntegrationTest {

  @Autowired
  private QuoteController quoteController;

  @Test
  void quoteControllerIntegrationTest() {
    String ticker = "AAPL";

    FinnhubQuote quote = quoteController.getQuote(ticker);

    assertNotNull(quote, "Response body should not be null");

    assertNotNull(quote.getT(), "Timestamp should not be null");
    assertNotNull(quote.getC(), "Current price should not be null");
  }

}
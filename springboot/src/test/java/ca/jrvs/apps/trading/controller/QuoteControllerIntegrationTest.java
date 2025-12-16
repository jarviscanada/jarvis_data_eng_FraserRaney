package ca.jrvs.apps.trading.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class QuoteControllerIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  void quoteControllerIntegrationTest() {
    String ticker = "AAPL";

    ResponseEntity<FinnhubQuote> response =
        restTemplate.getForEntity("/iex/ticker/" + ticker, FinnhubQuote.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    FinnhubQuote quote = response.getBody();
    assertNotNull(quote, "Response body should not be null");

    assertNotNull(quote.getT(), "Timestamp should not be null");
    assertNotNull(quote.getC(), "Current price should not be null");
  }
}
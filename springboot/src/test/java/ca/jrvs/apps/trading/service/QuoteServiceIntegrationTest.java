package ca.jrvs.apps.trading.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import ca.jrvs.apps.trading.data.entity.Quote;
import ca.jrvs.apps.trading.data.repository.QuoteJpaRepository;
import ca.jrvs.apps.trading.data.repository.SecurityOrderJpaRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource("classpath:application-test.properties")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EnableJpaRepositories(basePackages = "ca.jrvs.apps.trading.data.repository")
@EntityScan(basePackages = "ca.jrvs.apps.trading.data.entity")
@ComponentScan(basePackages = {
    "ca.jrvs.apps.trading.service"
})
public class QuoteServiceIntegrationTest {


  @SpyBean
  private QuoteService quoteService;

  @Autowired
  private QuoteJpaRepository quoteRepo;

  @Autowired
  private SecurityOrderJpaRepository securityOrderRepo;

  @BeforeEach
  public void setup() {
    securityOrderRepo.deleteAll();
    quoteRepo.deleteAll();
  }

  @AfterEach
  public void cleanup() {
    quoteRepo.deleteAll();
  }

  @Test
  public void findFinnhubQuoteByTicker() {
    String ticker = "AAPL";

    FinnhubQuote quote = quoteService.findFinnhubQuoteByTicker(ticker);

    assertNotNull(quote, "Response body should not be null");

    assertNotNull(quote.getT(), "Timestamp should not be null");
    assertNotNull(quote.getC(), "Current price should not be null");
  }

  @Test
  public void updateMarketData() {
    List<String> tickers = Arrays.asList("AAPL", "MSFT");
    quoteService.saveQuotes(tickers);

    quoteService.updateMarketData();

    verify(quoteService, atLeastOnce())
        .saveQuotes(anyList());

    verify(quoteService, atLeastOnce())
        .findAllQuotes();

  }

  @Test
  public void saveQuotes() {
    List<String> tickers = Arrays.asList("AAPL", "MSFT");
    List<Quote> quotes = quoteService.saveQuotes(tickers);

    assertFalse(quotes.isEmpty());
    assertEquals(2, quotes.size());
    assertTrue(() -> quoteRepo.existsById(tickers.get(1)));
    assertTrue(() -> quoteRepo.existsById(tickers.get(0)));
    assertEquals(tickers.get(0), quotes.get(0).getTicker());

  }

  @Test
  public void saveQuote() {
    String ticker = "AAPL";

    Quote quote = quoteService.saveQuote(ticker);

    assertNotNull(quote);
    assertTrue(() -> quoteRepo.existsById(ticker));
    assertEquals(quote.getTicker(), ticker);
  }

  @Test
  public void findAllQuotes() {
    List<String> tickers = Arrays.asList("AAPL", "MSFT");
    List<Quote> saved = quoteService.saveQuotes(tickers);
    List<Quote> quotes = quoteService.findAllQuotes();

    assertFalse(quotes.isEmpty());
    assertEquals(2, quotes.size());
    assertEquals(tickers.get(0), quotes.get(0).getTicker());
    assertEquals(saved.get(0).getHigh(), quotes.get(0).getHigh());
    assertEquals(saved.get(1).getOpen(), quotes.get(1).getOpen());


  }

}

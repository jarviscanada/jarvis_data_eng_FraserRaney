package ca.jrvs.apps.trading.service;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import ca.jrvs.apps.trading.data.repository.QuoteJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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


  @Autowired
  private QuoteService quoteService;

  @Autowired
  private QuoteJpaRepository quoteRepo;

  @BeforeEach
  public void setup() {
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
  }

  @Test
  public void saveQuotes() {
  }

  @Test
  public void saveQuote() {
  }

  @Test
  public void findAllQuotes() {
  }


}

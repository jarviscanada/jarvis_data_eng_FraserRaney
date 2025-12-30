package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.trading.data.entity.Quote;
import ca.jrvs.apps.trading.data.repository.QuoteJpaRepository;
import ca.jrvs.apps.trading.data.repository.SecurityOrderJpaRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QuoteJpaRepoIntegrationTest {

  @Autowired
  SecurityOrderJpaRepository securityOrderRepo;

  @Autowired
  private QuoteJpaRepository quoteRepo;

  private Quote aapl;
  private Quote msft;

  @BeforeEach
  void setup() {
    securityOrderRepo.deleteAll();
    quoteRepo.deleteAll();

    aapl = new Quote();
    aapl.setTicker("AAPL");
    aapl.setLastPrice(150.0);
    aapl.setHigh(155.0);
    aapl.setLow(145.0);
    aapl.setOpen(148.0);
    aapl.setClose(149.0);

    msft = new Quote();
    msft.setTicker("MSFT");
    msft.setLastPrice(300.0);
    msft.setHigh(305.0);
    msft.setLow(295.0);
    msft.setOpen(298.0);
    msft.setClose(299.0);
  }

  @AfterEach
  void deleteAll() {
    quoteRepo.deleteAll();
  }

  @Test
  void save() {
    Quote saved = quoteRepo.save(aapl);

    assertNotNull(saved);
    assertEquals("AAPL", saved.getTicker());
    assertTrue(quoteRepo.existsById("AAPL"));
  }

  @Test
  void saveAll() {

    List<Quote> saved = quoteRepo.saveAll(Arrays.asList(aapl, msft));

    assertEquals(2, saved.size());
    assertTrue(quoteRepo.existsById("AAPL"));
    assertTrue(quoteRepo.existsById("MSFT"));
  }

  @Test
  void findAll() {
    quoteRepo.saveAll(Arrays.asList(aapl, msft));

    List<Quote> quotes = quoteRepo.findAll();

    assertEquals(2, quotes.size());
  }

  @Test
  void findById() {
    quoteRepo.save(aapl);

    Optional<Quote> found = quoteRepo.findById("AAPL");

    assertTrue(found.isPresent());
    assertEquals(150.0, found.get().getLastPrice());
  }

  @Test
  void deleteById() {
    quoteRepo.save(aapl);

    quoteRepo.deleteById("AAPL");

    assertFalse(quoteRepo.existsById("AAPL"));
  }

  @Test
  void count() {
    quoteRepo.saveAll(Arrays.asList(aapl, msft));

    long count = quoteRepo.count();

    assertEquals(2, count);
  }

}

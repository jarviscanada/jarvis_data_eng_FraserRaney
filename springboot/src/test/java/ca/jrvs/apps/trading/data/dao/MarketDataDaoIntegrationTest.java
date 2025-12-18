package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.data.config.MarketDataConfig;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.http.conn.HttpClientConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:application-test.properties")
public class MarketDataDaoIntegrationTest {

  private MarketDataDao marketDataDao;

  @Autowired
  private HttpClientConnectionManager cm;

  @Autowired
  private MarketDataConfig mc;

  @BeforeEach
  void setup() {
    marketDataDao = new MarketDataDao(cm, mc);
  }

  @Test
  void findById() {
    Optional<FinnhubQuote> quote = marketDataDao.findById("AAPL");
    assertTrue(quote.isPresent());
    assertNotNull(quote.get().getT(), "Timestamp should not be null");
    assertNotNull(quote.get().getC(), "Current price should not be null");

  }

  @Test
  void findAllById() {
    List<FinnhubQuote> results =
        marketDataDao.findAllById(Arrays.asList("AAPL", "MSFT"));

    assertEquals(2, results.size());

    assertNotNull(results.get(0).getT(), "Timestamp should not be null");
    assertNotNull(results.get(1).getC(), "Current price should not be null");

    try {
      marketDataDao.findAllById(Arrays.asList("AAPL", "FB2"));
      fail();
    } catch (IllegalArgumentException ex) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

}

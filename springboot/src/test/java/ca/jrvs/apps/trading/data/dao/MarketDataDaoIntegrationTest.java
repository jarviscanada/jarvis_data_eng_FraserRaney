package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import ca.jrvs.apps.trading.data.config.MarketDataConfig;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MarketDataDaoIntegrationTest {

  MarketDataDao marketDataDao;

  @BeforeEach
  void setup() {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    cm.setMaxTotal(50);
    cm.setDefaultMaxPerRoute(50);
    MarketDataConfig mc = new MarketDataConfig();
    mc.setHost("https://finnhub.io/api/v1");
    mc.setToken(System.getenv("FINNHUB_API_KEY"));
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

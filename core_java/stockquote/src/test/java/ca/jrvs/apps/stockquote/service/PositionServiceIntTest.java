package ca.jrvs.apps.stockquote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Position;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import java.sql.Timestamp;
import java.util.Optional;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PositionServiceIntTest {

  private PositionDao dao = new PositionDao();
  private PositionService positionService = new PositionService();
  private QuoteDao quoteDao = new QuoteDao();

  @BeforeAll
  public static void setUpClass() {
    BasicConfigurator.configure();
  }

  @BeforeEach
  public void setUp() {
    dao.deleteAll();
    quoteDao.deleteAll();
    Timestamp now = new Timestamp(System.currentTimeMillis());

    Quote q1 = new Quote();
    q1.setTicker("SYM1");
    q1.setOpen(10.0);
    q1.setHigh(15.0);
    q1.setLow(9.0);
    q1.setPrice(12.5);
    q1.setLatestTradingDay(now);
    q1.setPreviousClose(11.0);
    q1.setChange(1.5);
    q1.setChangePercent(13.6);
    q1.setTimestamp(now);
    quoteDao.save(q1);

    Quote q2 = new Quote();
    q2.setTicker("SYM2");
    q2.setOpen(20.0);
    q2.setHigh(25.0);
    q2.setLow(18.0);
    q2.setPrice(22.0);
    q2.setLatestTradingDay(now);
    q2.setPreviousClose(19.0);
    q2.setChange(3.0);
    q2.setChangePercent(15.8);
    q2.setTimestamp(now);
    quoteDao.save(q2);

    Position p1 = new Position();
    p1.setTicker("SYM2");
    p1.setValuePaid(1000);
    p1.setNumOfShares(100);
    dao.save(p1);

  }

  @AfterEach
  void tearDown() {
    dao.deleteAll();
  }

  @Test
  void buy_new() {
    Position result = positionService.buy("SYM1", 100, 1);
    Optional<Position> local = dao.findById("SYM1");

    assertTrue(local.isPresent());
    assertEquals(result.getTicker(), local.get().getTicker());
    assertEquals(result.getNumOfShares(), local.get().getNumOfShares());
    assertEquals(result.getValuePaid(), local.get().getValuePaid());
    assertEquals(result.getValuePaid(), 100.0);

  }

  @Test
  void buy_more() {
    Position result = positionService.buy("SYM2", 100, 1);
    Optional<Position> local = dao.findById("SYM2");

    assertTrue(local.isPresent());
    assertEquals(result.getTicker(), local.get().getTicker());
    assertEquals(result.getNumOfShares(), local.get().getNumOfShares());
    assertEquals(result.getValuePaid(), local.get().getValuePaid());
    assertEquals(result.getValuePaid(), 1100.0);
  }

  @Test
  void sell() {
    assertTrue(dao.findById("SYM2").isPresent());
    positionService.sell("SYM2");
    assertFalse(dao.findById("SYM2").isPresent());
  }
}

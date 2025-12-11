package ca.jrvs.apps.stockquote.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.stockquote.data.entity.Position;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import java.sql.Timestamp;
import java.util.Optional;
import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionDaoTest {

  QuoteDao quoteDao = new QuoteDao();
  PositionDao positionDao = new PositionDao();

  @BeforeEach
  void setUp() {
    BasicConfigurator.configure();
    positionDao.deleteAll();
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


  }

  @Test
  void save() {
    Position p = new Position();
    p.setTicker("SYM1");
    p.setNumOfShares(100);
    p.setValuePaid(1000.0);

    Position saved = positionDao.save(p);
    assertNotNull(saved);
    assertEquals("SYM1", saved.getTicker());
    assertEquals(100, saved.getNumOfShares());
    assertEquals(1000.0, saved.getValuePaid());

    p.setTicker(null);
    assertThrows(IllegalArgumentException.class, () -> positionDao.save(p));
  }

  @Test
  void update() {
    // create first
    Position p = new Position();
    p.setTicker("SYM2");
    p.setNumOfShares(50);
    p.setValuePaid(500.0);
    positionDao.save(p);

    // update
    p.setNumOfShares(75);
    p.setValuePaid(750.0);
    Position updated = positionDao.save(p);
    assertEquals(75, updated.getNumOfShares());
    assertEquals(750.0, updated.getValuePaid());
  }

  @Test
  void findById() {
    Position p = new Position();
    p.setTicker("SYM1");
    p.setNumOfShares(100);
    p.setValuePaid(1000.0);

    Position saved = positionDao.save(p);
    // Ensure DB row exists
    Optional<Position> maybe = positionDao.findById("SYM1");
    assertTrue(maybe.isPresent());

    assertThrows(IllegalArgumentException.class, () -> positionDao.findById(null));
  }

  @Test
  void findAll() {
    Position p = new Position();
    p.setTicker("SYM1");
    p.setNumOfShares(100);
    p.setValuePaid(1000.0);
    positionDao.save(p);

    p.setTicker("SYM2");
    p.setNumOfShares(50);
    p.setValuePaid(500.0);
    positionDao.save(p);

    Iterable<Position> all = positionDao.findAll();
    int count = 0;
    for (Position pp : all) {
      count++;
    }
    assertEquals(2, count);
  }

  @Test
  void deleteById() {
    Position p = new Position();
    p.setTicker("SYM1");
    p.setNumOfShares(100);
    p.setValuePaid(1000.0);
    positionDao.save(p);

    positionDao.deleteById("SYM1");
    assertFalse(positionDao.findById("SYM1").isPresent());
    assertThrows(IllegalArgumentException.class, () -> positionDao.deleteById(null));
  }

  @Test
  void deleteAll() {
    Position p = new Position();
    p.setTicker("SYM1");
    p.setNumOfShares(100);
    p.setValuePaid(1000.0);
    positionDao.save(p);

    p.setTicker("SYM2");
    p.setNumOfShares(50);
    p.setValuePaid(500.0);
    positionDao.save(p);

    positionDao.deleteAll();
    Iterable<Position> allAfter = positionDao.findAll();
    int postCount = 0;
    for (Position pp : allAfter) {
      postCount++;
    }
    assertEquals(0, postCount);
  }
}
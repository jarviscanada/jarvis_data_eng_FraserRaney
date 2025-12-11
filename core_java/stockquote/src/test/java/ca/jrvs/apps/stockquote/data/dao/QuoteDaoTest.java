package ca.jrvs.apps.stockquote.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.stockquote.data.entity.Quote;
import java.sql.Timestamp;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuoteDaoTest {

  private QuoteDao dao = new QuoteDao();

  @BeforeEach
  void setUp() {
    dao.deleteAll();
  }

  @AfterEach
  void tearDown() {
    dao.deleteAll();
  }

  @Test
  void save() {
    Quote q = new Quote();
    q.setTicker("TESTSYM");
    q.setOpen(123.45);
    q.setHigh(130.00);
    q.setLow(120.00);
    q.setPrice(125.00);
    Timestamp now = new Timestamp(System.currentTimeMillis());
    q.setLatestTradingDay(now);
    q.setPreviousClose(122.00);
    q.setChange(3.00);
    q.setChangePercent(2.46);
    q.setTimestamp(new Timestamp(System.currentTimeMillis()));

    Quote saved = dao.save(q);
    assertNotNull(saved);
    assertEquals("TESTSYM", saved.getTicker());
    assertEquals(125.00, saved.getPrice());

    Quote argTest = new Quote();
    argTest.setTicker(null);
    assertThrows(IllegalArgumentException.class, () -> dao.save(argTest));
  }

  @Test
  void update() {
    // first create
    Quote q = new Quote();
    q.setTicker("UPDSYM");
    q.setOpen(50.0);
    q.setHigh(55.0);
    q.setLow(45.0);
    q.setPrice(52.0);
    Timestamp now = new Timestamp(System.currentTimeMillis());
    q.setLatestTradingDay(now);
    q.setPreviousClose(48.0);
    q.setChange(4.0);
    q.setChangePercent(8.33);
    q.setTimestamp(now);

    dao.save(q);

    // update price/high/low
    q.setPrice(60.0);
    q.setHigh(65.0);
    q.setLow(55.0);
    q.setChange(12.0);
    q.setChangePercent(25.0);
    q.setTimestamp(new Timestamp(System.currentTimeMillis()));

    Quote updated = dao.save(q);
    assertEquals(60.0, updated.getPrice());
    assertEquals(65.0, updated.getHigh());
    assertEquals(55.0, updated.getLow());
  }

  @Test
  void findById() {
    Quote q = new Quote();
    q.setTicker("TESTSYM");
    q.setOpen(123.45);
    q.setHigh(130.00);
    q.setLow(120.00);
    q.setPrice(125.00);
    Timestamp now = new Timestamp(System.currentTimeMillis());
    q.setLatestTradingDay(now);
    q.setPreviousClose(122.00);
    q.setChange(3.00);
    q.setChangePercent(2.46);
    q.setTimestamp(new Timestamp(System.currentTimeMillis()));
    dao.save(q);
    Optional<Quote> maybe = dao.findById("TESTSYM");
    assertTrue(maybe.isPresent());
    Quote fromDb = maybe.get();
    assertEquals(130.00, fromDb.getHigh());
    assertEquals(120.00, fromDb.getLow());
  }

  @Test
  void findAll() {
    Quote q1 = new Quote();
    q1.setTicker("SYM1");
    q1.setOpen(10.0);
    q1.setHigh(15.0);
    q1.setLow(9.0);
    q1.setPrice(12.5);
    Timestamp now = new Timestamp(System.currentTimeMillis());
    q1.setLatestTradingDay(now);
    q1.setPreviousClose(11.0);
    q1.setChange(1.5);
    q1.setChangePercent(13.6);
    q1.setTimestamp(now);
    dao.save(q1);

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
    dao.save(q2);
    Iterable<Quote> all = dao.findAll();
    int count = 0;
    for (Quote q : all) {
      count++;
    }
    assertEquals(2, count);
  }

  @Test
  void deleteById() {
    Quote q1 = new Quote();
    q1.setTicker("SYM1");
    q1.setOpen(10.0);
    q1.setHigh(15.0);
    q1.setLow(9.0);
    q1.setPrice(12.5);
    Timestamp now = new Timestamp(System.currentTimeMillis());
    q1.setLatestTradingDay(now);
    q1.setPreviousClose(11.0);
    q1.setChange(1.5);
    q1.setChangePercent(13.6);
    q1.setTimestamp(now);
    dao.save(q1);

    dao.deleteById("SYM1");
    Optional<Quote> maybe = dao.findById("SYM1");
    assertFalse(maybe.isPresent());
  }

  @Test
  void deleteAll() {
    Quote q1 = new Quote();
    q1.setTicker("SYM1");
    q1.setOpen(10.0);
    q1.setHigh(15.0);
    q1.setLow(9.0);
    q1.setPrice(12.5);
    Timestamp now = new Timestamp(System.currentTimeMillis());
    q1.setLatestTradingDay(now);
    q1.setPreviousClose(11.0);
    q1.setChange(1.5);
    q1.setChangePercent(13.6);
    q1.setTimestamp(now);
    dao.save(q1);

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
    dao.save(q2);

    dao.deleteAll();
    Iterable<Quote> after = dao.findAll();
    int afterCount = 0;
    for (Quote q : after) {
      afterCount++;
    }
    assertEquals(0, afterCount);
  }
}
package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.Quote;
import ca.jrvs.apps.trading.data.entity.SecurityOrder;
import ca.jrvs.apps.trading.data.entity.Trader;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.QuoteJpaRepository;
import ca.jrvs.apps.trading.data.repository.SecurityOrderJpaRepository;
import ca.jrvs.apps.trading.data.repository.TraderJpaRepository;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SecurityOrderJpaRepoIntegrationTest {

  @Autowired
  private SecurityOrderJpaRepository securityOrderRepo;

  @Autowired
  private AccountJpaRepository accountRepo;

  @Autowired
  private QuoteJpaRepository quoteRepo;

  @Autowired
  private TraderJpaRepository traderRepo;

  private Account testAccount;
  private Quote testQuote;
  private Trader testTrader;
  private Trader savedTrader;

  @BeforeEach
  void setupDependencies() {
    testTrader = new Trader();
    testTrader.setCountry("Canada");
    testTrader.setFirstName("John");
    testTrader.setLastName("Doe");
    testTrader.setDateOfBirth(new Date(1990, 05, 14));
    testTrader.setEmail("john.doe@example.com");
    savedTrader = traderRepo.save(testTrader);

    testAccount = new Account();
    testAccount.setTraderId(savedTrader.getId());
    testAccount.setAmount(1000D);
    testAccount = accountRepo.save(testAccount);

    testQuote = new Quote();
    testQuote.setTicker("AAPL");
    testQuote.setLastPrice(150.0);
    testQuote.setHigh(155.0);
    testQuote.setLow(145.0);
    testQuote.setOpen(148.0);
    testQuote.setClose(149.0);
    testQuote = quoteRepo.save(testQuote);

    // Sanity checks to ensure dependencies are persisted
    assertNotNull(testAccount.getId());
    assertNotNull(testQuote.getTicker());
  }

  @Test
  void saveAndFindById() {
    SecurityOrder order = new SecurityOrder();
    order.setAccountId(testAccount.getId());
    order.setTicker(testQuote.getTicker());
    order.setStatus("FILLED");
    order.setSize(10);
    order.setPrice(150.0);

    SecurityOrder saved = securityOrderRepo.save(order);

    Optional<SecurityOrder> found = securityOrderRepo.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals("FILLED", found.get().getStatus());
  }

  @Test
  void existsById() {
    SecurityOrder order = new SecurityOrder();
    order.setAccountId(testAccount.getId());
    order.setTicker(testQuote.getTicker());
    order.setStatus("PENDING");
    order.setSize(5);

    SecurityOrder saved = securityOrderRepo.save(order);
    assertTrue(securityOrderRepo.existsById(saved.getId()));
  }

  @Test
  void findAll() {
    SecurityOrder order1 = new SecurityOrder();
    order1.setAccountId(testAccount.getId());
    order1.setTicker(testQuote.getTicker());
    order1.setStatus("PENDING");
    order1.setSize(5);

    SecurityOrder order2 = new SecurityOrder();
    order2.setAccountId(testAccount.getId());
    order2.setTicker(testQuote.getTicker());
    order2.setStatus("FILLED");
    order2.setSize(3);

    securityOrderRepo.save(order1);
    securityOrderRepo.save(order2);

    List<SecurityOrder> orders = securityOrderRepo.findAll();
    assertEquals(2, orders.size());
  }

  @Test
  void findAllById() {
    SecurityOrder order1 = new SecurityOrder();
    order1.setAccountId(testAccount.getId());
    order1.setTicker(testQuote.getTicker());
    order1.setStatus("OPEN");
    order1.setSize(2);

    SecurityOrder order2 = new SecurityOrder();
    order2.setAccountId(testAccount.getId());
    order2.setTicker(testQuote.getTicker());
    order2.setStatus("CLOSED");
    order2.setSize(8);

    SecurityOrder saved1 = securityOrderRepo.save(order1);
    SecurityOrder saved2 = securityOrderRepo.save(order2);

    List<SecurityOrder> ordersByIds =
        securityOrderRepo.findAllById(Arrays.asList(saved1.getId(), saved2.getId()));

    assertEquals(2, ordersByIds.size());
  }

  @Test
  void deleteByIdAndDeleteAll() {
    SecurityOrder order = new SecurityOrder();
    order.setAccountId(testAccount.getId());
    order.setTicker(testQuote.getTicker());
    order.setStatus("PENDING");
    order.setSize(10);

    SecurityOrder saved = securityOrderRepo.save(order);
    assertTrue(securityOrderRepo.existsById(saved.getId()));

    securityOrderRepo.deleteById(saved.getId());
    assertFalse(securityOrderRepo.existsById(saved.getId()));

    // Now test deleteAll
    SecurityOrder another = new SecurityOrder();
    another.setAccountId(testAccount.getId());
    another.setTicker(testQuote.getTicker());
    another.setStatus("FILLED");
    another.setSize(4);

    securityOrderRepo.save(another);
    assertTrue(securityOrderRepo.count() > 0);

    securityOrderRepo.deleteAll();
    assertEquals(0, securityOrderRepo.count());
  }

  @Test
  void count() {
    SecurityOrder order1 = new SecurityOrder();
    order1.setAccountId(testAccount.getId());
    order1.setTicker(testQuote.getTicker());
    order1.setStatus("OPEN");
    order1.setSize(1);

    SecurityOrder order2 = new SecurityOrder();
    order2.setAccountId(testAccount.getId());
    order2.setTicker(testQuote.getTicker());
    order2.setStatus("FILLED");
    order2.setSize(2);

    securityOrderRepo.save(order1);
    securityOrderRepo.save(order2);

    assertEquals(2, securityOrderRepo.count());
  }
}

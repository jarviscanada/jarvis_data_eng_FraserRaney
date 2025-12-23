package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.Position;
import ca.jrvs.apps.trading.data.entity.PositionKey;
import ca.jrvs.apps.trading.data.entity.Quote;
import ca.jrvs.apps.trading.data.entity.SecurityOrder;
import ca.jrvs.apps.trading.data.entity.Trader;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.PositionRepository;
import ca.jrvs.apps.trading.data.repository.QuoteJpaRepository;
import ca.jrvs.apps.trading.data.repository.SecurityOrderJpaRepository;
import ca.jrvs.apps.trading.data.repository.TraderJpaRepository;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PositionRepoIntegrationTest {


  @Autowired
  private PositionRepository positionRepo;


  @Autowired
  private SecurityOrderJpaRepository securityOrderRepo;

  @Autowired
  private AccountJpaRepository accountRepo;

  @Autowired
  private QuoteJpaRepository quoteRepo;

  @Autowired
  private TraderJpaRepository traderRepo;

  private Account testAccount;
  private Quote aapl;
  private Quote msft;
  private Trader testTrader;
  private Trader savedTrader;
  private PositionKey key1;
  private PositionKey key2;

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

    aapl = new Quote();
    aapl.setTicker("AAPL");
    aapl.setLastPrice(150.0);
    aapl.setHigh(155.0);
    aapl.setLow(145.0);
    aapl.setOpen(148.0);
    aapl.setClose(149.0);
    aapl = quoteRepo.save(aapl);

    msft = new Quote();
    msft.setTicker("MSFT");
    msft.setLastPrice(300.0);
    msft.setHigh(305.0);
    msft.setLow(295.0);
    msft.setOpen(298.0);
    msft.setClose(299.0);

    SecurityOrder order1 = new SecurityOrder();
    order1.setAccountId(testAccount.getId());
    order1.setTicker(aapl.getTicker());
    order1.setStatus("FILLED");
    order1.setSize(5);

    SecurityOrder order2 = new SecurityOrder();
    order2.setAccountId(testAccount.getId());
    order2.setTicker(msft.getTicker());
    order2.setStatus("FILLED");
    order2.setSize(3);

    securityOrderRepo.save(order1);
    securityOrderRepo.save(order2);
  }

  @Test
  public void testFindAll() {
    List<Position> all = positionRepo.findAll();
    assertNotNull(all);
    assertTrue(all.size() == 2);  // Adjust based on setup
  }

  @Test
  public void testFindByIdAndExistsById() {
    // Create key for lookup
    PositionKey id = new PositionKey(testAccount.getId(), aapl.getTicker());

    Optional<Position> found = positionRepo.findById(id);
    assertTrue(found.isPresent());

    assertTrue(positionRepo.existsById(id));
  }

  @Test
  public void testCountAndFindAllById() {
    List<Position> all = positionRepo.findAll();
    long total = positionRepo.count();
    assertEquals(all.size(), total);

    List<PositionKey> ids =
        all.stream().map(Position::getId).collect(Collectors.toList());

    List<Position> subset = positionRepo.findAllById(ids);
    assertEquals(ids.size(), subset.size());
  }
}

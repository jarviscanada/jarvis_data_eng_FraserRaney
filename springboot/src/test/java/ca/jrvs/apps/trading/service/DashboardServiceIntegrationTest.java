package ca.jrvs.apps.trading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.PortfolioView;
import ca.jrvs.apps.trading.data.entity.Position;
import ca.jrvs.apps.trading.data.entity.SecurityOrder;
import ca.jrvs.apps.trading.data.entity.Trader;
import ca.jrvs.apps.trading.data.entity.TraderAccountView;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.SecurityOrderJpaRepository;
import ca.jrvs.apps.trading.data.repository.TraderJpaRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ComponentScan(basePackages = {
    "ca.jrvs.apps.trading.data.dao",
    "ca.jrvs.apps.trading.service"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class DashboardServiceIntegrationTest {

  @Autowired
  private DashboardService dashboardService;

  @Autowired
  private TraderJpaRepository traderRepo;

  @Autowired
  private AccountJpaRepository accountRepo;

  @Autowired
  private SecurityOrderJpaRepository securityOrderRepo;

  private Trader trader;
  private Account account;

  @BeforeEach
  void setup() {
    securityOrderRepo.deleteAll();
    accountRepo.deleteAll();
    traderRepo.deleteAll();

    trader = new Trader();
    trader.setFirstName("John");
    trader.setLastName("Doe");
    trader.setCountry("CA");
    trader.setEmail("john@jrvs.ca");
    trader.setDateOfBirth(LocalDate.of(1990, 1, 1));
    trader = traderRepo.save(trader);

    account = new Account();
    account.setTraderId(trader.getId());
    account.setAmount(10000.0);
    account = accountRepo.save(account);

    SecurityOrder order1 = new SecurityOrder();
    order1.setAccountId(account.getId());
    order1.setTicker("AAPL");
    order1.setSize(10);
    order1.setStatus("FILLED");
    order1.setPrice(150.0);

    SecurityOrder order2 = new SecurityOrder();
    order2.setAccountId(account.getId());
    order2.setTicker("MSFT");
    order2.setSize(5);
    order2.setStatus("FILLED");
    order2.setPrice(300.0);

    securityOrderRepo.saveAll(Arrays.asList(order1, order2));
  }

  @AfterEach
  void cleanup() {
    securityOrderRepo.deleteAll();
    accountRepo.deleteAll();
    traderRepo.deleteAll();
  }

  @Test
  void getTraderAccount_success() {
    TraderAccountView view =
        dashboardService.getTraderAccount(trader.getId());

    assertNotNull(view);
    assertEquals(trader.getId(), view.getTrader().getId());
    assertEquals(account.getId(), view.getAccount().getId());
    assertEquals(10000.0, view.getAccount().getAmount());
  }

  @Test
  void getPortfolioViewByTraderId_success() {
    PortfolioView portfolio =
        dashboardService.getPortfolioViewByTraderId(trader.getId());

    assertNotNull(portfolio);
    assertEquals(trader.getId(), portfolio.getTrader().getId());

    List<Position> positions = portfolio.getPositions();
    assertEquals(2, positions.size());

    assertTrue(
        positions.stream().anyMatch(p -> p.getId().getTicker().equals("AAPL"))
    );
    assertTrue(
        positions.stream().anyMatch(p -> p.getId().getTicker().equals("MSFT"))
    );
  }

  @Test
  void getTraderAccount_invalidTraderId() {
    assertThrows(IllegalArgumentException.class,
        () -> dashboardService.getTraderAccount(9999));
  }

  @Test
  void getPortfolioView_invalidTraderId() {
    assertThrows(IllegalArgumentException.class,
        () -> dashboardService.getPortfolioViewByTraderId(9999));
  }
}

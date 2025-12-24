package ca.jrvs.apps.trading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.Trader;
import ca.jrvs.apps.trading.data.entity.TraderAccountView;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.TraderJpaRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

@DataJpaTest
@ComponentScan(basePackages = {
    "ca.jrvs.apps.trading.data.dao",
    "ca.jrvs.apps.trading.service"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TraderAccountServiceIntegrationTest {

  @Autowired
  private TraderAccountService traderAccountService;

  @Autowired
  private TraderJpaRepository traderRepo;

  @Autowired
  private AccountJpaRepository accountRepo;

  @AfterEach
  void cleanup() {
    accountRepo.deleteAll();
    traderRepo.deleteAll();
  }

  @Test
  void createTraderAndAccount_success() {
    Trader trader = createTrader();

    TraderAccountView view =
        traderAccountService.createTraderAndAccount(trader);

    assertNotNull(view);
    assertNotNull(view.getTrader().getId());

    Account account =
        accountRepo.getAccountByTraderId(view.getTrader().getId());

    assertNotNull(account);
    assertEquals(0.0, account.getAmount());
  }

  @Test
  void createTraderAndAccount_nullTrader() {
    assertThrows(IllegalArgumentException.class,
        () -> traderAccountService.createTraderAndAccount(null));
  }

  @Test
  void deposit_success() {
    Trader trader = createTrader();
    TraderAccountView view =
        traderAccountService.createTraderAndAccount(trader);

    Trader savedTrader = traderRepo.findById(view.getTrader().getId()).get();

    Account updated =
        traderAccountService.deposit(savedTrader.getId(), 100.0);

    assertEquals(100.0, updated.getAmount());
  }

  @Test
  void deposit_negativeAmount() {
    assertThrows(IllegalArgumentException.class,
        () -> traderAccountService.deposit(1, -10.0));
  }

  @Test
  void withdraw_success() {
    Trader trader = createTrader();
    TraderAccountView view =
        traderAccountService.createTraderAndAccount(trader);

    Trader savedTrader = traderRepo.findById(view.getTrader().getId()).get();

    traderAccountService.deposit(savedTrader.getId(), 100.0);

    Account updated =
        traderAccountService.withdraw(savedTrader.getId(), 40.0);

    assertEquals(60.0, updated.getAmount());
  }


  @Test
  void deleteTraderById_success() {
    Trader trader = createTrader();
    TraderAccountView view =
        traderAccountService.createTraderAndAccount(trader);

    Trader savedTrader = traderRepo.findById(view.getTrader().getId()).get();

    traderAccountService.deleteTraderById(savedTrader.getId());

    assertFalse(traderRepo.existsById(savedTrader.getId()));
    assertNull(accountRepo.getAccountByTraderId(savedTrader.getId()));
  }

  @Test
  void deleteTraderById_nonZeroBalance() {
    Trader trader = createTrader();
    TraderAccountView view =
        traderAccountService.createTraderAndAccount(trader);

    Trader savedTrader = traderRepo.findById(view.getTrader().getId()).get();
    traderAccountService.deposit(savedTrader.getId(), 10.0);

    assertThrows(IllegalArgumentException.class,
        () -> traderAccountService.deleteTraderById(savedTrader.getId()));
  }

  private Trader createTrader() {
    Trader trader = new Trader();
    trader.setFirstName("John");
    trader.setLastName("Doe");
    trader.setDateOfBirth(LocalDate.of(1990, 01, 01));
    trader.setCountry("CA");
    trader.setEmail("john.doe@test.com");
    return trader;
  }
}

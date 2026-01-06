package ca.jrvs.apps.trading.service;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.Trader;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.TraderJpaRepository;
import java.time.LocalDate;
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
public class AccountServiceIntegrationTest {

  @Autowired
  private AccountJpaRepository accountRepository;

  @Autowired
  private TraderJpaRepository traderRepository;

  @Autowired
  private AccountService accountService;

  private Trader trader;

  @BeforeEach
  void setUp() {
    trader = new Trader();
    trader.setCountry("Canada");
    trader.setFirstName("John");
    trader.setLastName("Doe");
    trader.setDateOfBirth(LocalDate.of(1990, 05, 14));
    trader.setEmail("john.doe@example.com");
  }


  @Test
  public void testDeleteAccount() {
    Trader saved = traderRepository.save(trader);

    Account acc = new Account();
    acc.setTraderId(saved.getId());
    acc.setAmount(0L);
    Account saved2 = accountRepository.save(acc);

    assertTrue(traderRepository.existsById(saved.getId()));
    assertTrue(accountRepository.existsById(saved2.getId()));

    accountService.deleteAccountByTraderId(saved.getId());

    assertFalse(accountRepository.findById(saved2.getId()).isPresent());
  }

}


package ca.jrvs.apps.trading.service;


import static org.junit.jupiter.api.Assertions.assertFalse;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
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
public class AccountServiceIntegrationTest {

  @Autowired
  private AccountJpaRepository accountRepository;

  @Autowired
  private AccountService accountService;


  @Test
  public void testDeleteAccount() {
    Account acc = new Account();
    acc.setId(1);
    acc.setTraderId(1);
    acc.setAmount(0L);
    Account saved = accountRepository.save(acc);

    accountService.deleteAccountByTraderId(1);

    assertFalse(accountRepository.findById(saved.getId()).isPresent());
  }


}


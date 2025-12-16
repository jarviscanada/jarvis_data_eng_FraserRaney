package ca.jrvs.apps.trading.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
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


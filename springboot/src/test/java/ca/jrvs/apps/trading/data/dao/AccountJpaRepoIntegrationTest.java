package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.Trader;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
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
public class AccountJpaRepoIntegrationTest {

  @Autowired
  private AccountJpaRepository accountRepo;


  @Autowired
  private TraderJpaRepository traderRepo;

  private Trader testTrader;
  private Account acc1;
  private Account acc2;

  @BeforeEach
  void setup() {
    testTrader = new Trader();
    testTrader.setCountry("Canada");
    testTrader.setFirstName("John");
    testTrader.setLastName("Doe");
    testTrader.setDateOfBirth(new Date(1990, 05, 14));
    testTrader.setEmail("john.doe@example.com");
    testTrader = traderRepo.save(testTrader);
    // Create two sample accounts
    acc1 = new Account();
    acc1.setTraderId(testTrader.getId());
    acc1.setAmount(500.0);

    acc2 = new Account();
    acc2.setTraderId(testTrader.getId());
    acc2.setAmount(1000.0);
  }

  @Test
  void saveAndFindById() {
    Account saved = accountRepo.save(acc1);

    Optional<Account> found = accountRepo.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals(saved.getTraderId(), found.get().getTraderId());
  }

  @Test
  void existsById() {
    Account saved = accountRepo.save(acc1);
    assertTrue(accountRepo.existsById(saved.getId()));
  }

  @Test
  void findAll() {
    accountRepo.save(acc1);
    accountRepo.save(acc2);

    List<Account> all = accountRepo.findAll();
    assertEquals(2, all.size());
  }

  @Test
  void findAllById() {
    Account s1 = accountRepo.save(acc1);
    Account s2 = accountRepo.save(acc2);

    List<Account> accounts = accountRepo.findAllById(Arrays.asList(s1.getId(), s2.getId()));
    assertEquals(2, accounts.size());
  }

  @Test
  void count() {
    accountRepo.save(acc1);
    accountRepo.save(acc2);

    long count = accountRepo.count();
    assertEquals(2, count);
  }

  @Test
  void deleteByIdAndDeleteAll() {
    Account s1 = accountRepo.save(acc1);

    assertTrue(accountRepo.existsById(s1.getId()));

    accountRepo.deleteById(s1.getId());
    assertFalse(accountRepo.existsById(s1.getId()));

    accountRepo.save(acc2);
    assertTrue(accountRepo.count() > 0);

    accountRepo.deleteAll();
    assertEquals(0, accountRepo.count());
  }


}

package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ca.jrvs.apps.trading.data.entity.Trader;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.SecurityOrderJpaRepository;
import ca.jrvs.apps.trading.data.repository.TraderJpaRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TraderJpaRepoIntegrationTest {

  @Autowired
  private TraderJpaRepository traderRepo;

  @Autowired
  private SecurityOrderJpaRepository securityOrderRepo;

  @Autowired
  private AccountJpaRepository accountRepo;

  private Trader trader;

  @BeforeEach
  void setup() {
    securityOrderRepo.deleteAll();
    accountRepo.deleteAll();
    traderRepo.deleteAll();

    trader = new Trader();
    trader.setFirstName("John");
    trader.setLastName("Doe");
    trader.setDateOfBirth(LocalDate.of(1990, 1, 1));
    trader.setCountry("Canada");
    trader.setEmail("john.doe@example.com");
  }

  @Test
  void saveAndFindById() {
    Trader saved = traderRepo.save(trader);
    assertNotNull(saved.getId());

    Optional<Trader> found = traderRepo.findById(saved.getId());
    assertTrue(found.isPresent());
    assertEquals("John", found.get().getFirstName());
  }

  @Test
  void existsById() {
    Trader saved = traderRepo.save(trader);
    assertTrue(traderRepo.existsById(saved.getId()));
  }

  @Test
  void findAll() {
    traderRepo.save(trader);
    assertFalse(traderRepo.findAll().isEmpty());
  }

  @Test
  void count() {
    traderRepo.save(trader);
    long c = traderRepo.count();
    assertEquals(1, c);
  }

  @Test
  void deleteById() {
    Trader saved = traderRepo.save(trader);
    traderRepo.deleteById(saved.getId());
    assertFalse(traderRepo.existsById(saved.getId()));
  }

  @Test
  void deleteAll() {
    traderRepo.save(trader);
    traderRepo.deleteAll();
    assertEquals(0, traderRepo.count());
  }
}
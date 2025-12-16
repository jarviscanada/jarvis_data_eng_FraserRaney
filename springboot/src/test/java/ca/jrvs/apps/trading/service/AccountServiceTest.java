package ca.jrvs.apps.trading.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AccountServiceTest {

  @Mock
  private AccountJpaRepository mockAccountRepo;

  private AccountService accountService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    accountService = new AccountService(mockAccountRepo);
  }

  @Test
  public void deleteAccountByTraderId_balanceZero_deletesAccount() {
    // given
    Integer traderId = 1;
    Account account = new Account();
    account.setId(100);
    account.setTraderId(traderId);
    account.setAmount(0L);

    when(mockAccountRepo.getAccountByTraderId(traderId)).thenReturn(account);

    // when
    accountService.deleteAccountByTraderId(traderId);

    // then
    verify(mockAccountRepo, times(1)).deleteById(account.getId());
  }

  @Test
  public void deleteAccountByTraderId_balanceNotZero_throwsIllegalArgumentException() {
    // given
    Integer traderId = 2;
    Account account = new Account();
    account.setId(200);
    account.setTraderId(traderId);
    account.setAmount(500L); // non-zero balance

    when(mockAccountRepo.getAccountByTraderId(traderId)).thenReturn(account);

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class,
            () -> accountService.deleteAccountByTraderId(traderId));

    assertEquals("Balance not 0", exception.getMessage());

    verify(mockAccountRepo, never()).deleteById(any());
  }
}


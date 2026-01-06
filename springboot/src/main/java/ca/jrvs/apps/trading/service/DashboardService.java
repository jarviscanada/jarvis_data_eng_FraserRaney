package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.PortfolioView;
import ca.jrvs.apps.trading.data.entity.Position;
import ca.jrvs.apps.trading.data.entity.Trader;
import ca.jrvs.apps.trading.data.entity.TraderAccountView;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.PositionRepository;
import ca.jrvs.apps.trading.data.repository.TraderJpaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

  private AccountJpaRepository accountRepo;
  private TraderJpaRepository traderRepo;
  private PositionRepository positionRepo;

  @Autowired
  public DashboardService(AccountJpaRepository accountRepo,
      TraderJpaRepository traderRepo,
      PositionRepository positionRepo) {
    this.accountRepo = accountRepo;
    this.traderRepo = traderRepo;
    this.positionRepo = positionRepo;
  }

  /**
   * Create and return a traderAccountView by trader ID - get trader account by id - get trader info
   * by id - create and return a traderAccountView
   *
   * @param traderId must not be null
   * @return traderAccountView
   * @throws IllegalArgumentException if traderId is null or not found
   */
  public TraderAccountView getTraderAccount(Integer traderId) {
    if (traderId == null) {
      throw new IllegalArgumentException("traderId cannot be null");
    }

    Trader trader = findTraderById(traderId);
    Account account = findAccountByTraderId(traderId);

    return new TraderAccountView(account, trader);

  }

  /**
   * Create and return portfolioView by trader ID - get account by trader id - get positions by
   * account id - create and return a portfolioView
   *
   * @param traderId must not be null
   * @return portfolioView
   * @throws IllegalArgumentException if traderId is null or not found
   */
  public PortfolioView getPortfolioViewByTraderId(Integer traderId) {
    if (traderId == null) {
      throw new IllegalArgumentException("traderId cannot be null");
    }

    Trader trader = findTraderById(traderId);
    Account account = findAccountByTraderId(traderId);
    List<Position> positions = positionRepo.findAllByIdAccountId(account.getId());

    return new PortfolioView(trader, positions);

  }

  /**
   * Helper method to find a trader's corresponding account
   *
   * @param traderId
   * @return
   * @throws IllegalArgumentException if traderId is not found
   */
  private Account findAccountByTraderId(Integer traderId) {
    Account account = accountRepo.getAccountByTraderId(traderId);
    if (account == null) {
      throw new IllegalArgumentException("Account not found for traderId=" + traderId);
    }
    return account;

  }

  private Trader findTraderById(Integer traderId) {
    return traderRepo.findById(traderId)
        .orElseThrow(() -> new IllegalArgumentException("Trader not found"));
  }


}

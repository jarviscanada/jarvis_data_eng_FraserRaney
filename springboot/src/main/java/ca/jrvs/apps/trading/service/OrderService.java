package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.MarketOrder;
import ca.jrvs.apps.trading.data.entity.Position;
import ca.jrvs.apps.trading.data.entity.PositionKey;
import ca.jrvs.apps.trading.data.entity.SecurityOrder;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.PositionRepository;
import ca.jrvs.apps.trading.data.repository.SecurityOrderJpaRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
  private static final String exceptionFormat = "exception in %s, message %s";


  @Autowired
  AccountJpaRepository accountRepo;

  @Autowired
  QuoteService quoteService;

  @Autowired
  SecurityOrderJpaRepository securityOrderRepo;

  @Autowired
  PositionRepository positionRepo;

  @Autowired
  TraderAccountService traderAccountService;

  /**
   * Execute a market order - validate the order (e.g. size and ticker) - create a securityOrder -
   * handle buy or sell orders - buy order : check account balance - sell order : check position for
   * the ticker/symbol - do not forget to update the securityOrder.status - save and return
   * securityOrder
   * <p>
   * NOTE: you are encouraged to make some helper methods (protected or private)
   *
   * @param orderData market order
   * @return SecurityOrder from security_order table
   * @throws DataAccessException      if unable to get data from DAO
   * @throws IllegalArgumentException for invalid inputs
   */
  public SecurityOrder executeMarketOrder(MarketOrder orderData) {

    if (orderData == null) {
      throw new IllegalArgumentException("MarketOrder cannot be null");
    }
    if (orderData.getSize() <= 0) {
      throw new IllegalArgumentException("Order size must be positive");
    }
    if (orderData.getTicker() == null || orderData.getTicker().trim().isEmpty()) {
      throw new IllegalArgumentException("Ticker cannot be empty");
    }

    Account account = accountRepo.getAccountByTraderId(orderData.getTraderId());
    if (account == null) {
      throw new IllegalArgumentException("Account not found");
    }

    SecurityOrder securityOrder = new SecurityOrder();
    securityOrder.setAccountId(account.getId());
    securityOrder.setTicker(orderData.getTicker());
    securityOrder.setSize(orderData.getSize());
    securityOrder.setStatus("CREATED");

    if (orderData.getOption() == MarketOrder.Option.BUY) {
      handleBuyMarketOrder(orderData, securityOrder, account);
    } else {
      handleSellMarketOrder(orderData, securityOrder, account);
    }

    return securityOrderRepo.save(securityOrder);
  }

  /**
   * Helper method to execute a buy order
   *
   * @param marketOrder   user order
   * @param securityOrder to be saved in database
   * @param account       account
   */
  protected void handleBuyMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder,
      Account account) {
    double price = quoteService.findFinnhubQuoteByTicker(marketOrder.getTicker()).getH();
    double totalCost = price * marketOrder.getSize();

    if (account.getAmount() < totalCost) {
      securityOrder.setStatus("REJECTED");
      throw new IllegalArgumentException("Insufficient funds");
    }

    account.setAmount(account.getAmount() - totalCost);
    accountRepo.save(account);

    securityOrder.setPrice(price);
    securityOrder.setStatus("FILLED");
  }

  /**
   * Helper method to execute a sell order
   *
   * @param marketOrder   user order
   * @param securityOrder to be saved in database
   * @param account       account
   */
  protected void handleSellMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder,
      Account account) {
    long position;
    Optional<Position> positionOpt = positionRepo
        .findById(new PositionKey(account.getId(), marketOrder.getTicker()));

    if (positionOpt.isPresent()) {
      position = positionOpt.get().getPosition();
    } else {
      position = 0L;
    }

    if (position < marketOrder.getSize()) {
      securityOrder.setStatus("REJECTED");
      throw new IllegalArgumentException("Insufficient position");
    }

    double price = quoteService.findFinnhubQuoteByTicker(marketOrder.getTicker()).getL();
    double proceeds = price * marketOrder.getSize();

    account.setAmount(account.getAmount() + proceeds);
    accountRepo.save(account);

    securityOrder.setPrice(price);
    securityOrder.setStatus("FILLED");
  }
}

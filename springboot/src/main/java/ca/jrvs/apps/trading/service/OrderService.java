package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.data.dao.MarketDataDao;
import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.MarketOrder;
import ca.jrvs.apps.trading.data.entity.SecurityOrder;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.PositionRepository;
import ca.jrvs.apps.trading.data.repository.SecurityOrderJpaRepository;
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
  AccountJpaRepository accountJpaRepo;

  @Autowired
  QuoteService quoteService;

  @Autowired
  SecurityOrderJpaRepository securityOrderJpaRepo;

  @Autowired
  PositionRepository positionRepo;

  @Autowired
  TraderAccountService traderAccountService;

  /**
   * Execute a market order
   * - validate the order (e.g. size and ticker)
   * - create a securityOrder
   * - handle buy or sell orders
   * 	- buy order : check account balance
   * 	- sell order : check position for the ticker/symbol
   * 	- do not forget to update the securityOrder.status
   * - save and return securityOrder
   *
   * NOTE: you are encouraged to make some helper methods (protected or private)
   *
   * @param orderData market order
   * @return SecurityOrder from security_order table
   * @throws DataAccessException if unable to get data from DAO
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

    //TODO
    return null;
  }

  /**
   * Helper method to execute a buy order
   *
   * @param marketOrder user order
   * @param securityOrder to be saved in database
   * @param account account
   */
  protected void handleBuyMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder, Account account) {

    //TODO
  }

  /**
   * Helper method to execute a sell order
   *
   * @param marketOrder user order
   * @param securityOrder to be saved in database
   * @param account account
   */
  protected void handleSellMarketOrder(MarketOrder marketOrder, SecurityOrder securityOrder, Account account) {
    //TODO
  }
}

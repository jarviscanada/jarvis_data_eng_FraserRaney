package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.entity.Position;
import java.util.Optional;

public class PositionService {

  private PositionDao dao;

  public PositionService(PositionDao dao) {
    this.dao = dao;
  }

  public PositionService() {
    this(new PositionDao());

  }

  /**
   * Processes a buy order and updates the database accordingly
   *
   * @param ticker
   * @param numberOfShares
   * @param price
   * @return The position in our database after processing the buy
   */
  public Position buy(String ticker, int numberOfShares, double price) {
    if (ticker == null || ticker.trim().isEmpty()) {
      throw new IllegalArgumentException("ticker is null or empty");
    }
    if (numberOfShares <= 0) {
      throw new IllegalArgumentException("numberOfShares must be > 0");
    }
    if (price <= 0) {
      throw new IllegalArgumentException("price must be > 0");
    }

    String symbol = ticker.trim().toUpperCase();
    Optional<Position> opt = dao.findById(symbol);
    Position position;
    double deltaValue = numberOfShares * price;

    if (opt.isPresent()) {
      position = opt.get();
      int newShares = position.getNumOfShares() + numberOfShares;
      double newValuePaid = position.getValuePaid() + deltaValue;
      position.setNumOfShares(newShares);
      position.setValuePaid(newValuePaid);
    } else {
      position = new Position();
      position.setTicker(symbol);
      position.setNumOfShares(numberOfShares);
      position.setValuePaid(deltaValue);
    }

    return dao.save(position);

  }

  /**
   * Sells all shares of the given ticker symbol
   *
   * @param ticker
   */
  public void sell(String ticker) {
    if (ticker == null || ticker.trim().isEmpty()) {
      throw new IllegalArgumentException("ticker is null or empty");
    }

    String symbol = ticker.trim().toUpperCase();
    Optional<Position> opt = dao.findById(symbol);
    if (!opt.isPresent()) {
      throw new IllegalArgumentException("No position found for ticker: " + symbol);
    }
    dao.deleteById(symbol);
  }

}

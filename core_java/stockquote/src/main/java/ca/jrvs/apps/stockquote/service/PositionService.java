package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.entity.Position;

public class PositionService {

  private PositionDao dao;

  /**
   * Processes a buy order and updates the database accordingly
   * @param ticker
   * @param numberOfShares
   * @param price
   * @return The position in our database after processing the buy
   */
  public Position buy(String ticker, int numberOfShares, double price) {
    //TO DO
    return new Position();
  }

  /**
   * Sells all shares of the given ticker symbol
   * @param ticker
   */
  public void sell(String ticker) {
    //TO DO
  }

}

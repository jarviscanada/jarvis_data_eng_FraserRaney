package ca.jrvs.apps.trading.data.entity;

import java.util.List;

public class PortfolioView {

  private Trader trader;
  private List<Position> positions;

  public PortfolioView(Trader trader, List<Position> positions) {
    this.trader = trader;
    this.positions = positions;
  }

  public List<Position> getPositions() {
    return positions;
  }

  public Trader getTrader() {
    return trader;
  }

  @Override
  public String toString() {
    return "PortfolioView{" +
        "trader=" + trader +
        ", positions=" + positions.toString() +
        '}';
  }
}

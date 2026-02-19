package ca.jrvs.apps.trading.data.entity;

public class MarketOrder {

  private String ticker;
  private int size;
  private int traderId;
  private Option option;

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getTraderId() {
    return traderId;
  }

  public void setTraderId(int traderId) {
    this.traderId = traderId;
  }

  @Override
  public String toString() {
    return "MarketOrder{" +
        "ticker='" + ticker + '\'' +
        ", size=" + size +
        ", traderId=" + traderId +
        '}';
  }

  public Option getOption() {
    return option;
  }

  public void setOption(Option option) {
    this.option = option;
  }

  public enum Option {BUY, SELL}

}

package ca.jrvs.apps.trading.data.entity;

public class TraderAccountView {
  private Account account;
  private Trader trader;

  public TraderAccountView(Account account, Trader trader) {
    this.account = account;
    this.trader = trader;
  }

  public Account getAccount() {
    return account;
  }

  public Trader getTrader() {
    return trader;
  }

  @Override
  public String toString() {
    return "TraderAccountView{" +
        "account=" + account +
        ", trader=" + trader +
        '}';
  }
}

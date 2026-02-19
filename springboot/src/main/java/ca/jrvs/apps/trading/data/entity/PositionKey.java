package ca.jrvs.apps.trading.data.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class PositionKey implements Serializable {

  @Column(name = "account_id")
  private Integer accountId;

  @Column(name = "ticker")
  private String ticker;

  public PositionKey() {
  }

  public PositionKey(Integer accountId, String ticker) {
    this.accountId = accountId;
    this.ticker = ticker;
  }

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PositionKey)) {
      return false;
    }
    PositionKey that = (PositionKey) o;
    return Objects.equals(accountId, that.accountId) &&
        Objects.equals(ticker, that.ticker);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, ticker);
  }
}


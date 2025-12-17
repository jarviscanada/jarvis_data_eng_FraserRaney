package ca.jrvs.apps.trading.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {

  @Id
  int id;

  @Column(name = "trader_id", nullable = false)
  int traderId;
  double amount;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getTraderId() {
    return traderId;
  }

  public void setTraderId(int traderId) {
    this.traderId = traderId;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "Account{" +
        "id=" + id +
        ", traderId=" + traderId +
        ", amount=" + amount +
        '}';
  }
}

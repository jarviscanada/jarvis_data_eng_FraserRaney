package ca.jrvs.apps.stockquote.data.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "c",
    "d",
    "dp",
    "h",
    "l",
    "o",
    "pc",
    "t"
})
public class Quote {

  private String ticker; //id
  @JsonProperty("o")
  private double open;
  @JsonProperty("h")
  private double high;
  @JsonProperty("l")
  private double low;
  @JsonProperty("c")
  private double price;
  @JsonProperty("pc")
  private double previousClose;
  @JsonProperty("d")
  private double change;
  @JsonProperty("dp")
  private double changePercent;
  private Timestamp timestamp; //time when the info was pulled

  @JsonProperty("t")
  public Timestamp getLatestTradingDay() {
    return latestTradingDay;
  }

  @JsonProperty("t")
  public void setLatestTradingDay(Timestamp latestTradingDay) {
    this.latestTradingDay = latestTradingDay;
  }

  @JsonProperty("t")
  private Timestamp latestTradingDay;

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @JsonProperty("o")
  public double getOpen() {
    return open;
  }

  @JsonProperty("o")
  public void setOpen(double open) {
    this.open = open;
  }

  @JsonProperty("h")
  public double getHigh() {
    return high;
  }

  @JsonProperty("h")
  public void setHigh(double high) {
    this.high = high;
  }

  @JsonProperty("l")
  public double getLow() {
    return low;
  }

  @JsonProperty("l")
  public void setLow(double low) {
    this.low = low;
  }

  @JsonProperty("c")
  public double getPrice() {
    return price;
  }

  @JsonProperty("c")
  public void setPrice(double price) {
    this.price = price;
  }

  @JsonProperty("pc")
  public double getPreviousClose() {
    return previousClose;
  }

  @JsonProperty("pc")
  public void setPreviousClose(double previousClose) {
    this.previousClose = previousClose;
  }

  @JsonProperty("d")
  public double getChange() {
    return change;
  }

  @JsonProperty("d")
  public void setChange(double change) {
    this.change = change;
  }

  @JsonProperty("dp")
  public double getChangePercent() {
    return changePercent;
  }

  @JsonProperty("dp")
  public void setChangePercent(double changePercent) {
    this.changePercent = changePercent;
  }


  public Timestamp getTimestamp() {
    return timestamp;
  }


  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  @Override
  public String toString() {
    return "Quote{" +
        "ticker='" + ticker + '\'' +
        ", open=" + open +
        ", high=" + high +
        ", low=" + low +
        ", price=" + price +
        ", previousClose=" + previousClose +
        ", change=" + change +
        ", changePercent=" + changePercent +
        ", timestamp=" + timestamp +
        ", latestTradingDay=" + latestTradingDay +
        '}';
  }
}
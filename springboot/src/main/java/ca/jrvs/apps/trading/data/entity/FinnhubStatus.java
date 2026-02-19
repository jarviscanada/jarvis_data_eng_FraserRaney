package ca.jrvs.apps.trading.data.entity;

public class FinnhubStatus {

  public String exchange;
  public Object holiday;
  public Boolean isOpen;
  public String session;
  public String timezone;
  public Long t;

  public String getExchange() {
    return exchange;
  }

  public void setExchange(String exchange) {
    this.exchange = exchange;
  }

  public Boolean getOpen() {
    return isOpen;
  }

  public void setOpen(Boolean open) {
    isOpen = open;
  }

  public String getSession() {
    return session;
  }

  public void setSession(String session) {
    this.session = session;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

  public Long getT() {
    return t;
  }

  public void setT(Long t) {
    this.t = t;
  }

  @Override
  public String toString() {
    return "FinnhubStatus{" +
        "exchange='" + exchange + '\'' +
        ", isOpen=" + isOpen +
        ", session='" + session + '\'' +
        ", timezone='" + timezone + '\'' +
        ", t=" + t +
        '}';
  }

  public Object getHoliday() {
    return holiday;
  }

  public void setHoliday(Object holiday) {
    this.holiday = holiday;
  }
}

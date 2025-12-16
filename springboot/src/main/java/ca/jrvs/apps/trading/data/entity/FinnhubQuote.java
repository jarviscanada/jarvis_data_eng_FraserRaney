package ca.jrvs.apps.trading.data.entity;

public class FinnhubQuote {
  //{"c":273.645,"d":-0.465,"dp":-0.1696,"h":274.03,"l":271.79,"o":273.28,"pc":274.11,"t":1765901185}

  private double c; // current price
  private double d;
  private double dp;
  private double h;
  private double l;
  private double o;
  private double pc;
  private long t;

  public double getC() {
    return c;
  }

  public void setC(double c) {
    this.c = c;
  }

  public double getD() {
    return d;
  }

  public void setD(double d) {
    this.d = d;
  }

  public double getDp() {
    return dp;
  }

  public void setDp(double dp) {
    this.dp = dp;
  }

  public double getH() {
    return h;
  }

  public void setH(double h) {
    this.h = h;
  }

  public double getL() {
    return l;
  }

  public void setL(double l) {
    this.l = l;
  }

  public double getO() {
    return o;
  }

  public void setO(double o) {
    this.o = o;
  }

  public double getPc() {
    return pc;
  }

  public void setPc(double pc) {
    this.pc = pc;
  }

  public long getT() {
    return t;
  }

  public void setT(long t) {
    this.t = t;
  }

  @Override
  public String toString() {
    return "FinnhubQuote{" +
        "c=" + c +
        ", d=" + d +
        ", dp=" + dp +
        ", h=" + h +
        ", l=" + l +
        ", o=" + o +
        ", pc=" + pc +
        ", t=" + t +
        '}';
  }
}

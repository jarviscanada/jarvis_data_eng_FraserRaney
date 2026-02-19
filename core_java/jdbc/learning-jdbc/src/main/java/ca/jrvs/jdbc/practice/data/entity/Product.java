package ca.jrvs.jdbc.practice.data.entity;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {

  private UUID productId;
  private String name;
  private BigDecimal price;
  private Vendor vendor;

  public UUID getProductId() {
    return productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public Vendor getVendor() {
    return vendor;
  }

  public void setVendor(Vendor vendor) {
    this.vendor = vendor;
  }

  @Override
  public String toString() {
    return "Product{" +
        "productId=" + productId +
        ", name='" + name + '\'' +
        ", price=" + price +
        ", vendor=" + vendor +
        '}';
  }
}

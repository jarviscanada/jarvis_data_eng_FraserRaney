package ca.jrvs.jdbc.practice.data.dao;

import ca.jrvs.jdbc.practice.data.entity.Product;
import ca.jrvs.jdbc.practice.data.entity.Vendor;
import ca.jrvs.jdbc.practice.data.util.DatabaseUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class ProductVendorDao implements Dao<Product, UUID> {

  private static final Logger LOGGER = Logger.getLogger(ProductVendorDao.class.getName());
  private static final String GET_ALL = "select p.product_id, p.name pname, p.price, p.vendor_id, v.name vname, v.contact, v.email, v.phone, v.address from wisdom.products p left join wisdom.vendors v on p.vendor_id = v.vendor_id";
  private static final String GET_ONE = "select p.product_id, p.name pname, p.price, p.vendor_id, v.name vname, v.contact, v.email, v.phone, v.address from wisdom.products p left join wisdom.vendors v on p.vendor_id = v.vendor_id where p.product_id = ?";
  private static final String GET_ONE_VENDOR = "select vendor_id, name, contact, email, phone, address from wisdom.vendors where vendor_id = ?";
  private static final String GET_ALL_VENDORS = "select vendor_id, name, contact, email, phone, address from wisdom.vendors";
  private static final String CREATE = "insert into wisdom.products (product_id, name, price, vendor_id) values (?,?,?,?)";
  private static final String CREATE_VENDOR = "insert into wisdom.vendors (vendor_id, name, contact, email, phone, address) values (?,?,?,?,?,?)";
  private static final String DELETE = "delete from wisdom.products where product_id = ?";
  private static final String UPDATE_VENDOR = "update wisdom.vendors set name = ?, contact = ?, email = ?, phone = ?, address = ? where vendor_id = ?";
  private static final String UPDATE = "update wisdom.products set name = ?, price = ?, vendor_id = ? where product_id = ?";

  @Override
  public List<Product> getAll() {
    List<Product> products = new ArrayList<>();
    Connection connection = DatabaseUtils.getConnection();
    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(GET_ALL);
      products = this.processResultSet(rs);
    } catch (SQLException e) {
      DatabaseUtils.handleSqlException("ProductVendorDao.getAll", e, LOGGER);
    }
    return products;
  }

  @Override
  public Product create(Product entity) {

    UUID productId = UUID.randomUUID();
    Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(CREATE);
      statement.setObject(1, productId);
      statement.setString(2, entity.getName());
      statement.setBigDecimal(3, entity.getPrice());
      UUID vendorId = entity.getVendor().getVendorId();
      if (vendorId == null) {
        vendorId = this.createVendor(entity.getVendor()).getVendorId();
      }
      statement.setObject(4, vendorId);
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("CustomerDao.create.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("CustomerDao.create", e, LOGGER);
    }
    Optional<Product> product = this.getOne(productId);
    if (!product.isPresent()) {
      return null;
    }
    return product.get();

  }

  @Override
  public Optional<Product> getOne(UUID id) {
    try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_ONE)) {
      statement.setObject(1, id);
      ResultSet rs = statement.executeQuery();
      List<Product> products = this.processResultSet(rs);
      if (products.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(products.get(0));
    } catch (SQLException e) {
      DatabaseUtils.handleSqlException("ProductVendorDao.getOne", e, LOGGER);
    }
    return Optional.empty();
  }

  @Override
  public Product update(Product entity) {

    Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(UPDATE);
      statement.setString(1, entity.getName());
      statement.setBigDecimal(2, entity.getPrice());
      statement.setObject(3, this.updateVendor(entity.getVendor()).getVendorId());
      statement.setObject(4, entity.getProductId());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("ProductVendorDao.update.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("ProductVendorDao.update", e, LOGGER);
    }
    return this.getOne(entity.getProductId()).get();
  }

  @Override
  public void delete(UUID id) {
    Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(DELETE);
      statement.setObject(1, id);
      statement.executeUpdate();
      connection.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("ProductVendorDao.delete.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("ProductVendorDao.delete", e, LOGGER);
    }
  }

  public List<Vendor> getAllVendors() {
    List<Vendor> vendors = new ArrayList<>();
    Connection connection = DatabaseUtils.getConnection();
    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(GET_ALL_VENDORS);
      vendors = this.processResultSetVendor(rs);
    } catch (SQLException e) {
      DatabaseUtils.handleSqlException("ProductVendorDao.getAllVendors", e, LOGGER);
    }
    return vendors;
  }


  public Optional<Vendor> getOneVendor(UUID id) {
    try (PreparedStatement statement = DatabaseUtils.getConnection()
        .prepareStatement(GET_ONE_VENDOR)) {
      statement.setObject(1, id);
      ResultSet rs = statement.executeQuery();
      List<Vendor> vendors = this.processResultSetVendor(rs);
      if (vendors.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(vendors.get(0));
    } catch (SQLException e) {
      DatabaseUtils.handleSqlException("CustomerDao.getOne", e, LOGGER);
    }
    return Optional.empty();
  }

  public Vendor createVendor(Vendor entity) {

    UUID vendorId = UUID.randomUUID();
    Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(CREATE_VENDOR);
      statement.setObject(1, vendorId);
      statement.setString(2, entity.getName());
      statement.setString(3, entity.getContact());
      statement.setString(4, entity.getEmail());
      statement.setString(5, entity.getPhone());
      statement.setString(6, entity.getAddress());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("ProductVendorDao.create.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("ProductVendorDao.create", e, LOGGER);
    }

    Optional<Vendor> vendor = this.getOneVendor(vendorId);
    if (!vendor.isPresent()) {
      return null;
    }
    return vendor.get();

  }

  public Vendor updateVendor(Vendor entity) {

    Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(UPDATE_VENDOR);
      statement.setString(1, entity.getName());
      statement.setString(2, entity.getContact());
      statement.setString(3, entity.getEmail());
      statement.setString(4, entity.getPhone());
      statement.setString(5, entity.getAddress());
      statement.setObject(6, entity.getVendorId());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("ProductVendorDao.update.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("ProductVendorDao.update", e, LOGGER);
    }
    return this.getOneVendor(entity.getVendorId()).get();
  }


  List<Product> processResultSet(ResultSet rs) throws SQLException {
    List<Product> products = new ArrayList<>();
    while (rs.next()) {
      Product product = new Product();
      product.setProductId((UUID) rs.getObject("product_id"));
      product.setName(rs.getString("pname"));
      product.setPrice(rs.getBigDecimal("price"));
      Vendor vendor = new Vendor();
      vendor.setVendorId((UUID) rs.getObject("vendor_id"));
      vendor.setName(rs.getString("vname"));
      vendor.setContact(rs.getString("contact"));
      vendor.setEmail(rs.getString("email"));
      vendor.setPhone(rs.getString("phone"));
      vendor.setAddress(rs.getString("address"));
      product.setVendor(vendor);
      products.add(product);
    }
    return products;
  }

  List<Vendor> processResultSetVendor(ResultSet rs) throws SQLException {
    List<Vendor> vendors = new ArrayList<>();
    while (rs.next()) {
      Vendor vendor = new Vendor();
      vendor.setVendorId((UUID) rs.getObject("vendor_id"));
      vendor.setName(rs.getString("name"));
      vendor.setContact(rs.getString("contact"));
      vendor.setEmail(rs.getString("email"));
      vendor.setPhone(rs.getString("phone"));
      vendor.setAddress(rs.getString("address"));
      vendors.add(vendor);
    }
    return vendors;
  }
}

package ca.jrvs.jdbc.practice.data.dao;

import ca.jrvs.jdbc.practice.data.entity.Customer;
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

public class CustomerDao implements Dao<Customer, UUID> {

  private static final Logger LOGGER = Logger.getLogger(CustomerDao.class.getName());
  private static final String GET_ALL = "select customer_id, first_name, last_name, email, phone, address from wisdom.customers";
  private static final String GET_ONE = "select customer_id, first_name, last_name, email, phone, address from wisdom.customers where customer_id = ?";
  private static final String CREATE = "insert into wisdom.customers (customer_id, first_name, last_name, email, phone, address) values (?,?,?,?,?,?)";
  private static final String UPDATE = "update wisdom.customers set first_name = ?, last_name = ?, email = ?, phone = ?, address = ? where customer_id = ?";
  private static final String DELETE = "delete from wisdom.customers where customer_id = ?";

  @Override
  public List<Customer> getAll() {
    List<Customer> customers = new ArrayList<>();
    Connection connection = DatabaseUtils.getConnection();
    try (Statement statement = connection.createStatement()) {
      ResultSet rs = statement.executeQuery(GET_ALL);
      customers = this.processResultSet(rs);
    } catch (SQLException e) {
      DatabaseUtils.handleSqlException("CustomerDao.getAll", e, LOGGER);
    }
    return customers;
  }

  @Override
  public Customer create(Customer entity) {

    UUID customerId = UUID.randomUUID();
    Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(CREATE);
      statement.setObject(1, customerId);
      statement.setString(2, entity.getFirstName());
      statement.setString(3, entity.getLastName());
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
        DatabaseUtils.handleSqlException("CustomerDao.create.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("CustomerDao.create", e, LOGGER);
    }
    Optional<Customer> customer = this.getOne(customerId);
    if (!customer.isPresent()) {
      return null;
    }
    return customer.get();

  }

  @Override
  public Optional<Customer> getOne(UUID id) {
    try (PreparedStatement statement = DatabaseUtils.getConnection().prepareStatement(GET_ONE)) {
      statement.setObject(1, id);
      ResultSet rs = statement.executeQuery();
      List<Customer> customers = this.processResultSet(rs);
      if (customers.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(customers.get(0));
    } catch (SQLException e) {
      DatabaseUtils.handleSqlException("CustomerDao.getOne", e, LOGGER);
    }
    return Optional.empty();
  }

  @Override
  public Customer update(Customer entity) {

    Connection connection = DatabaseUtils.getConnection();
    try {
      connection.setAutoCommit(false);
      PreparedStatement statement = connection.prepareStatement(UPDATE);
      statement.setString(1, entity.getFirstName());
      statement.setString(2, entity.getLastName());
      statement.setString(3, entity.getEmail());
      statement.setString(4, entity.getPhone());
      statement.setString(5, entity.getAddress());
      statement.setObject(6, entity.getCustomerId());
      statement.execute();
      connection.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("CustomerDao.update.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("CusomerDao.update", e, LOGGER);
    }
    return this.getOne(entity.getCustomerId()).get();
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
        DatabaseUtils.handleSqlException("CustomerDao.delete.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("Customer.delete", e, LOGGER);
    }


  }

  List<Customer> processResultSet(ResultSet rs) throws SQLException {
    List<Customer> customers = new ArrayList<>();
    while (rs.next()) {
      Customer customer = new Customer();
      customer.setCustomerId((UUID) rs.getObject("customer_id"));
      customer.setFirstName(rs.getString("first_name"));
      customer.setLastName(rs.getString("last_name"));
      customer.setEmail(rs.getString("email"));
      customer.setPhone(rs.getString("phone"));
      customer.setAddress(rs.getString("address"));
      customers.add(customer);
    }
    return customers;
  }
}

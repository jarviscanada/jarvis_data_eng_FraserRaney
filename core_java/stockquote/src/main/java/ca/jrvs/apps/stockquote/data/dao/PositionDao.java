package ca.jrvs.apps.stockquote.data.dao;

import ca.jrvs.apps.stockquote.data.entity.Position;
import ca.jrvs.apps.stockquote.data.util.DatabaseUtils;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionDao implements CrudDao<Position, String> {

  private Connection c = DatabaseUtils.getConnection();
  private final Logger LOGGER = LoggerFactory.getLogger(PositionDao.class);
  private final String FIND_BY_ID = "select symbol, number_of_shares, value_paid from position where symbol = ?";
  private final String FIND_ALL = "select symbol, number_of_shares, value_paid from position";
  private final String DELETE_BY_ID = "delete from position where symbol = ?";
  private final String DELETE_ALL = "truncate table position";
  private final String UPDATE = "update position set number_of_shares=?, value_paid=? where symbol = ?";
  private final String CREATE = "insert into position (symbol, number_of_shares, value_paid) values (?,?,?)";

  /**
   * Saves a given entity. Used for create and update
   *
   * @param entity - must not be null
   * @return The saved entity. Will never be null
   * @throws IllegalArgumentException - if id is null
   */
  @Override
  public Position save(Position entity) throws IllegalArgumentException {
    if (entity.getTicker() == null) {
      throw new IllegalArgumentException("save: id is null");
    }
    Optional<Position> position = this.findById(entity.getTicker());
    if (position.isPresent()) {
      return this.update(entity);
    } else {
      return this.create(entity);
    }
  }

  public Position create(Position entity) {
    try {
      this.c.setAutoCommit(false);
      PreparedStatement statement = this.c.prepareStatement(CREATE);
      statement.setString(1, entity.getTicker());
      statement.setInt(2, entity.getNumOfShares());
      statement.setBigDecimal(3, new BigDecimal(entity.getValuePaid()));
      statement.execute();
      this.c.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        this.c.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("PositionDao.create.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("PositionDao.create", e, LOGGER);
    }
    Optional<Position> position = this.findById(entity.getTicker());
    if (!position.isPresent()) {
      return null;
    }
    return position.get();
  }

  public Position update(Position entity) {
    try {
      this.c.setAutoCommit(false);
      PreparedStatement statement = this.c.prepareStatement(UPDATE);
      statement.setInt(1, entity.getNumOfShares());
      statement.setBigDecimal(2, new BigDecimal(entity.getValuePaid()));
      statement.setString(3, entity.getTicker());
      statement.execute();
      this.c.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        this.c.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("PositionDao.update.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("PositionDao.update", e, LOGGER);
    }
    return this.findById(entity.getTicker()).get();
  }


  /**
   * Retrieves an entity by its id
   *
   * @param s - must not be null
   * @return Entity with the given id or empty optional if none found
   * @throws IllegalArgumentException - if id is null
   */
  @Override
  public Optional<Position> findById(String s) throws IllegalArgumentException {
    if (s == null) {
      throw new IllegalArgumentException("findById: id is null");
    }
    try (PreparedStatement statement = c.prepareStatement(FIND_BY_ID)) {
      statement.setString(1, s);
      ResultSet rs = statement.executeQuery();
      List<Position> positions = this.processResultSet(rs);
      if (positions.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(positions.get(0));
    } catch (SQLException sqle) {
      DatabaseUtils.handleSqlException("PositionDao.findById", sqle, LOGGER);
    }
    return null;
  }

  /**
   * Retrieves all entities
   *
   * @return All entities
   */
  @Override
  public Iterable<Position> findAll() {
    List<Position> positions = new ArrayList();
    try (Statement statement = this.c.createStatement()) {
      ResultSet rs = statement.executeQuery(FIND_ALL);
      positions = this.processResultSet(rs);
      rs.close();
    } catch (SQLException sqle) {
      DatabaseUtils.handleSqlException("PositionDao.findAll", sqle, LOGGER);
    }
    return positions;
  }

  /**
   * Deletes the entity with the given id. If the entity is not found, it is silently ignored
   *
   * @param s - must not be null
   * @throws IllegalArgumentException - if id is null
   */
  @Override
  public void deleteById(String s) throws IllegalArgumentException {
    if (s == null) {
      throw new IllegalArgumentException("deleteById: id is null");
    }
    try {
      this.c.setAutoCommit(false);
      PreparedStatement statement = this.c.prepareStatement(DELETE_BY_ID);
      statement.setString(1, s);
      statement.execute();
      this.c.commit();
      statement.close();
    } catch (SQLException sqle) {
      try {
        this.c.rollback();
      } catch (SQLException e) {
        DatabaseUtils.handleSqlException("PositionDao.deleteById.rollback", e, LOGGER);
      }
      DatabaseUtils.handleSqlException("PositionDao.deleteById", sqle, LOGGER);
    }
  }

  /**
   * Deletes all entities managed by the repository
   */
  @Override
  public void deleteAll() {
    try {
      this.c.setAutoCommit(false);
      Statement statement = this.c.createStatement();
      statement.execute(DELETE_ALL);
      this.c.commit();
      statement.close();
    } catch (SQLException sqle) {
      try {
        this.c.rollback();
      } catch (SQLException e) {
        DatabaseUtils.handleSqlException("PositionDao.deleteAll.rollback", e, LOGGER);
      }
      DatabaseUtils.handleSqlException("PositionDao.deleteAll", sqle, LOGGER);
    }
  }

  private List<Position> processResultSet(ResultSet rs) throws SQLException {
    List<Position> positions = new ArrayList<>();
    while (rs.next()) {
      Position position = new Position();
      position.setTicker(rs.getString("symbol"));
      position.setNumOfShares(rs.getInt("number_of_shares"));
      position.setValuePaid(rs.getDouble("value_paid"));
      positions.add(position);
    }
    return positions;
  }
}
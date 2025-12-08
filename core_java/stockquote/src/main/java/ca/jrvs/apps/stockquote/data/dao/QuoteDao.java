package ca.jrvs.apps.stockquote.data.dao;

import ca.jrvs.apps.stockquote.data.entity.Quote;
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

public class QuoteDao implements CrudDao<Quote, String> {

  private Connection c;
  private final Logger LOGGER = LoggerFactory.getLogger(QuoteDao.class);
  private final String FIND_BY_ID = "select symbol, open, high, low, price, latest_trading_day, previous_close, change, change_percent, timestamp from quote where symbol = ?";
  private final String FIND_ALL = "select symbol, open, high, low, price, latest_trading_day, previous_close, change, change_percent, timestamp from quote";
  private final String DELETE_BY_ID = "delete from quote where symbol = ?";
  private final String DELETE_ALL = "delete from quote";
  private final String UPDATE = "update quote set open=?, high=?, low=?, price=?, latest_trading_day=?, previous_close=?, change=?, change_percent=?, timestamp=? where symbol = ?";
  private final String CREATE = "insert into quote (symbol, open, high, low, price, latest_trading_day, previous_close, change, change_percent, timestamp) values (?,?,?,?,?,?,?,?,?,?)";

  public QuoteDao(Connection c) {
    this.c = c;
  }

  public QuoteDao() {
    this(DatabaseUtils.getConnection());
  }

  /**
   * Saves a given entity. Used for create and update
   *
   * @param entity - must not be null
   * @return The saved entity. Will never be null
   * @throws IllegalArgumentException - if id is null
   */
  @Override
  public Quote save(Quote entity) throws IllegalArgumentException {
    if (entity.getTicker() == null) {
      throw new IllegalArgumentException("save: id is null");
    }
    Optional<Quote> quote = this.findById(entity.getTicker());
    if (quote.isPresent()) {
      return this.update(entity);
    } else {
      return this.create(entity);
    }
  }

  public Quote create(Quote entity) {
    try {
      this.c.setAutoCommit(false);
      PreparedStatement statement = this.c.prepareStatement(CREATE);
      statement.setString(1, entity.getTicker());
      statement.setBigDecimal(2, new BigDecimal(entity.getOpen()));
      statement.setBigDecimal(3, new BigDecimal(entity.getHigh()));
      statement.setBigDecimal(4, new BigDecimal(entity.getLow()));
      statement.setBigDecimal(5, new BigDecimal(entity.getPrice()));
      statement.setTimestamp(6, entity.getLatestTradingDay());
      statement.setBigDecimal(7, new BigDecimal(entity.getPreviousClose()));
      statement.setBigDecimal(8, new BigDecimal(entity.getChange()));
      statement.setBigDecimal(9, new BigDecimal(entity.getChangePercent()));
      statement.setTimestamp(10, entity.getTimestamp());
      statement.execute();
      this.c.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        this.c.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("QuoteDao.create.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("QuoteDao.create", e, LOGGER);
    }
    Optional<Quote> position = this.findById(entity.getTicker());
    if (!position.isPresent()) {
      return null;
    }
    return position.get();
  }

  public Quote update(Quote entity) {
    try {
      this.c.setAutoCommit(false);
      PreparedStatement statement = this.c.prepareStatement(UPDATE);
      statement.setBigDecimal(1, new BigDecimal(entity.getOpen()));
      statement.setBigDecimal(2, new BigDecimal(entity.getHigh()));
      statement.setBigDecimal(3, new BigDecimal(entity.getLow()));
      statement.setBigDecimal(4, new BigDecimal(entity.getPrice()));
      statement.setTimestamp(5, entity.getLatestTradingDay());
      statement.setBigDecimal(6, new BigDecimal(entity.getPreviousClose()));
      statement.setBigDecimal(7, new BigDecimal(entity.getChange()));
      statement.setBigDecimal(8, new BigDecimal(entity.getChangePercent()));
      statement.setTimestamp(9, entity.getTimestamp());
      statement.setString(10, entity.getTicker());
      statement.execute();
      this.c.commit();
      statement.close();
    } catch (SQLException e) {
      try {
        this.c.rollback();
      } catch (SQLException sqle) {
        DatabaseUtils.handleSqlException("QuoteDao.update.rollback", sqle, LOGGER);
      }
      DatabaseUtils.handleSqlException("QuoteDao.update", e, LOGGER);
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
  public Optional<Quote> findById(String s) throws IllegalArgumentException {
    if (s == null) {
      throw new IllegalArgumentException("findById: id is null");
    }
    try (PreparedStatement statement = c.prepareStatement(FIND_BY_ID)) {
      statement.setString(1, s);
      ResultSet rs = statement.executeQuery();
      List<Quote> quotes = this.processResultSet(rs);
      if (quotes.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(quotes.get(0));
    } catch (SQLException sqle) {
      DatabaseUtils.handleSqlException("QuoteDao.findById", sqle, LOGGER);
    }
    return null;
  }

  /**
   * Retrieves all entities
   *
   * @return All entities
   */
  @Override
  public Iterable<Quote> findAll() {
    List<Quote> quotes = new ArrayList();
    try (Statement statement = this.c.createStatement()) {
      ResultSet rs = statement.executeQuery(FIND_ALL);
      quotes = this.processResultSet(rs);
      rs.close();
    } catch (SQLException sqle) {
      DatabaseUtils.handleSqlException("Quotedao.findAll", sqle, LOGGER);
    }
    return quotes;
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
        DatabaseUtils.handleSqlException("QuoteDao.deleteById.rollback", e, LOGGER);
      }
      DatabaseUtils.handleSqlException("QuoteDao.deleteById", sqle, LOGGER);
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
        DatabaseUtils.handleSqlException("QuoteDao.deleteAll.rollback", e, LOGGER);
      }
      DatabaseUtils.handleSqlException("QuoteDao.deleteAll", sqle, LOGGER);
    }
  }

  //implement all inherited methods
  //you are not limited to methods defined in CrudDao
  private List<Quote> processResultSet(ResultSet rs) throws SQLException {
    List<Quote> quotes = new ArrayList<>();
    while (rs.next()) {
      Quote quote = new Quote();
      quote.setTicker(rs.getString("symbol"));
      quote.setOpen(rs.getDouble("open"));
      quote.setHigh(rs.getDouble("high"));
      quote.setLow(rs.getDouble("low"));
      quote.setPrice(rs.getDouble("price"));
      quote.setLatestTradingDay(rs.getTimestamp("latest_trading_day"));
      quote.setPreviousClose(rs.getDouble("previous_close"));
      quote.setChange(rs.getDouble("change"));
      quote.setChangePercent(rs.getDouble("change_percent"));
      quote.setTimestamp(rs.getTimestamp("timestamp"));
      quotes.add(quote);
    }
    return quotes;
  }
}

package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.data.dao.MarketDataDao;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import ca.jrvs.apps.trading.data.entity.Quote;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
  private final MarketDataDao marketDataDao;

  public QuoteService(MarketDataDao marketDataDao) {
    this.marketDataDao = marketDataDao;
  }

  /**
   * Find an IexQuote from the given ticker
   *
   * @param ticker
   * @return corresponding IexQuote object
   * @throws IllegalArgumentExcpetion if ticker is invalid
   */
  public FinnhubQuote findFinnhubQuoteByTicker(String ticker) {
    logger.info("Fetching quote for ticker: {}", ticker);
    try {
      FinnhubQuote quote = marketDataDao.findQuoteByTicker(ticker);
      logger.debug("Received quote: {}", quote);
      return quote;
    } catch (Exception e) {
      logger.error("Error while fetching quote for ticker: {}", ticker, e);
      throw e;
    }
  }

  /**
   * Update quote table against IEX source
   * <p>
   * - get all quotes from the db - for each ticker get IexQuote - convert IexQuote to Quote entity
   * - persist quote to db
   *
   * @throws ResourceNotFoundException if ticker is not found from IEX
   * @throws DataAccessException       if unable to retrieve data
   * @throws IllegalArgumentException  for invalid input
   */
  public void updateMarketData() {
    //TODO
  }

  /**
   * Validate (against IEX) and save given tickers to quote table
   * <p>
   * - get IexQuote(s) - convert each IexQuote to Quote entity - persist the quote to db
   *
   * @param tickers
   * @return list of converted quote entities
   * @throws IllegalArgumentException if ticker is not found from IEX
   */
  public List<Quote> saveQuotes(List<String> tickers) {
    //TODO
    return Collections.emptyList();
  }

  /**
   * Update a given quote to the quote table without validation
   *
   * @param quote entity to save
   * @return the saved quote entity
   */
  public Quote saveQuote(Quote quote) {
    //TODO
    return null;
  }

  /**
   * Find all quotes from the quote table
   *
   * @return a list of quotes
   */
  public List<Quote> findAllQuotes() {
    //TODO
    return Collections.emptyList();
  }

  /**
   * Helper method to map an IexQuote to a Quote entity Note: 'iexQuote.getLatestPrice() == null' if
   * the stock market is closed Make sure to set a default value for number field(s)
   */
  protected static Quote buildQuoteFromIexQuote(FinnhubQuote iexQuote) {
    //TODO
    return null;
  }

  /**
   * Helper method to validate and save a single ticker Not to be confused with saveQuote(Quote
   * quote)
   */
  protected Quote saveQuote(String ticker) {
    //TODO
    return null;
  }
}

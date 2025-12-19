package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.data.dao.MarketDataDao;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import ca.jrvs.apps.trading.data.entity.FinnhubStatus;
import ca.jrvs.apps.trading.data.entity.Quote;
import ca.jrvs.apps.trading.data.repository.QuoteJpaRepository;
import ca.jrvs.apps.trading.service.util.ResourceNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

  private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);
  private static final String exceptionFormat = "exception in %s, message %s";

  private final MarketDataDao marketDataDao;

  @Autowired
  private QuoteJpaRepository quoteRepo;

  public QuoteService(MarketDataDao marketDataDao) {
    this.marketDataDao = marketDataDao;
  }

  /**
   * Find an FinnhubQuote from the given ticker
   *
   * @param ticker
   * @return corresponding IexQuote object
   * @throws IllegalArgumentException if ticker is invalid
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
   * - get all quotes from the db - for each ticker get FinnhubQuote - convert FinnhubQuote to Quote
   * entity - persist quote to db
   *
   * @throws ResourceNotFoundException if ticker is not found from Finnhub
   * @throws DataAccessException       if unable to retrieve data
   * @throws IllegalArgumentException  for invalid input
   */
  public void updateMarketData() {
    List<String> tickers;
    try {
      tickers = findAllQuotes().stream().map(Quote::getTicker).collect(Collectors.toList());
    } catch (DataAccessException e) {
      handleException("QuoteService.updateMarketData.database", e, logger);
      throw e;
    }
    StreamSupport.stream(tickers.spliterator(), false)
        .forEach(ticker -> {
          if (ticker == null || ticker.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "QuoteService.updateMarketData: ticker is null or empty");
          }
        });
    try {
      saveQuotes(tickers);
    } catch (IllegalArgumentException e) {
      handleException("QuoteService.updateMarketData.fetchAndSave", e, logger);
      throw new ResourceNotFoundException("No data was found for the given symbol (US stocks only)",
          e);
    } catch (Exception e) {
      handleException("QuoteSerive.updateMarketData.other", e, logger);
    }
  }

  /**
   * Validate (against Finnhub) and save given tickers to quote table
   * <p>
   * - get FinnhubQuote(s) - convert each FinnhubQuote to Quote entity - persist the quote to db
   *
   * @param tickers
   * @return list of converted quote entities
   * @throws IllegalArgumentException if ticker is not found from Finnhub
   */
  public List<Quote> saveQuotes(List<String> tickers) {
    return StreamSupport.stream(tickers.spliterator(), false)
        .map(this::saveQuote)
        .collect(Collectors.toList());
  }

  /**
   * Update a given quote to the quote table without validation
   *
   * @param quote entity to save
   * @return the saved quote entity
   */
  public Quote saveQuote(Quote quote) {
    return quoteRepo.save(quote);
  }

  /**
   * Find all quotes from the quote table
   *
   * @return a list of quotes
   */
  public List<Quote> findAllQuotes() {
    List<Quote> quotes = quoteRepo.findAll();

    if (quotes.isEmpty()) {
      return Collections.emptyList();
    }
    return quotes;
  }

  /**
   * Helper method to map an FinnhubQuote to a Quote entity Note: 'FinnhubQuote.getLatestPrice() ==
   * null' if the stock market is closed Make sure to set a default value for number field(s)
   */
  protected static Quote buildQuoteFromFinnhubQuote(FinnhubQuote finnhubQuote, String ticker,
      FinnhubStatus status) {

    Quote quote = new Quote();
    quote.setTicker(ticker);
    quote.setLastPrice(finnhubQuote.getC());
    quote.setHigh(finnhubQuote.getH());
    quote.setLow(finnhubQuote.getL());
    quote.setOpen(finnhubQuote.getO());
    quote.setClose(finnhubQuote.getPc());

    try {
      if (!status.getOpen()) {
        quote.setLastPrice(0.0);
      }
    } catch (RuntimeException e) {
      handleException("QuoteService.buildQuoteFromFinnhubQuote", e, logger);
      throw e;
    }

    return quote;
  }

  /**
   * Helper method to validate and save a single ticker Not to be confused with saveQuote(Quote
   * quote)
   */
  public Quote saveQuote(String ticker) {
    FinnhubQuote finnhubQuote;
    FinnhubStatus finnhubStatus;
    Quote quote;
    Quote saved;
    if (ticker == null || ticker.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "QuoteService.saveQuote: ticker is null or empty");
    }
    try {
      finnhubQuote = marketDataDao.findQuoteByTicker(ticker);
      finnhubStatus = marketDataDao.isUSMarketOpen();
      quote = buildQuoteFromFinnhubQuote(finnhubQuote, ticker, finnhubStatus);
      saved = saveQuote(quote);
      return saved;
    } catch (RuntimeException e) {
      handleException("QuoteService.saveQuote", e, logger);
      throw e;
    }
  }

  public static void handleException(String method, Exception e, Logger log) {
    log.warn(String.format(exceptionFormat, method, e.getMessage()));
  }
}

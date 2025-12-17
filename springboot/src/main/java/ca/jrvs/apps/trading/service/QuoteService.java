package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.data.dao.MarketDataDao;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
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
}

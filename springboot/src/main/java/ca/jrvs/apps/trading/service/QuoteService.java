package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.data.dao.MarketDataDao;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {

  private final MarketDataDao marketDataDao;

  public QuoteService(MarketDataDao marketDataDao) {
    this.marketDataDao = marketDataDao;
  }

  public FinnhubQuote findFinnhubQuoteByTicker(String ticker) {
    return marketDataDao.findQuoteByTicker(ticker);
  }
}

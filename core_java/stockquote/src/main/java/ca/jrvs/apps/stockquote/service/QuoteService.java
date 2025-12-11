package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import ca.jrvs.apps.stockquote.data.util.QuoteHttpHelper;
import java.util.Optional;

public class QuoteService {

  private QuoteDao dao;
  private QuoteHttpHelper httpHelper;

  public QuoteService(QuoteDao dao, QuoteHttpHelper httpHelper) {
    this.dao = dao;
    this.httpHelper = httpHelper;
  }

  public QuoteService() {
    this(new QuoteDao(), new QuoteHttpHelper());
  }

  /**
   * Fetches latest quote data from endpoint
   *
   * @param ticker
   * @return Latest quote information or empty optional if ticker symbol not found
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
    if (ticker == null || ticker.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "QuoteService.fetchQuoteDataFromAPI: ticker is null or empty");
    }

    String symbol = ticker.trim().toUpperCase();

    Quote quote = httpHelper.fetchQuoteInfo(symbol);
    if (quote == null) {
      return Optional.empty();
    }
    dao.save(quote);
    return Optional.of(quote);

  }

}
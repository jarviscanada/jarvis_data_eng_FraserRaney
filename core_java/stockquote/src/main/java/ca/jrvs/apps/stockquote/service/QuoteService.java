package ca.jrvs.apps.stockquote.service;

import ca.jrvs.apps.stockquote.data.dao.QuoteDao;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import ca.jrvs.apps.stockquote.data.util.QuoteHttpHelper;
import java.util.Optional;

public class QuoteService {

  private QuoteDao dao;
  private QuoteHttpHelper httpHelper;

  /**
   * Fetches latest quote data from endpoint
   * @param ticker
   * @return Latest quote information or empty optional if ticker symbol not found
   */
  public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
    //TO DO
    return Optional.empty();
  }

}
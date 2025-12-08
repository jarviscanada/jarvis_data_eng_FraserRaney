package ca.jrvs.apps.stockquote.controller;

import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.entity.Position;
import ca.jrvs.apps.stockquote.data.entity.Quote;
import ca.jrvs.apps.stockquote.service.PositionService;
import ca.jrvs.apps.stockquote.service.QuoteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StockQuoteController {

  private QuoteService quoteService;
  private PositionService positionService;
  private PositionDao positionDao;
  private Logger LOGGER = LoggerFactory.getLogger(StockQuoteController.class);
  private static final String exceptionFormat = "exception in %s, message %s";

  public StockQuoteController(QuoteService quoteService, PositionService positionService) {
    this.quoteService = quoteService;
    this.positionService = positionService;
    this.positionDao = new PositionDao();
  }

  /**
   * Entry point to dispatch user commands.
   * <p>
   * Example usage: quote AAPL buy MSFT 10 250.0 sell MSFT show-position MSFT
   *
   * @param args program arguments
   */
  public void initClient(String[] args) {
    if (args.length == 0) {
      System.out.println("Usage: <command> [arguments]");
      System.out.println("Commands:");
      System.out.println("  quote <TICKER>");
      System.out.println("  buy <TICKER> <#shares>");
      System.out.println("  sell <TICKER>");
      System.out.println("  show <TICKER>");
      return;
    }

    String cmd = args[0].toLowerCase();
    try {
      switch (cmd) {
        case "quote":
          handleQuote(args);
          break;
        case "buy":
          handleBuy(args);
          break;
        case "sell":
          handleSell(args);
          break;
        case "show":
          handleShowPosition(args);
          break;
        default:
          System.out.println("Unknown command: " + cmd);
          break;
      }
    } catch (Exception e) {
      handleException("StockQuoteController.initClient", e, LOGGER);
    }
  }

  private void handleQuote(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: quote <TICKER>");
      return;
    }
    String ticker = args[1];
    Optional<Quote> opt = quoteService.fetchQuoteDataFromAPI(ticker);
    if (!opt.isPresent()) {
      System.out.println("Quote not found for: " + ticker);
    } else {
      Quote q = opt.get();
      System.out.println("Quote for " + ticker + ":");
      System.out.println("  Price: " + q.getPrice());
      System.out.println("  High: " + q.getHigh());
      System.out.println("  Low: " + q.getLow());
      System.out.println("  Latest trading day: " + q.getLatestTradingDay());
    }
  }

  private void handleBuy(String[] args) {
    if (args.length != 3) {
      System.out.println("Usage: buy <TICKER> <#shares>");
      return;
    }
    String ticker = args[1];
    int shares;
    double price;
    try {
      shares = Integer.parseInt(args[2]);
    } catch (NumberFormatException nfe) {
      handleException("StockQuoteController.handleBuy", nfe, LOGGER);
      return;
    }
    Optional<Quote> opt = quoteService.fetchQuoteDataFromAPI(ticker);
    if (!opt.isPresent()) {
      System.out.println("Quote not found for: " + ticker);
    } else {
      Quote q = opt.get();
      Position pos = positionService.buy(ticker, shares, q.getPrice());
      System.out.println("Bought position: " + pos.getTicker()
          + " shares=" + pos.getNumOfShares()
          + " totalCost=" + pos.getValuePaid());
    }
  }


  private void handleSell(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: sell <TICKER>");
      return;
    }
    String ticker = args[1];
    try {
      positionService.sell(ticker);
      System.out.println("Sold all shares of " + ticker);
    } catch (IllegalArgumentException e) {
      handleException("StockQuoteController.handleSell", e, LOGGER);
    }
  }

  private void handleShowPosition(String[] args) {
    if (args.length != 2) {
      System.out.println("Usage: show-position <TICKER>");
      return;
    }
    String ticker = args[1];
    Optional<Position> opt = positionDao.findById(ticker);
    if (!opt.isPresent()) {
      System.out.println("No position found for " + ticker);
    } else {
      Position pos = opt.get();
      System.out.println("Position for " + ticker + ": shares=" + pos.getNumOfShares()
          + " paid=" + pos.getValuePaid());

      Optional<Quote> maybeQuote = quoteService.fetchQuoteDataFromAPI(ticker);
      if (maybeQuote.isPresent()) {
        double currentValue = maybeQuote.get().getPrice() * pos.getNumOfShares();
        System.out.println("Current Market Value: " + currentValue);
        System.out.println("Unrealized P/L: " + (currentValue - pos.getValuePaid()));
      } else {
        System.out.println("Unable to fetch current quote for " + ticker);
      }
    }
  }

  public static void handleException(String method, Exception e, Logger log) {
    log.warn(String.format(exceptionFormat, method, e.getMessage()));
  }
}

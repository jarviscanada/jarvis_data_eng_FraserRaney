package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.data.entity.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/quote")
public class QuoteController {

  private final QuoteService quoteService;

  public QuoteController(QuoteService quoteService) {
    this.quoteService = quoteService;
  }

  @GetMapping("/finnhub/ticker/{ticker}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public Quote getQuote(@PathVariable String ticker) {
    try {
      return quoteService.saveQuote(ticker.toUpperCase());
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  @PutMapping("/finnhubMarketData")
  @ResponseStatus(HttpStatus.OK)
  public void updateMarketData() {
    try {
      quoteService.updateMarketData();
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }
}

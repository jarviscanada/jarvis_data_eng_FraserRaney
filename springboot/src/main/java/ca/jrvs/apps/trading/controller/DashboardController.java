package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.data.entity.PortfolioView;
import ca.jrvs.apps.trading.data.entity.TraderAccountView;
import ca.jrvs.apps.trading.service.DashboardService;
import ca.jrvs.apps.trading.service.TraderAccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

  private final TraderAccountService traderAccountService;
  private DashboardService dashboardService;

  @Autowired
  public DashboardController(DashboardService dashboardService,
      TraderAccountService traderAccountService) {
    this.dashboardService = dashboardService;
    this.traderAccountService = traderAccountService;
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/profile/traderId/{traderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public TraderAccountView getAccount(@PathVariable Integer traderId) {
    try {
      return dashboardService.getTraderAccount(traderId);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }


  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/portfolio/traderId/{traderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public PortfolioView getPortfolio(@PathVariable Integer traderId) {
    try {
      return dashboardService.getPortfolioViewByTraderId(traderId);
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

  @ResponseBody
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(value = "/traders", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<TraderAccountView> getTraders() {
    try {
      return traderAccountService.getAllTraders();
    } catch (Exception e) {
      throw ResponseExceptionUtil.getResponseStatusException(e);
    }
  }

}

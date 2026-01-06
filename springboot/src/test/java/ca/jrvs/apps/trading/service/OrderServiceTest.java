package ca.jrvs.apps.trading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.data.entity.Account;
import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import ca.jrvs.apps.trading.data.entity.MarketOrder;
import ca.jrvs.apps.trading.data.entity.Position;
import ca.jrvs.apps.trading.data.entity.PositionKey;
import ca.jrvs.apps.trading.data.entity.SecurityOrder;
import ca.jrvs.apps.trading.data.repository.AccountJpaRepository;
import ca.jrvs.apps.trading.data.repository.PositionRepository;
import ca.jrvs.apps.trading.data.repository.SecurityOrderJpaRepository;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Captor
  ArgumentCaptor<SecurityOrder> captorSecurityOrder;

  @Mock
  private AccountJpaRepository mockAccountRepo;

  @Mock
  private SecurityOrderJpaRepository securityOrderRepo;

  @Mock
  PositionRepository positionRepo;

  @Mock
  QuoteService quoteService;

  @InjectMocks
  private OrderService orderService;

  @BeforeEach
  void setUp() {
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  void executeMarketOrder_sell_sucess() {
    MarketOrder order = new MarketOrder();
    order.setTicker("AAPL");
    order.setSize(10);
    order.setTraderId(1);
    order.setOption(MarketOrder.Option.SELL);

    Account account = new Account();
    account.setId(100);
    account.setTraderId(1);
    account.setAmount(10_000.0);

    FinnhubQuote quote = new FinnhubQuote();
    quote.setL(90.0);

    PositionKey positionKey = new PositionKey(account.getId(), order.getTicker());

    when(mockAccountRepo.getAccountByTraderId(1)).thenReturn(account);
    when(quoteService.findFinnhubQuoteByTicker("AAPL")).thenReturn(quote);
    when(positionRepo.findById(positionKey)).thenReturn(
        Optional.of(new Position(positionKey, 100L)));
    when(securityOrderRepo.save(any(SecurityOrder.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    SecurityOrder result = orderService.executeMarketOrder(order);

    assertNotNull(result);
    assertEquals("AAPL", result.getTicker());
    assertEquals(10, result.getSize());
    assertEquals("FILLED", result.getStatus());
    assertEquals(90.0, result.getPrice());

    // ---------- Verify ----------
    verify(securityOrderRepo).save(captorSecurityOrder.capture());
    SecurityOrder savedOrder = captorSecurityOrder.getValue();

    assertEquals("FILLED", savedOrder.getStatus());
    assertEquals(90.0, savedOrder.getPrice());

    verify(mockAccountRepo).save(account);
  }

  @Test
  void executeMarketOrder_buy_success() {

    // ---------- Arrange ----------
    MarketOrder order = new MarketOrder();
    order.setTicker("AAPL");
    order.setSize(10);
    order.setTraderId(1);
    order.setOption(MarketOrder.Option.BUY);

    Account account = new Account();
    account.setId(100);
    account.setTraderId(1);
    account.setAmount(10_000.0);

    FinnhubQuote quote = new FinnhubQuote();
    quote.setH(100.0);

    when(mockAccountRepo.getAccountByTraderId(1)).thenReturn(account);
    when(quoteService.findFinnhubQuoteByTicker("AAPL")).thenReturn(quote);
    when(securityOrderRepo.save(any(SecurityOrder.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    SecurityOrder result = orderService.executeMarketOrder(order);

    assertNotNull(result);
    assertEquals("AAPL", result.getTicker());
    assertEquals(10, result.getSize());
    assertEquals("FILLED", result.getStatus());
    assertEquals(100.0, result.getPrice());

    // ---------- Verify ----------
    verify(securityOrderRepo).save(captorSecurityOrder.capture());
    SecurityOrder savedOrder = captorSecurityOrder.getValue();

    assertEquals("FILLED", savedOrder.getStatus());
    assertEquals(100.0, savedOrder.getPrice());

    verify(mockAccountRepo).save(account);
  }

}
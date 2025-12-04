package ca.jrvs.apps.stockquote.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.stockquote.data.dao.PositionDao;
import ca.jrvs.apps.stockquote.data.entity.Position;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PositionServiceTest {

  private PositionDao mockDao;
  private PositionService positionService;

  @BeforeEach
  void setUp() {
    mockDao = mock(PositionDao.class);
    positionService = new PositionService(mockDao);
  }

  @Test
  void buy_exists() {
    Position p1 = new Position();
    String symbol = "SYM1";
    p1.setTicker(symbol);
    p1.setNumOfShares(100);
    p1.setValuePaid(1000.0);

    Position p2 = new Position();
    p2.setTicker(symbol);
    p2.setNumOfShares(200);
    p2.setValuePaid(1100.0);

    when(mockDao.findById(symbol)).thenReturn(Optional.of(p1));
    when(mockDao.save(any(Position.class))).thenReturn(p2);

    Position result = positionService.buy(symbol, 100, 1);

    assertEquals("SYM1", result.getTicker());
    assertEquals(200, result.getNumOfShares());
    assertEquals(1100.0, result.getValuePaid());

  }

  @Test
  void buy_new() {
    Position p1 = new Position();
    String symbol = "SYM1";
    p1.setTicker(symbol);
    p1.setNumOfShares(100);
    p1.setValuePaid(100.0);

    when(mockDao.findById(symbol)).thenReturn(Optional.empty());
    when(mockDao.save(any(Position.class))).thenReturn(p1);

    Position result = positionService.buy(symbol, 100, 1);

    assertEquals("SYM1", result.getTicker());
    assertEquals(100, result.getNumOfShares());
    assertEquals(100.0, result.getValuePaid());

    assertThrows(IllegalArgumentException.class, () -> positionService.buy(null, 100, 1));
    assertThrows(IllegalArgumentException.class, () -> positionService.buy("MSFT", 0, 1));
    assertThrows(IllegalArgumentException.class, () -> positionService.buy("MSFT", 100, 0));

  }

  @Test
  void sell() {
    assertThrows(IllegalArgumentException.class, () -> positionService.sell(null));
    assertThrows(IllegalArgumentException.class, () -> positionService.sell("INVALIDSYMBOL"));
  }
}
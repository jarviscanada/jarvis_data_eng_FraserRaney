package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MarketDataDaoTest {

  @Mock
  private HttpClient mockHttpClient;

  @Mock
  private HttpResponse mockResponse;

  @Mock
  private HttpEntity mockEntity;

  private MarketDataDao marketDataDao;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    // Inject a version of the DAO that uses the mock HttpClient
    marketDataDao = new MarketDataDao() {
      @Override
      protected HttpClient getHttpClient() {
        // Use mock instead of the real client
        return mockHttpClient;
      }
    };
  }

  @Test
  void findQuoteByTicker() throws Exception {
    String json = "{\"c\":100.0,\"t\":12345}";

    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent()).thenReturn(
        new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

    FinnhubQuote quote = marketDataDao.findQuoteByTicker("AAPL");

    assertNotNull(quote);
    assertEquals(100.0, quote.getC());
    assertEquals(12345, quote.getT());
  }
}
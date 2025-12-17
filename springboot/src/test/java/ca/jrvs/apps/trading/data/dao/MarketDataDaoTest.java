package ca.jrvs.apps.trading.data.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.data.entity.FinnhubQuote;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataRetrievalFailureException;

class MarketDataDaoTest {

  @Mock
  private HttpClient mockHttpClient;

  @Mock
  private HttpResponse mockResponse;

  @Mock
  private StatusLine mockStatusLine;

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
  void findQuoteByTicker_success() throws Exception {
    String json = "{\"c\":100.0,\"t\":12345}";

    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
    when(mockStatusLine.getStatusCode()).thenReturn(200);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent()).thenReturn(
        new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

    FinnhubQuote quote = marketDataDao.findQuoteByTicker("AAPL");

    assertNotNull(quote);
    assertEquals(100.0, quote.getC());
    assertEquals(12345, quote.getT());
  }

  @Test
  void findQuoteByTicker_404() throws Exception {
    String json = "{\"c\":100.0,\"t\":12345}";

    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
    when(mockStatusLine.getStatusCode()).thenReturn(404);

    assertThrows(DataRetrievalFailureException.class, () -> {
      marketDataDao.findQuoteByTicker("AAPL");
    });
  }

  @Test
  void findQuoteByTicker_empty() throws Exception {
    String empty = "{\"c\":0,\"d\":null,\"dp\":null,\"h\":0,\"l\":0,\"o\":0,\"pc\":0,\"t\":0}";

    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
    when(mockStatusLine.getStatusCode()).thenReturn(200);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent()).thenReturn(
        new ByteArrayInputStream(empty.getBytes(StandardCharsets.UTF_8)));

    assertThrows(IllegalArgumentException.class, () -> {
      marketDataDao.findQuoteByTicker("AAPL");
    });
  }

  @Test
  void findQuoteByTicker_500() throws Exception {
    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
    when(mockStatusLine.getStatusCode()).thenReturn(500);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent()).thenReturn(null);

    assertThrows(DataRetrievalFailureException.class, () -> {
      marketDataDao.findQuoteByTicker("AAPL");
    });
  }

  @Test
  void findQuoteByTicker_throwIo() throws Exception {

    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
    when(mockStatusLine.getStatusCode()).thenReturn(500);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent()).thenThrow(new IOException("entityUtils error"));

    assertThrows(DataRetrievalFailureException.class, () -> {
      marketDataDao.findQuoteByTicker("AAPL");
    });
  }

  @Test
  void findQuoteByTicker_json() throws Exception {

    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
    when(mockStatusLine.getStatusCode()).thenReturn(500);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent()).thenReturn(
        new ByteArrayInputStream("INVALID".getBytes(StandardCharsets.UTF_8)));

    assertThrows(DataRetrievalFailureException.class, () -> {
      marketDataDao.findQuoteByTicker("AAPL");
    });
  }
}
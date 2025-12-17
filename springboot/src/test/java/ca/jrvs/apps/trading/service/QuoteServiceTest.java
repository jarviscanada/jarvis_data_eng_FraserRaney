package ca.jrvs.apps.trading.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.data.dao.MarketDataDao;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

class QuoteServiceTest {

  @Mock
  private HttpClient mockHttpClient;

  @Mock
  private HttpResponse mockResponse;

  @Mock
  private HttpEntity mockEntity;

  private MarketDataDao marketDataDao;
  private QuoteService quoteService;

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
    quoteService = new QuoteService(marketDataDao);

  }

  @Test
  void findFinnhubQuoteByTicker_throws() throws Exception {
    String ticker = "AAPL";
    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent())
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    assertThrows(HttpClientErrorException.class,
        () -> quoteService.findFinnhubQuoteByTicker(ticker));


  }

  @Test
  void findFinnhubQuoteByTicker_success() throws Exception {
    String ticker = "AAPL";

    String json = "{\"c\":123.45,\"t\":1000000}";

    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent()).thenReturn(
        new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

    FinnhubQuote quote = quoteService.findFinnhubQuoteByTicker(ticker);

    assertNotNull(quote);
    assertEquals(123.45, quote.getC());
    assertEquals(1000000L, quote.getT());
  }


}
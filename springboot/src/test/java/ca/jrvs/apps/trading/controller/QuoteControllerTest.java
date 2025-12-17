package ca.jrvs.apps.trading.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.trading.data.dao.MarketDataDao;
import ca.jrvs.apps.trading.service.QuoteService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

class QuoteControllerTest {

  @Mock
  private HttpClient mockHttpClient;

  @Mock
  private HttpResponse mockResponse;

  @Mock
  private StatusLine mockStatusLine;


  @Mock
  private HttpEntity mockEntity;

  private MarketDataDao marketDataDao;
  private QuoteService quoteService;
  private QuoteController quoteController;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);

    marketDataDao = new MarketDataDao() {
      @Override
      protected HttpClient getHttpClient() {
        // Use mock instead of the real client
        return mockHttpClient;
      }
    };
    quoteService = new QuoteService(marketDataDao);
    quoteController = new QuoteController(quoteService);

  }

  @Test
  void getQuote_throwBad() throws Exception {
    String ticker = "AAPL";

    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
    when(mockStatusLine.getStatusCode()).thenReturn(200);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent())
        .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

    assertThrows(ResponseStatusException.class,
        () -> quoteController.getQuote(ticker));

  }

  @Test
  void getQuote_throwArg() throws Exception {
    String ticker = "AAPL";

    when(mockHttpClient.execute(any(HttpGet.class))).thenReturn(mockResponse);
    when(mockResponse.getStatusLine()).thenReturn(mockStatusLine);
    when(mockStatusLine.getStatusCode()).thenReturn(200);
    when(mockResponse.getEntity()).thenReturn(mockEntity);
    when(mockEntity.getContent())
        .thenThrow(new IllegalArgumentException());

    assertThrows(ResponseStatusException.class,
        () -> quoteController.getQuote(ticker));

  }
}
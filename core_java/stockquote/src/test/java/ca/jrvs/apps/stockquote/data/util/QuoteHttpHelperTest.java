package ca.jrvs.apps.stockquote.data.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.stockquote.data.entity.Quote;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class QuoteHttpHelperTest {

  private OkHttpClient mockClient;
  private QuoteHttpHelper helper;
  private final Logger LOGGER = LoggerFactory.getLogger(QuoteHttpHelperTest.class);

  @BeforeEach
  void setUp() {
    mockClient = mock(OkHttpClient.class);
    helper = new QuoteHttpHelper("mockApiKey", mockClient);
  }

  @Test
  void fetchQuoteInfo_returnsQuote() {
    String symbol = "AAPL";
    // Example of a valid JSON body (simplified ? must match your Quote class structure)
    String json = "{"
        + "\"c\": 150.0,"
        + "\"d\": 2.0,"
        + "\"dp\": 1.1,"
        + "\"h\": 151.0,"
        + "\"l\": 149.0,"
        + "\"o\": 150.5,"
        + "\"pc\": 149.5,"
        + "\"t\": 1700000000"
        + "}";

    Call mockCall = mock(Call.class);
    ResponseBody body = ResponseBody.create(json, okhttp3.MediaType.get("application/json"));
    Response mockResponse = new Response.Builder()
        .request(new Request.Builder().url("http://localhost/").build())
        .protocol(okhttp3.Protocol.HTTP_1_1)
        .code(200)
        .message("OK")
        .body(body)
        .build();

    when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
    try {
      when(mockCall.execute()).thenReturn(mockResponse);
    } catch (IOException e) {
      LOGGER.warn("IOException in fetchQuoteInfo_returnsQuote: ", e);
    }
    Quote quote = helper.fetchQuoteInfo(symbol);

    assertNotNull(quote);
    assertEquals(symbol, quote.getTicker());
    assertEquals(150.0, quote.getPrice());
    assertEquals(151.0, quote.getHigh());
    assertEquals(149.0, quote.getLow());
  }

  @Test
  void fetchQuoteInfo_emptyResponse() throws Exception {
    String emptyJson = "{\"c\":0,\"d\":null,\"dp\":null,\"h\":0,\"l\":0,\"o\":0,\"pc\":0,\"t\":0}";
    ;  // or copy the string

    Call mockCall = mock(Call.class);
    ResponseBody body = ResponseBody.create(emptyJson, okhttp3.MediaType.get("application/json"));
    Response mockResponse = new Response.Builder()
        .request(new Request.Builder().url("http://localhost/").build())
        .protocol(okhttp3.Protocol.HTTP_1_1)
        .code(200)
        .message("OK")
        .body(body)
        .build();

    when(mockClient.newCall(any(Request.class))).thenReturn(mockCall);
    when(mockCall.execute()).thenReturn(mockResponse);

    assertThrows(IllegalArgumentException.class, () -> helper.fetchQuoteInfo("INVALID"));
  }
}
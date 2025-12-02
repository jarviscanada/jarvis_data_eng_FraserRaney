package ca.jrvs.apps.practice;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkExample {
  OkHttpClient client = new OkHttpClient();

  String run(String url) throws IOException {
    String apiKey = "d4n0g8pr01qsn6g8qu0gd4n0g8pr01qsn6g8qu10";

    Request request = new Request.Builder()
        .url(url)
        .addHeader("X-Finnhub-Token", apiKey)
        .build();

    try (Response response = client.newCall(request).execute()) {
      return response.body().string();
    }
  }

  public static void main(String[] args) throws IOException {
    String symbol = "AAPL";
    String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol;

    Quote quote = JsonParser.toObjectFromJson(new OkExample().run(url), Quote.class);

    System.out.println(quote);
  }
}

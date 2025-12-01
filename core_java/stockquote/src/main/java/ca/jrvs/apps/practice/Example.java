package ca.jrvs.apps.practice;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class Example {

  public static void main(String[] args) {
    System.out.println("Hello, World");

    String apiKey = "d4n0g8pr01qsn6g8qu0gd4n0g8pr01qsn6g8qu10";
    String secret  = "d4n0g8pr01qsn6g8qu0gd4n0g8pr01qsn6g8qu10";  // if needed for webhooks
    String symbol = "AAPL";
    String url = "https://finnhub.io/api/v1/quote?symbol=" + symbol;

    // Create HttpClient
    try (CloseableHttpClient client = HttpClients.createDefault()) {
      // Prepare GET request
      HttpGet request = new HttpGet(url);

      // If the API requires a custom header for the token:
      request.setHeader("X-Finnhub-Token", apiKey);

      // If you also need to send a secret header (for webhook acknowledgement or similar):
      //request.setHeader("X-Finnhub-Secret", secret);

      // Execute request
      try (CloseableHttpResponse response = client.execute(request)) {
        int status = response.getStatusLine().getStatusCode();
        String body = EntityUtils.toString(response.getEntity(), "UTF-8");

        System.out.println("HTTP Status: " + status);
        System.out.println("Response Body:");
        System.out.println(body);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(body);
        System.out.println(root.toPrettyString());

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

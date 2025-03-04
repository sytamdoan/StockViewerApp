import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Stock {
    private static final String APIURL = "https://www.alphavantage.co/query";
    private static final String APIkey = "90MSA6KYH9GRJW3T";
    private static String stock = "";
    private static String timeInterval = "";
    private static JsonNode stockPricing;

    public Stock(String stock, String timeInterval) {
        setStock(stock);
        setTimeInterval(timeInterval);
        stockPricing = getStockInfo();
    }

    public void setStock(String newStock) {
        stock = newStock;
    }

    public void setTimeInterval(String newTimeInterval) {
        timeInterval = newTimeInterval;
    }

    public String getStockSymbol() {
        return stock;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public JsonNode getStockPricing() {
        stockPricing = getStockInfo();
        return stockPricing;
    }

    public static JsonNode getStockInfo() {
        String urlString = "https://my-json-server.typicode.com/sytamdoan/stockinfo/db";
        //String urlString = APIURL + "?function=TIME_SERIES_INTRADAY&symbol=" + stock + "&interval=" + timeInterval + "&apikey=" + APIkey;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readTree(response.toString());

            } else {
                System.out.println("Error: " + responseCode);
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

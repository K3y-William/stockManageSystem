import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class test1 {

    public static void main(String[] args) {
        // 示例：调用 API 并提取数据
        System.out.println(callApiAndExtractData("AAPL", "2023-01-09", "2023-01-09"));
        System.out.println(callApiAndExtractData("GOOGL", "2023-01-10", "2023-01-10"));
        // 可以根据需要调用多次，传递不同的参数
    }

    private static double callApiAndExtractData(String symbol, String startDate, String endDate) {
        try {
            // 构建 API 请求的 URL
            String apiUrl = "https://api.polygon.io/v2/aggs/ticker/" + symbol + "/range/1/day/" + startDate + "/" + endDate + "?apiKey=tZvwESa_l38eC20jTXWOwpsIiNoHCH6a";
            URL url = new URL(apiUrl);

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法
            connection.setRequestMethod("GET");

            // 获取响应码
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取 API 响应
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 处理 API 响应数据
                return extractDataFromJson(response.toString(), symbol);

            } else {
                System.out.println("API request failed. Response Code: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private static double extractDataFromJson(String jsonString, String symbol) {
        // 使用 Gson 解析 JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        // 获取 "results" 对应的 JsonArray
        JsonArray resultsArray = jsonObject.getAsJsonArray("results");

        // 获取第一个元素
        JsonObject firstResult = resultsArray.get(0).getAsJsonObject();

        // 获取 "c" 对应的数字
        double cValue = firstResult.getAsJsonPrimitive("c").getAsDouble();

        return cValue;

        // 输出结果
//        System.out.println("Symbol: " + symbol + ", The value of 'c': " + cValue);
    }
}
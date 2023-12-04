package company;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.impl.priceControlDaoImp;
import dao.priceControlDao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PriceControlSystem implements Runnable{
    static String[] stockTag= {"A","AA","AAA"};
    static int[] stock_id = new int[]{1,2,3};

    static  LocalDate startDate = LocalDate.of(2023, 1, 9);
    static int changingDate = 1;

    // Define a DateTimeFormatter for formatting the date
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final priceControlDao priceControlDao;


    public PriceControlSystem(dao.priceControlDao priceControlDao) {
        this.priceControlDao = priceControlDao;
    }

    @Override
    public void run() {
        System.out.println("Price Control System Start....");
        while (true) {
            LocalDate curDate = startDate.plusDays(changingDate++);
            for (int i = 0; i < 3; i++){
                try {
                    double price = callApiAndExtractData(stockTag[i],formatter.format(curDate),formatter.format(curDate));
                    priceControlDao.updatePrice(stock_id[i],price);

                } catch (NullPointerException e) {
                    // Handle the NullPointerException
                    System.err.println("NullPointerException occurred. Continuing to the next iteration.");
                }
                System.out.println(stockTag[i]+" update success");

            }
            System.out.println("current date:" + formatter.format(curDate));

            try {
                // sleep 5s
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    //接入股票价格的api

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

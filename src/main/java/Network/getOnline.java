package Network;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class getOnline {
    public static void main(String[] args) {
        // API的URL
        String url = "http://localhost:8080/api/con2/";

        // 表单数据
        Map<String, String> form_data = new HashMap<>();
        form_data.put("key1", "lazybones+r");
        form_data.put("key2", "key2");
        form_data.put("value1", "value1");
        form_data.put("value2", "value2");

        try {
            // 发送POST请求并获取JSON响应
            HttpClient client = HttpClient.newHttpClient();
            String requestBody = toQueryString(form_data);
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(postRequest, BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // 解析JSON数据
                System.out.println("JSON Response: " + response.body());
            } else {
                System.out.println("请求失败，状态码：" + response.statusCode());
                System.out.println("Response Body: " + response.body());
            }

            // 发送GET请求并获取文本数据
            HttpRequest getRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> txtResponse = client.send(getRequest, BodyHandlers.ofString());
            System.out.println("Text Response: " + txtResponse.body());
            System.out.println("Final URL: " + txtResponse.uri());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 将Map转换为URL编码的查询字符串
    private static String toQueryString(Map<String, String> params) {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (!query.isEmpty()) {
                query.append("&");
            }
            query.append(param.getKey()).append("=").append(param.getValue());
        }
        return query.toString();
    }
}

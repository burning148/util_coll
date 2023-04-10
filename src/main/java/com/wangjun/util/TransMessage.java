package com.wangjun.util;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.SneakyThrows;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author wangjun294
 * @description 用百度翻译api将国际化中文文件转换成各类语言
 */
public class TransMessage {
    private static final String API_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";
    private final String appId;
    private final String secretKey;

    public TransMessage(String appId, String secretKey) {
        this.appId = appId;
        this.secretKey = secretKey;
    }

    public String translate(String query) {
        try {
            // 对查询字符串进行URL编码
            String encodedQuery = URLEncoder.encode(query, "UTF-8");

            // 拼接API请求URL
            String apiUrl = API_URL + "?q=" + encodedQuery + "&from=zh&to=jp" // zh 中文，jp日文，en英文，详见百度翻译文档
                    + "&appid=" + appId + "&salt=" + System.currentTimeMillis()
                    + "&sign=" + getSign(query);

            // 发送HTTP GET请求
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // 解析服务器返回的JSON格式数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // 解析JSON格式数据，获取翻译结果
            JSONObject json = new JSONObject(response.toString());
            JSONArray transResult = json.getJSONArray("trans_result");
            JSONObject result = transResult.getJSONObject(0);
            return result.getStr("dst");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getSign(String query) {
        // 计算签名，详见百度翻译API文档
        String salt = String.valueOf(System.currentTimeMillis());
        String sign = appId + query + salt + secretKey;
        return MD5(sign);
    }

    private String MD5(String input) {
        return DigestUtil.md5Hex(input);
    }

    @SneakyThrows
    public void replace(String inputFileName, String outputFileName) {
        BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName));
        String line;
        int lineNo = 0; // 记录执行位置
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("=");
            if (parts.length > 1) {
                String key = parts[0];
                String value = parts[1];
                String translation = translate(value);
                Thread.sleep(1000); // 百度翻译api调用频率限制
                writer.write(key + "=" + translation);
                writer.newLine();
            } else {
                writer.write(line);
                writer.newLine();
            }
            System.out.println(lineNo++);
        }
        reader.close();
        writer.close();
    }

    public static void main(String[] args) {
        String appId = "20191025000344353";
        String secretKey = "FYakeSBkJE5j0DZxQ6VK";
        TransMessage replacer = new TransMessage(appId, secretKey);
        replacer.replace("D:\\MyData\\wangjun294\\Desktop\\output_zh.properties",
                "D:\\MyData\\wangjun294\\Desktop\\output_jp.properties");
    }
}

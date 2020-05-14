package cn.xyz.test.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class Test12 {
	public static String postRequest(String url, JSONObject arguments) {
        StringBuffer data = new StringBuffer();
        StringBuffer response = new StringBuffer("");


        try {
            for (Map.Entry<String, Object> entry : arguments.entrySet()) {
                data.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                data.append("=");
                data.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                data.append("&");

            }

            if (data.toString().endsWith("&")) data.deleteCharAt(data.length() - 1);
            URL desurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) desurl.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            connection.setRequestProperty("Accept", "application/json");
            connection.connect();
            // POST请求


            OutputStream os = connection.getOutputStream();

            os.write(data.toString().getBytes("utf-8"));
            os.flush();
            os.close();
            // 读取响应
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            String lines;
//
//            while ((lines = reader.readLine()) != null) {
//                lines = URLDecoder.decode(lines, "utf-8");
//                sb.append(lines);
//            }
//            reader.close();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));//转成utf-8格式
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }


            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response.toString();
    }
}

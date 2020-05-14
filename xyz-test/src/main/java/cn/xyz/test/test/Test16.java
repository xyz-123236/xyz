package cn.xyz.test.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Map;

public class Test16 {
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


    public static String get(String urlStr) {
//        String username = URLEncoder.encode("李四", "UTF-8");

        //对应GET请求，要把请求信息拼接在url后面
//        URL url = new URL("XXXX?username="+username+"&password=123");

        try {
            URL url = new URL(urlStr);

            //调用url的openConnection()方法,获得连接对象
            HttpURLConnection conn = null;

            conn = (HttpURLConnection) url.openConnection();
            //设置HttpURLConnection的属性
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            conn.setInstanceFollowRedirects(true);

            //只是建立一个连接, 并不会发送真正http请求  (可以不调用)
            conn.connect();

            //通过响应码来判断是否连接成功
            if (conn.getResponseCode() == 200) {
                //获得服务器返回的字节流
                InputStream is = conn.getInputStream();

                //内存输出流,适合数据量比较小的字符串 和 图片
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = is.read(buf)) != -1) {
                    baos.write(buf, 0, len);
                }
                //可使用 toByteArray() 和 toString() 获取数据。
                byte[] result = baos.toByteArray();
                System.out.println(new String(result));
                is.close();
                System.out.println("客户端执行完毕!!");
                return new String(result);
            } else {
                String newUrl = conn.getHeaderField("Location");

                // get the cookie if need, for login
                String cookies = conn.getHeaderField("Set-Cookie");

                // open the new connnection again
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                conn.setRequestProperty("Cookie", cookies);
                conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                conn.addRequestProperty("User-Agent", "Mozilla");
                conn.addRequestProperty("Referer", "google.com");


                System.out.println("Redirect to URL : " + newUrl);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return "";


    }
}

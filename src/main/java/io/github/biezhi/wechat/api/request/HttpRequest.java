package io.github.biezhi.wechat.api.request;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private String ip = null;
    private String userAgent = null;
    private String cookie = "";
    private String host = null;
    public HttpRequest(String ip, String userAgent, String cookie){
        this.ip = ip;
        this.userAgent = userAgent;
        this.cookie = cookie;
    }
    public String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            if(param != null && !param.equals("")) {
                urlNameString = urlNameString + "?" + param;
            }
            URL realUrl = new URL(urlNameString);

            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            //connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if(ip != null){
                connection.setRequestProperty("x-forwarded-for", ip);
            }
            if(userAgent != null){
                connection.setRequestProperty("user-agent", userAgent);
            }
           // connection.setRequestProperty("X-Requested-With", "com.android.browser");
            if(cookie != null){
                connection.setRequestProperty("Cookie", cookie);
            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            Map headers = connection.getHeaderFields();
            List<String> cookies = (List<String>) headers.get("Set-Cookie");
            if(cookies != null){
                //this.cookie = "";
                for(String c : cookies){
                    this.cookie = (this.cookie == null ? "" : this.cookie )+ c + ";";
                }
            }
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                common.logger.debug(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "";
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                result = "";
                e2.printStackTrace();
            }
        }
        return result;
    }
}


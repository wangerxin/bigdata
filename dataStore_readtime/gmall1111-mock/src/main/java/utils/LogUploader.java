package utils;


import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 发送数据到服务器
 */
public class LogUploader {

    /**
     * 发送日志到服务器（tomcat 或者 nginx）
     * @param log
     */
    public static void sendLogStream(String log){
        try{


            //连接服务器
            URL url  =new URL("http://logserver:8080/log");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //--连接配置
            //设置请求方式为post
            conn.setRequestMethod("POST");
            //时间头用来供server进行时钟校对的
            conn.setRequestProperty("clientTime",System.currentTimeMillis() + "");
            //允许上传数据
            conn.setDoOutput(true);
            //设置请求的头信息,设置内容类型为JSON
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            //打印日志到控制台
            System.out.println(log);

            //写数据到服务器
            OutputStream out = conn.getOutputStream();
            out.write(("log="+log).getBytes());
            out.flush();
            out.close();
            int code = conn.getResponseCode();
            System.out.println(code); //返回状态码
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}



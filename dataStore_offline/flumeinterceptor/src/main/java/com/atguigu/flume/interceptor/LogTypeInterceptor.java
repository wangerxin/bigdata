package com.atguigu.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 把日志类型封装在head中
 * 数据模型： event：一行记录被封装为一个event对象，包含header和body
 *            header：默认为null
 *            body：字节数组，可以解析为string
 */
public class LogTypeInterceptor implements Interceptor {

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {

        //1. 取出字符串
        byte[] body = event.getBody();
        String bodyStr = new String(body, Charset.forName("UTF-8"));

        String logType = "";
        //2. 判断
        if (bodyStr.contains("start")){
            logType = "start";
        }else {
            logType = "event";
        }

        //3.封装head
        Map<String, String> headers = event.getHeaders();
        headers.put("logType",logType);

        //4. 返回
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> list) {

        //创建集合
        ArrayList<Event> events = new ArrayList<>();

        //调用intercept()
        for (Event event : list) {
            Event interceptEvent = intercept(event);
            events.add(interceptEvent);
        }

        //返回集合
        return events;
    }

    @Override
    public void close() {

    }

    static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new LogTypeInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}

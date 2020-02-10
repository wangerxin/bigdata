package com.atguigu.flume.interceptor;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *  作用: 日志清洗
 */
public class LogETLInterceptor implements Interceptor {


    @Override
    public void initialize() {

    }

    /**
     *
     * @param event
     * @return
     */
    @Override
    public Event intercept(Event event) {

        //获取数据
        byte[] body = event.getBody();
        String bodyStr = new String(body, Charset.forName("UTF-8"));

        //判断数据
        if (LogUtils.validate(bodyStr)){
            return event;
        }
        //返回
        return null;
    }

    @Override
    public List<Event> intercept(List<Event> list) {

        //创建集合
        ArrayList<Event> events = new ArrayList<>();

        //调用intercept()
        for (Event event : list) {
            Event interceptEvent = intercept(event);
            if (interceptEvent != null){
                events.add(interceptEvent);
            }
        }
        //返回
        return events;
    }

    @Override
    public void close() {

    }

    static class Builder implements Interceptor.Builder{

        @Override
        public Interceptor build() {
            return new LogETLInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}

package com.atguigu.flume.interceptor;

import org.apache.flume.Event;
import org.apache.flume.event.SimpleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class BootStrap {

    public static Logger logger = LoggerFactory.getLogger(BootStrap.class);

    public static void main(String[] args) {


        //1.准备数据
        //字符串数据
        String bodyStr = "1549696569054| {\"cm\":{\"ln\":\"-89.2\",\"sv\":\"V2.0.4\",\"os\":\"8.2.0\",\"g\":\"M67B4QYU@gmail.com\",\"nw\":\"4G\",\"l\":\"en\",\"vc\":\"18\",\"hw\":\"1080*1920\",\"ar\":\"MX\",\"uid\":\"u8678\",\"t\":\"1549679122062\",\"la\":\"-27.4\",\"md\":\"sumsung-12\",\"vn\":\"1.1.3\",\"ba\":\"Sumsung\",\"sr\":\"Y\"},\"ap\":\"weather\",\"et\":[]}";
        //字节数组数据
        byte[] bodyByte = "1549696569054 | {\"cm\":{\"ln\":\"-89.2\",\"sv\":\"V2.0.4\",\"os\":\"8.2.0\",\"g\":\"M67B4QYU@gmail.com\",\"nw\":\"4G\",\"l\":\"en\",\"vc\":\"18\",\"hw\":\"1080*1920\",\"ar\":\"MX\",\"uid\":\"u8678\",\"t\":\"1549679122062\",\"la\":\"-27.4\",\"md\":\"sumsung-12\",\"vn\":\"1.1.3\",\"ba\":\"Sumsung\",\"sr\":\"Y\"},\"ap\":\"weather\",\"et\":[]}".getBytes();
        //创建event
        SimpleEvent simpleEvent = new SimpleEvent();
        simpleEvent.setBody(bodyByte);

        //测试etl
        LogETLInterceptor logETLInterceptor = new LogETLInterceptor();
        Event etlEvent = logETLInterceptor.intercept(simpleEvent);
        if (etlEvent == null) {
            System.out.println("这一条数据被过滤了");
            return;
        } else {
            System.out.println("head" + etlEvent.getHeaders().toString());
            System.out.println("body:" + new String(etlEvent.getBody()));
        }

        //测试 封装类型到head
       /* LogTypeInterceptor logTypeInterceptor = new LogTypeInterceptor();
        Event typeEvent = logTypeInterceptor.intercept(simpleEvent);
        System.out.println(typeEvent.getHeaders().toString());
        System.out.println(new String(typeEvent.getBody()));*/


    }
}

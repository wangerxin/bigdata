package com.atguigu.gmall1111logger.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall1111_commom.constant.GmallConstant;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * 接受客户端的请求
 */
@RestController // 等价于Responsebody+controller ，表示响应数据而不是页面
public class LoggerController {

    //使用logger把接受到的日志打印到磁盘
    private static final  org.slf4j.Logger logger = LoggerFactory.getLogger(LoggerController.class) ;

    @Autowired
    KafkaTemplate kafkaTemplate;

     @PostMapping("/log")
      public String getLog( @RequestParam("log") String log ){

         //给日志添加服务器时间
         JSONObject jsonObject  = JSON.parseObject(log);
         jsonObject.put("ts",System.currentTimeMillis()+3600*1000*24 +new Random().nextInt(3600*1000*5));
         String newLog = jsonObject.toJSONString();

         //根据不同的日志类型发送到不同的kafka的topic
         if( "startup".equals(jsonObject.getString("type"))  ){
             kafkaTemplate.send(GmallConstant.KAFKA_TOPIC_STARTUP,newLog);
         }else{
             kafkaTemplate.send(GmallConstant.KAFKA_TOPIC_EVENT,newLog);
         }

         // 写日志到磁盘
         logger.info(newLog);
          return  "";
      }

}

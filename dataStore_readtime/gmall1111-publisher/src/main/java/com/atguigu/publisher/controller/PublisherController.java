package com.atguigu.publisher.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.publisher.service.PublisherService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
public class PublisherController {

    @Autowired
    PublisherService publisherService;

    @GetMapping("realtime-total")
    public String getRealTotal(@RequestParam("date") String date){

        //[{"id":"dau","name":"新增日活","value":1200},
        // {"id":"new_mid","name":"c","value":233}]

        //使用JSONArray封装数据
        JSONArray jsonArray = new JSONArray();

        //查询service
        int dauTotal = publisherService.getDauTotal(date);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id","dau");
        jsonObject.put("name","新增日活");
        jsonObject.put("value",dauTotal);
        jsonArray.add(jsonObject);

        return jsonArray.toJSONString();
    }

    @GetMapping("realtime-hour")
    public String getRealtimeHour(@RequestParam("id") String id,@RequestParam("date") String date ){

        JSONObject jsonObject = new JSONObject();
        if ("dau".equals(id)){

            //查询数据
            Map todayMap = publisherService.getDauHours(date);
            //Map  yesterdayMap= publisherService.getDauHours(getYesterday(date));

            //封装结果
            //jsonObject.put("yesterday",yesterdayMap);
            jsonObject.put("today",todayMap);
        }

        return jsonObject.toJSONString();
    }

    public String getYesterday(String today){
        Date todayDt=new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            todayDt = simpleDateFormat.parse(today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date yesterdayDt = DateUtils.addDays(todayDt, -1);
        String yesterday = simpleDateFormat.format(yesterdayDt);
        return yesterday;

    }


}

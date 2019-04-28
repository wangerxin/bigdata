package com.atguigu.appclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BootStrap {
    public static void main(String[] args) {

        //josn
        JSONObject json = new JSONObject();
        json.put("key1", "value1");
        if (json.containsKey("key1")) {
            Object value1 = json.get("key1");
            System.out.println(value1);
        }

        json.remove("key1");
        //json数组
        JSONArray array = new JSONArray();

    }
}

package com.atguigu.publisher.service;

import java.util.Map;

public interface PublisherService {

    //--日活模块
    // 查询日活总数
    public int getDauTotal(String date );
    // 查询分时日活数量
    public Map getDauHours(String date );

    //--金额模块
    //查询每日金额
    //查询每日分时金额
}

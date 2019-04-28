package com.atguigu.ct.producer;

import com.atguigu.ct.common.bean.Producer;
import com.atguigu.ct.producer.bean.LocalFileProducer;

public class BootStrap {
    public static void main(String[] args) {
        //1.构建生产对象
        Producer producer=new LocalFileProducer();
        //2.生产数据
        //3.关闭资源
    }
}

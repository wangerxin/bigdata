package com.atguigu.utils

import java.io.{InputStream, InputStreamReader}
import java.util.Properties


object PropertiesUtil {


  /**
    * 测试加载配置文件
    * @param args
    */
  def main(args: Array[String]): Unit = {

    //加载配置文件,返回配置对象
    val properties: Properties = PropertiesUtil.load("config.properties")
    println(properties.getProperty("kafka.broker.list"))
  }

  /**
    * 加载配置文件,返回配置对象
    * @param fileName
    * @return
    */
  def load(fileName:String): Properties ={

    val prop=new Properties();
    val inputStream: InputStream = Thread.currentThread().getContextClassLoader.getResourceAsStream(fileName)
    prop.load(new InputStreamReader(inputStream,"UTF-8"))

    prop
  }



}

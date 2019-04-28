package com.atguigu.sparkmall.common.util

import java.text.SimpleDateFormat
import java.util.{Date, ResourceBundle}

import com.alibaba.fastjson.{JSON, JSONObject}



object ConfigurationUtil {



  //todo 配置文件类
  //从.properties配置文件中获取vlaue
  def getValueFromProperties(fileName: String, key: String) = {
    val bundle: ResourceBundle = ResourceBundle.getBundle(fileName)
    bundle.getString(key)
  }

  //从config.properties配置文件中获取vlaue
  def getValueFromConfig(key: String) = {
    val bundle: ResourceBundle = ResourceBundle.getBundle("config")
    bundle.getString(key)
  }

  //从condition.properties配置文件中获取vlaue
  def getValueFromCondition(key: String) = {
    val jsonStr: String = getValueFromProperties("condition","condition.params.json")
    val conditionObj: JSONObject = JSON.parseObject(jsonStr)
    conditionObj.getString(key)
  }

  // TODO 字符串类

  //判断字符串是否为空
  def isNotEmpty(str :String)={
    str != null && !"".equals(str)
  }


  //todo 时间格式类
  def timeStampToDateStr(timeStramp :Long ,format:String = "yyyy-MM-dd") ={

    val date = new Date(timeStramp)
    val sdf: SimpleDateFormat = new SimpleDateFormat(format)
    val dateStr: String = sdf.format(date)
    dateStr
  }

  def main(args: Array[String]): Unit = {

    val ts: Long = System.currentTimeMillis()

    val str: String = timeStampToDateStr(ts)


  }


}


package com.atguigu.spark_kudu

import com.alibaba.fastjson.JSON

object JsonUtil {

  def isJson(str: String): Boolean = {
    var result: Boolean = false
    try {
      val obj: AnyRef = JSON.parse(str)
      result = true
    } catch {
      case e: Exception =>
    }
    result
  }
}

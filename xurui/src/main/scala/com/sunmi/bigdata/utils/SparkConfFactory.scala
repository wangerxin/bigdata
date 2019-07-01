package com.sunmi.bigdata.utils

import org.apache.spark.SparkConf

object SparkConfFactory {
  def getSparkConf: SparkConf = {
    val sparkConf = new SparkConf()
      .setAppName(s"${this.getClass.getSimpleName}")
    sparkConf
  }
}

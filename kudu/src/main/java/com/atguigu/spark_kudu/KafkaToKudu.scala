package com.atguigu.test

import java.util

import com.alibaba.fastjson.{JSON, JSONObject}
import com.atguigu.spark_kudu.{JsonUtil, KafkaUtil, KuduUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kudu.client.{Insert, KuduClient, PartialRow}
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}
import scala.xml.Null


object KafkaToKudu {

  def main(args: Array[String]): Unit = {

    //spark环境
    val sparkConf: SparkConf = new SparkConf().setAppName("KafkaToKudu").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    //消费kafka,创建spark流
    val kafkaStreaming: InputDStream[ConsumerRecord[String, String]] = KafkaUtil.getKafkaStream("first", ssc)
    kafkaStreaming.foreachRDD(rdd => {
      rdd.foreachPartition(partition => {
        //连接kudu
        val kuduClient = new KuduClient.KuduClientBuilder("hadoop103").build //获取kudu连接
        val kuduTable = kuduClient.openTable("user") //获取kudu table
        val kuduSession = kuduClient.newSession()

        //获取最新kudu表列名和数据类型
        val tableColumnMap: Map[String, String] = KuduUtil.getSchema(kuduTable)

        partition.foreach(record => {
          val logStr: String = record.value()
          var logJson: JSONObject = null

          //log是否可以解析为JsonObject
          if (JsonUtil.isJson(logStr)) {
            logJson = JSON.parseObject(logStr)

            //将log封装为row
            val insert: Insert = kuduTable.newInsert()
            val row: PartialRow = insert.getRow
            KuduUtil.setRow(row, logJson, tableColumnMap)

            //将row写入kudu表
            try {
              kuduSession.flush()
              kuduSession.apply(insert)
              println("success: " + logStr)
            } catch {
              case e: Exception => {
                e.printStackTrace()
              }
            } finally {
              KuduUtil.close(kuduSession, kuduClient)
            }
          } else {

            //不能解析为JsonObject
            println(logStr)
          }
        })
      })
    })
    ssc.start()
    ssc.awaitTermination()
    //{"id":7,"name":"a7","city":"beijing","age":20}
  }
}

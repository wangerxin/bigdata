package com.atguigu.realtime

import com.MyesUtil
import com.alibaba.fastjson.JSON
import com.atguigu.bean.OrderInfo
import com.atguigu.gmall1111.commom.constant.GmallConstant
import com.atguigu.utils.MyKafkaUtil
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object OrderApp {

  def main(args: Array[String]): Unit = {

    //创建spark客户端
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("orderApp")
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))

    //消费kafka
    val recordDS: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream(GmallConstant.KAFKA_TOPIC_ORDER, ssc)

    //转换为case class
    val orderInfoDS: DStream[OrderInfo] = recordDS.map { record =>

      // record => order
      val orderStr: String = record.value()
      val orderInfo: OrderInfo = JSON.parseObject(orderStr, classOf[OrderInfo])

      //2019-01-02 03:04:05
      val dateTimeArr: Array[String] = orderInfo.createTime.split(" ")
      val timeArr: Array[String] = dateTimeArr(1).split(":")

      //2019-01-02
      orderInfo.createDate = dateTimeArr(0)
      //03
      orderInfo.createHour = timeArr(0)
      //03:04
      orderInfo.createHourMinute = timeArr(0) + ":" + timeArr(1)

      orderInfo
    }

    //写入es
    orderInfoDS.foreachRDD(rdd => {
      rdd.foreachPartition { iter => {

        val orderInfoList: List[OrderInfo] = iter.toList
        MyesUtil.insertEsBulk(GmallConstant.ES_INDEX_ORDER,orderInfoList)
      }
      }
    })
  }
}

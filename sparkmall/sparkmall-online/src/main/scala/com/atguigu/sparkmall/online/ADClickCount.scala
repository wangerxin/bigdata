package com.atguigu.sparkmall.online

import com.atguigu.sparkmall.common.model.UserAD
import com.atguigu.sparkmall.common.util.{ConfigurationUtil, MyKafkaUtil, RedisUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s.jackson.JsonMethods
import redis.clients.jedis.Jedis

//每天各地区top3 热门广告

object ADClickCount {

  def main(args: Array[String]): Unit = {

    // 准备Sspark环境
    val sparkConf = new SparkConf().setAppName("blackName").setMaster("local[*]").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    val sparkContext: SparkContext = ssc.sparkContext
    sparkContext.setCheckpointDir("checkpoint")

    //消费kafka数据, 创建DStream
    val topic = ConfigurationUtil.getValueFromConfig("kafka.topic")
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream(topic, ssc)

    // InputDStream[ConsumerRecord[String, String]] =》 UserAd
    val userAdDsream: DStream[UserAD] = kafkaDStream.map(record => {

      val massage: String = record.value()
      val fields = massage.split(" ")
      UserAD(fields(0), fields(1), fields(2), fields(3), fields(4))
    })

    //todo 使用窗口函数
    // 窗口大小
    // 步长大小
    // 窗口分组大小, 如果有应当与步长大小一致, 才有实际意义
    // rdd
    val windowDStream: DStream[UserAD] = userAdDsream.window(Seconds(60),Seconds(10))

    // UserAD => ((dateTime),1)
    val tunlpDStream: DStream[(String, Int)] = windowDStream.map(userAd => {
      val ts: String = userAd.ts
      val dateTime: String = ConfigurationUtil.timeStampToDateStr(ts.toLong,"yyyy-MM-dd HH:mm:ss")
      val dateTimeSub: String = dateTime.substring(0,dateTime.length-1)

      (dateTimeSub+"0", 1)
    })

    // reducebykey
    val reduceDStream: DStream[(String, Int)] = tunlpDStream.reduceByKey(_+_)

    //sort
    val sortDStream: DStream[(String, Int)] = reduceDStream.transform(rdd => {
      rdd.sortBy {
        case (dateTime, count) => {
          dateTime
        }
      }
    })
    sortDStream.print()

    //启动
    ssc.start()
    ssc.awaitTermination()
  }
}

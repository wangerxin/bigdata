package com.atguigu.sparkmall.online

import java.util

import com.atguigu.sparkmall.common.model.UserAD
import com.atguigu.sparkmall.common.util.{ConfigurationUtil, MyKafkaUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import redis.clients.jedis.Jedis

//每天各地区各城市各广告的点击流量实时统计。
object AdCount {

  def main(args: Array[String]): Unit = {

    // 准备Sspark环境
    val sparkConf = new SparkConf().setAppName("blackName").setMaster("local[*]").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val ssc = new StreamingContext(sparkConf, Seconds(3))
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

    // UserAD => ((date,area,city,AD),1)
    val tunlpDStream: DStream[(String, Int)] = userAdDsream.map(userAd => {
      val ts: String = userAd.ts
      val date: String = ConfigurationUtil.timeStampToDateStr(ts.toLong)

      (date + "_" + userAd.area + "_" + userAd.city + "_" + userAd.adId, 1)
    })

    // updateStateByKey
    val adCount: DStream[(String, Int)] = tunlpDStream.updateStateByKey {
      case (seq, option) => {
        val sum = seq.sum + option.getOrElse(0)
        Option(sum)
      }
    }

    // 写入redis
    adCount.foreachRDD(rdd=>{

      rdd.foreachPartition(iter=>{
        val jedis: Jedis = new Jedis("hadoop102",6379)
        for ((someAD,count) <- iter) {
          jedis.hset("adCount",someAD,count.toString)
        }
        jedis.close()
      })
    })

    //启动
    ssc.start()
    ssc.awaitTermination()
  }
}

package com.atguigu.sparkmall.online

import com.atguigu.sparkmall.common.model.UserAD
import com.atguigu.sparkmall.common.util.{ConfigurationUtil, MyKafkaUtil, RedisUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.json4s.jackson.JsonMethods
import redis.clients.jedis.Jedis

//每天各地区top3 热门广告

object AdTop3 {

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

    // UserAD => ((date_area_city_AD),1)
    val tunlpDStream: DStream[(String, Int)] = userAdDsream.map(userAd => {
      val ts: String = userAd.ts
      val date: String = ConfigurationUtil.timeStampToDateStr(ts.toLong)

      (date + "_" + userAd.area + "_" + userAd.city + "_" + userAd.adId, 1)
    })

    // updateStateByKey => ((date_area_city_AD),count)
    val adCount: DStream[(String, Int)] = tunlpDStream.updateStateByKey {
      case (seq, option) => {
        val sum = seq.sum + option.getOrElse(0)
        Option(sum)
      }
    }

    // map : DStream[((date_area_city_AD),count)] => DStream[(date_area, (ad, Int))]
    val date_area_AD_count_DStream: DStream[(String, (String, Int))] = adCount.map {

      case (date_area_city_AD, count) => {
        val field: Array[String] = date_area_city_AD.split("_")
        (field(0) + "_" + field(1), (field(3), count))
      }
    }

    // groupbykey :  DStream[(date_area, (ad, count))] => DStream[(date_area, Iterable[(ad, count)])]
    val groupByDateAreaDStream: DStream[(String, Iterable[(String, Int)])] = date_area_AD_count_DStream.groupByKey()

    // sort take(3) toMap => DStream[(date_area, Map[ad, count])]
    val resultDStream: DStream[(String, Map[String, Int])] = groupByDateAreaDStream.mapValues(iter => {

      iter.toList.sortWith {
        case (left, right) => {
          left._2 > right._2
        }
      }.take(3).toMap
    })
    resultDStream.print()

    // 写入redis
    resultDStream.foreachRDD(rdd => {
      rdd.foreachPartition(iter => {

        val jedis: Jedis = RedisUtil.getJedisClient
        import org.json4s.JsonDSL._

        for ((date_area, map) <- iter) {
          val value: String = JsonMethods.compact(JsonMethods.render(map))
          jedis.hset("date:area:perday", date_area, value)
        }
        jedis.close()
      })
    })

    //启动
    ssc.start()
    ssc.awaitTermination()
  }
}

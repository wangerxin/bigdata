package com.atguigu.sparkmall.online

import java.util

import com.atguigu.sparkmall.common.model.UserAD
import com.atguigu.sparkmall.common.util.{ConfigurationUtil, MyKafkaUtil, RedisUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

object AdBlackName {

  def main(args: Array[String]): Unit = {

    // 准备Sspark环境
    val sparkConf = new SparkConf().setAppName("blackName").setMaster("local[*]")//.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    val sparkContext: SparkContext = ssc.sparkContext
    sparkContext.setCheckpointDir("checkpoint")

    //消费kafka数据, 创建DStream
    val topic = ConfigurationUtil.getValueFromConfig("kafka.topic")
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream(topic, ssc)
    kafkaDStream.foreachRDD(rdd=>{
      println("过滤前:" + rdd.count())
    })


    //InputDStream[ConsumerRecord[String, String]] => DStream[UserAD]
    val userADDStream: DStream[UserAD] = kafkaDStream.map(record => {
      val log: String = record.value()
      val fields: Array[String] = log.split(" ")
      UserAD(fields(0), fields(1), fields(2), fields(3), fields(4))
    })


    // 查询redis, 过滤黑名单
    val filterDStream: DStream[UserAD] = userADDStream.transform(rdd => {

      //查询redis,并使用广播变量
      val jedis: Jedis = new Jedis("hadoop102", 6379)
      val blackNameSet: util.Set[String] = jedis.smembers("blackName")
      val blackNameSetBD = sparkContext.broadcast(blackNameSet)
      jedis.close()

      val filterRDD: RDD[UserAD] = rdd.filter(userAD => {
        !blackNameSetBD.value.contains(userAD.userId)
      })
      filterRDD
    })

    filterDStream.foreachRDD(rdd=>{
      println("过滤后:  " + rdd.count())
    })

    //转换 DStream[UserAD] => DStream[((date_userid_adid), 1)]
    val dateUserADOneDStream: DStream[(String, Int)] = filterDStream.map(userAD => {

      val ts: String = userAD.ts
      val dateStr: String = ConfigurationUtil.timeStampToDateStr(ts.toLong)

      (dateStr + "_" + userAD.userId + "_" + userAD.adId, 1)
    })

    // 聚合DStream[((date_userid_adid), 1)] => DStream[((date_userid_adid), count)]
    val dateUserADCountDStream: DStream[(String, Int)] = dateUserADOneDStream.updateStateByKey {
      case (seq, option) => {
        val sum = seq.sum + option.getOrElse(0)
        Option(sum)
      }
    }

    // 将黑名单写入redis, DStream[((date_userid_adid), count)]
    dateUserADCountDStream.foreachRDD(rdd => {

      rdd.foreachPartition(iter => {
        //val jedis: Jedis = RedisUtil.getJedisClient
        val jedis = new Jedis("hadoop102", 6379)

        for ((key, count) <- iter) {
          if (count >= 50) {
            val userId: String = key.split("_")(1)
            jedis.sadd("blackName", userId)
          }
        }
        jedis.close()
      })
    })

    //启动
    ssc.start()
    ssc.awaitTermination()

  }
}

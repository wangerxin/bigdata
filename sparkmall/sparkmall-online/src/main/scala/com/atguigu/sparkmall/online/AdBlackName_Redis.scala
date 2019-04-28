package com.atguigu.sparkmall.online

import java.util

import com.atguigu.sparkmall.common.model.UserAD
import com.atguigu.sparkmall.common.util.{ConfigurationUtil, MyKafkaUtil, RedisUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}
import redis.clients.jedis.Jedis

object AdBlackName_Redis {

  def main(args: Array[String]): Unit = {

    // 准备Sspark环境
    val sparkConf = new SparkConf().setAppName("blackName").setMaster("local[*]")
    //.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val ssc = new StreamingContext(sparkConf, Seconds(3))
    val sparkContext: SparkContext = ssc.sparkContext
    sparkContext.setCheckpointDir("checkpoint")

    //消费kafka数据, 创建DStream
    val topic = ConfigurationUtil.getValueFromConfig("kafka.topic")
    val kafkaDStream: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream(topic, ssc)
    kafkaDStream.foreachRDD(rdd => {
      println("过滤前:" + rdd.count())
    })


    //InputDStream[ConsumerRecord[key, value]] => DStream[UserAD]
    val userADDStream: DStream[UserAD] = kafkaDStream.map(record => {
      val log: String = record.value()
      val fields: Array[String] = log.split(" ")
      UserAD(fields(0), fields(1), fields(2), fields(3), fields(4))
    })

    // 查询redis, 过滤黑名单
    val filterDStream: DStream[UserAD] = userADDStream.transform(rdd => {

      //查询redis,并使用广播变量
      //val jedis: Jedis = new Jedis("hadoop102", 6379)
      val jedis: Jedis = RedisUtil.getJedisClient
      val blackNameSet: util.Set[String] = jedis.smembers("blackNameRedis")
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

    // DStream[UserAD] => DStream[(String, Int)]
    val dateUseridAdid2OneDStream: DStream[(String, Int)] = filterDStream.map(userAD => {

      val dateStr: String = ConfigurationUtil.timeStampToDateStr(userAD.ts.toLong)

      (dateStr + "_" + userAD.userId + "_" + userAD.adId, 1)
    })


    // 在redis中累加
    dateUseridAdid2OneDStream.foreachRDD(rdd => {

      rdd.foreachPartition(iter => {

        // 创建jedis客户端
        //val jedis: Jedis = new Jedis("hadoop102", 6379)
        val jedis: Jedis = RedisUtil.getJedisClient

        // 写入redis
        for ((dateUseridAdid, one) <- iter) {

          val key= "data:userid:adid"
          jedis.hincrBy(key, dateUseridAdid, 1)

          // 如果大于50 , 将userid加入黑名单
          val countStr: String = jedis.hget(key, dateUseridAdid)
          val count = countStr.toInt
          if (count > 50) {
            jedis.sadd("blackNameRedis", dateUseridAdid.split("_")(1))
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

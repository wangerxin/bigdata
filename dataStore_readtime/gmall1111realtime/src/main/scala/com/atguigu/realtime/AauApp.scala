package com.atguigu.realtime

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import com.MyesUtil
import com.alibaba.fastjson.JSON
import com.atguigu.bean.StartUpLog
import com.atguigu.gmall1111.commom.constant.GmallConstant
import com.atguigu.utils.{MyKafkaUtil, RedisUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

import scala.collection.mutable.ArrayBuffer

object AauApp {

  def main(args: Array[String]): Unit = {

    //创建spark客户端
    val sparkConf: SparkConf = new SparkConf().setAppName("DauApp").setMaster("local[*]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    val sc: SparkContext = ssc.sparkContext

    //消费kakfa,创建DStream
    val recordDStream: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream(GmallConstant.KAFKA_TOPIC_STARTUP, ssc)

    //结构转换: InputDStream[ConsumerRecord[String, String]] => DStream[StartUpLog]
    val startUpLogDStrem: DStream[StartUpLog] = recordDStream.map(record => {

      //取record的value值,转换成样例类
      val recordStr = record.value()
      val startUpLog: StartUpLog = JSON.parseObject(recordStr, classOf[StartUpLog])

      //取出时间戳,解析成"yyyy-MM-dd HH:mm"
      val dateTimeStr: String = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(startUpLog.ts))
      val dateTimeArray: Array[String] = dateTimeStr.split(" ")
      startUpLog.logDate = dateTimeArray(0)
      startUpLog.logHour = dateTimeArray(1).split(":")(0)
      startUpLog.logHourMinute = dateTimeArray(1)

      startUpLog
    })

    //过滤
    val filterDStream: DStream[StartUpLog] = startUpLogDStrem.transform(rdd => {

      //driver端周期性执行
      //查询redis,获取所有日活用户集合
      val jedis: Jedis = RedisUtil.getJedisClient
      val dauSet: util.Set[String] = jedis.smembers("dau:2019-04-29")//这个时间应该用变量
      jedis.close()

      //封装成广播变量,发送到executor
      val dauSetBD: Broadcast[util.Set[String]] = sc.broadcast(dauSet)
      println("过滤前:" + rdd.count)

      //在executor中过滤
      val filterRDD: RDD[StartUpLog] = rdd.filter(startUpLog => {
        !dauSetBD.value.contains(startUpLog.mid)
      })

      println("过滤后:" + filterRDD.count)
      filterRDD
    })

    //mid写入redis, startLog写入es
    filterDStream.foreachRDD(rdd => {

      rdd.foreachPartition(iter => {

        //方法一: 发现出现上课说过的问题,数据被消费后不能重复消费,测试结果是toList之后,就没有数据写入redis
        //val list: List[StartUpLog] = iter.toList

        //方法二: 使用数组在for循环中添加数据,然后写入es
        val startUpLogArray: ArrayBuffer[StartUpLog] = new ArrayBuffer[StartUpLog]()

        //将mid写入redis
        val jedis: Jedis = RedisUtil.getJedisClient
        for (startUpLog <- iter) {
          val key = "dau:" + startUpLog.logDate
          val value = startUpLog.mid
          jedis.sadd(key, value)

          //将整条日志添加到数组
          startUpLogArray.append(startUpLog)
        }
        jedis.close()

        //将日志明细数据写入es,方便通过其他维度查询
        val startUpLogList: List[StartUpLog] = startUpLogArray.toList
        MyesUtil.insertEsBulk(GmallConstant.ES_INDEX_DAU,startUpLogList)
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}

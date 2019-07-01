package com.sunmi.bigdata.app

import java.util.Properties

import com.sunmi.bigdata.utils.{SparkConfFactory, SparkSessionSingleton}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Minutes, Seconds, StreamingContext}
import scalikejdbc.{ConnectionPool, DB, _}

object PrintMachineCount {

  // 从properties文件里获取各种参数
  val prop = new Properties()
  prop.load(this.getClass.getClassLoader().getResourceAsStream("printall.properties"))

  // 获取jdbc相关参数
  val driver = prop.getProperty("jdbcDriver")
  val jdbcUrl = prop.getProperty("jdbcUrl")
  val jdbcUser = prop.getProperty("jdbcUser")
  val jdbcPassword = prop.getProperty("jdbcPassword")

  // 设置批处理间隔
  val processingInterval = prop.getProperty("processingInterval").toLong

  // 获取kafka相关参数
  val brokers = prop.getProperty("brokers")
  val topic = prop.getProperty("topic")
  val group = prop.getProperty("group")
  val offersetTable = prop.getProperty("offersetTable")

  // 设置jdbc
  Class.forName(driver)
  // 设置连接池
  ConnectionPool.singleton(jdbcUrl, jdbcUser, jdbcPassword)

  def main(args: Array[String]): Unit = {

//    System.setProperty("hadoop.home.dir", "D:\\hadoop-2.6.0-cdh5.15.0") //本地测试

    val sparkConf = SparkConfFactory.getSparkConf
//      .setMaster("local[*]") //本地测试
    val sparkSession = SparkSessionSingleton.getSparkSession(sparkConf)
//    spark.streaming.backpressure.enabled=true 开启反压机制
    //可参考kafka的分区数量来决定并行度
    sparkSession.conf.set("spark.defalut.parallelism", "8")
    //每秒钟每个分区kafka拉取消息的速率     
//    sparkSession.conf.set("spark.streaming.kafka.maxRatePerPartition", "10")
    // 序列化     
    sparkSession.conf.set("spark.serilizer", "org.apache.spark.serializer.KryoSerializer")
    // 开启rdd的压缩
    sparkSession.conf.set("spark.rdd.compress", "true")


    //获取StreamingContext
    val ssc = new StreamingContext(sparkSession.sparkContext, Minutes(processingInterval)) //Minutes

    //配置Kafka参数
    val kafkaParams = Map[String, Object]("bootstrap.servers" -> brokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> group,
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean))


    //获取offerset
    val fromOffsets = queryOffset(offersetTable,topic)

    print("1")
    var offsetRanges: Array[OffsetRange] = Array.empty[OffsetRange]

    //创建kafka的direct流
    val messages: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](ssc, LocationStrategies.PreferConsistent,
      ConsumerStrategies.Assign[String, String](fromOffsets.keys.toList, kafkaParams, fromOffsets))


    messages.transform { rdd =>
      //获取消费后的offerset集
      offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      rdd
    }.map { consumerRecord =>
      //对key进行切分
      val splitString = consumerRecord.key().split("/")
      //日期
      val time = splitString(1)
      println(time)
      //机型
      val stype = splitString(2)
      //机器sn
      val sn = splitString(3)
      //size的长度
      val size = splitString.size

      //输出元组
      (time, stype, sn, size)
    }.filter { tuple =>
      //通过key的长度判断
      tuple._4 == 5
    }.map { tuple =>
      Row(tuple._2, tuple._3, tuple._1)
    }.foreachRDD { rdd => {
      val schemaString = "print_stype print_sn print_time"
      val field = schemaString.split(" ").map(x => StructField(x, StringType, nullable = true))
      val schema = StructType(field)
      val df: DataFrame = sparkSession.createDataFrame(rdd, schema)
      //        df.show()
      df.createOrReplaceTempView("tempTable")
      sparkSession.sql("set hive.exec.dynamic.partition.mode = nonstrict")
      sparkSession.sql("set hive.exec.dynamic.partition = true")
      val sql = "insert into ods.ods_print_nostruct_detail partition(dt) select print_stype,print_sn,print_time dt from tempTable"
      sparkSession.sql(sql)
      df.unpersist(true)


      DB.localTx { implicit session =>
        for (o <- offsetRanges) {
          // 保存offset
          sql"""update kafka_sparkstreaming_offset_info set untilOffset = ${o.untilOffset} where topic = ${o.topic} and groupId=${group} and `partition` = ${o.partition}""".update.apply()
          sql"""update kafka_sparkstreaming_offset_info set fromOffset = ${o.fromOffset} where topic = ${o.topic} and groupId=${group} and `partition` = ${o.partition}""".update.apply()
        }

      }
      println("插入hive成功了")
    }

    }

    ssc.start()
    ssc.awaitTermination()

  }


  //查询消费的offerset
  def queryOffset(tableName: String, topics : String): Map[TopicPartition, Long] = {
    DB.readOnly(
      implicit session => {
        SQL(s"select * from ${tableName} where topic='${topics}' and groupId='${group}'").map(rs => {
          (new TopicPartition(rs.string(1),
            rs.int(3)),
            rs.long(5))
        }).list().apply()
      }).toMap
  }

//  def queryOffset(tableName: String, topics: String): Map[TopicPartition, Long] = {
//    DB.readOnly(
//      implicit session => {
//        SQL(s"select * from ${tableName} where topic = '${topics}' and groupId='xurui'").map(rs => {
//          (new TopicPartition(rs.string("topic"),
//            rs.int("partition")),
//            rs.long("untilOffset"))
//        }).list().apply()
//      }).toMap
//  }


}


package offsetMySQL

import kafka.common.TopicAndPartition
import kafka.message.MessageAndMetadata
import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka.{HasOffsetRanges, KafkaUtils, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import scalikejdbc.{DB, SQL}
import scalikejdbc.config.DBs

object StreamingOffsetMySQL {

  def main(args: Array[String]): Unit = {

    // todo 1.env
    val conf = new SparkConf().setMaster("local[2]").setAppName("StreamingOffsetMySQL")
    val ssc = new StreamingContext(conf, Seconds(10))

    // todo 2.参数
    //Topics
    val topics = ConfigUtil.getStringValue("kafka.topics").split(",").toSet
    //kafka参数,这里应用了自定义的ConfigUtil工具类，来获取application.properties里的参数
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> ConfigUtil.getStringValue("metadata.broker.list"),
      "auto.offset.reset" -> ConfigUtil.getStringValue("auto.offset.reset"),
      "group.id" -> ConfigUtil.getStringValue("group.id")
    )

    //先使用scalikejdbc从MySQL数据库中读取offset信息
    //+------------+------------------+------------+------------+-------------+
    //| topic      | groupid          | partitions | fromoffset | untiloffset |
    //+------------+------------------+------------+------------+-------------+
    //MySQL表结构如上，将“topic”，“partitions”，“untiloffset”列读取出来
    //组成 fromOffsets: Map[TopicAndPartition, Long]，后面createDirectStream用到

    // todo 3.连接mysql，获取offset
    DBs.setup()
    //从mysql中查询offset
    val fromOffset = DB.readOnly(implicit session => {
      SQL("select * from baidu_offset").map(rs => {
        (TopicAndPartition(rs.string("topic"), rs.int("partitions")), rs.long("untiloffset"))
      }).list().apply()
    }).toMap

    // todo 4.如果MySQL表中没有offset信息，就从0开始消费；如果有，就从已经存在的offset开始消费
    val messages: InputDStream[(String, String)] = if (fromOffset.isEmpty) {
      println("从头开始消费...")
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
    } else {
      println("从已存在记录开始消费...")
      val messageHandler = (mm: MessageAndMetadata[String, String]) => (mm.key(), mm.message())
      KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, (String, String)](ssc, kafkaParams, fromOffset, messageHandler)
    }

    // todo 5.处理业务
    messages.foreachRDD(rdd => {
      if (!rdd.isEmpty()) {
        //输出rdd的数据量
        println("数据统计记录为：" + rdd.count())
        //官方案例给出的获得rdd offset信息的方法，offsetRanges是由一系列offsetRange组成的数组
        //          trait HasOffsetRanges {
        //            def offsetRanges: Array[OffsetRange]
        //          }
        val offsetRanges: Array[OffsetRange] = rdd.asInstanceOf[HasOffsetRanges].offsetRanges

        // todo 6.将最新offset保存到mysql
        offsetRanges.foreach(x => {
          //输出每次消费的主题，分区，开始偏移量和结束偏移量
          println(s"---${x.topic},${x.partition},${x.fromOffset},${x.untilOffset}---")
          //将最新的偏移量信息保存到MySQL表中
          DB.autoCommit(implicit session => {
            SQL("replace into baidu_offset(topic,groupid,partitions,fromoffset,untiloffset) values (?,?,?,?,?)")
              .bind(x.topic, ConfigUtil.getStringValue("group.id"), x.partition, x.fromOffset, x.untilOffset)
              .update().apply()
          })
        })
      }
    })

    ssc.start()
    ssc.awaitTermination()
  }
}

//package sparkStreaming
//
//import kafka.serializer.StringDecoder
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.spark.SparkConf
//import org.apache.spark.storage.StorageLevel
//import org.apache.spark.streaming.dstream.ReceiverInputDStream
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//
///**
//  * recerver方式
//  * 经验: 当前方式需要配置zookeeper地址
//  */
//object ReceiverStreaming {
//
//  def main(args: Array[String]): Unit = {
//
//    //1.创建SparkConf并初始化SSC
//    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("KafkaSparkStreaming")
//    val ssc = new StreamingContext(sparkConf, Seconds(5))
//
//    //2.将kafka参数映射为map
//    val kafkaParam: Map[String, String] = Map[String, String](
//      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
//      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
//      ConsumerConfig.GROUP_ID_CONFIG -> "gruopName",
//      //ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "hadoop102:9092,hadoop103:9092,hadoop104:9092",
//      "zookeeper.connect" -> "hadoop102:2181,hadoop103:2181" //如果没有配置zookeeper会报错
//    )
//
//    //3.通过KafkaUtil创建kafkaDSteam,接收的数据应该是(key,value),key是null,value有值
//    //StringDecoder是解码方式
//    val kafkaDSteam: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](
//      ssc,
//      kafkaParam,
//      Map("first"->1),
//      StorageLevel.MEMORY_ONLY
//    )
//
//    //4.对kafkaDSteam做计算（WordCount）
////    kafkaDSteam.foreachRDD { rdd => {
////        rdd.foreachPartition{partition =>{
////        }}
////      }
////    }
//
//    //6.启动SparkStreaming
//    kafkaDSteam.print()
//    ssc.start()
//    ssc.awaitTermination()
//  }
//}
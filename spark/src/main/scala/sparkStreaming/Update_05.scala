//package sparkStreaming
//
//
//import kafka.serializer.StringDecoder
//import org.apache.kafka.clients.consumer.ConsumerConfig
//import org.apache.spark.SparkConf
//import org.apache.spark.storage.StorageLevel
//import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import org.apache.spark.streaming.kafka.KafkaUtils
//
//object Update_05 {
//
//  def main(args: Array[String]): Unit = {
//
//    //初始化
//    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("socket")
//    val ssc = new StreamingContext(sparkConf, Seconds(5))
//    ssc.sparkContext.setCheckpointDir("input")
//
//    //读取kafka
//    val kakfaDStream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](
//      ssc,
//      Map(
//        ConsumerConfig.GROUP_ID_CONFIG -> "spark",
//        "zookeeper.connect" -> "hadoop102:2181",
//        ConsumerConfig.KEY_DESERIALIZER_CLASS_DOC -> "org.apache.kafka.common.serialization.StringDeserializer",
//        ConsumerConfig.VALUE_DESERIALIZER_CLASS_DOC -> "org.apache.kafka.common.serialization.StringDeserializer"
//      ),
//      Map("spark" -> 2),
//      StorageLevel.MEMORY_ONLY
//    )
//
//    //计算
//    val word2one: DStream[(String, Int)] = kakfaDStream.map(kv => (kv._2, 1))
//    val word_count: DStream[(String, Int)] = word2one.updateStateByKey {
//      case (seq, buffer) => {
//        val result: Int = seq.sum + buffer.getOrElse(0)
//        Option(result)
//      }
//    }
//
//    //打印
//    word_count.print()
//
//    //启动
//    ssc.start()
//    ssc.awaitTermination()
//  }
//}

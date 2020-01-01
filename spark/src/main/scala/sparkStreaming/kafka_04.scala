package sparkStreaming


import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils

object kafka_04 {

  def main(args: Array[String]): Unit = {

    //初始化
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("socket")
    val ssc = new StreamingContext(sparkConf,Seconds(5))

    //读取kafka
    val kakfaDStream: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](
      ssc,
      Map(
        ConsumerConfig.GROUP_ID_CONFIG -> "spark",
        "zookeeper.connect" -> "hadoop102:2181",
        ConsumerConfig.KEY_DESERIALIZER_CLASS_DOC -> "org.apache.kafka.common.serialization.StringDeserializer",
        ConsumerConfig.VALUE_DESERIALIZER_CLASS_DOC -> "org.apache.kafka.common.serialization.StringDeserializer"
      ),
      Map("sparkmall2" -> 3),
      StorageLevel.MEMORY_ONLY
    )

    //计算
    val word2one: DStream[(String, Int)] = kakfaDStream.map(kv => (kv._2, 1))
    val word2count: DStream[(String, Int)] = word2one.reduceByKey(_+_)

    //打印
    word2count.print()

    //启动
    ssc.start()
    ssc.awaitTermination()
  }

}
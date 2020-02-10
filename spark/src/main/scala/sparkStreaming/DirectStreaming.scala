package sparkStreaming

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object DirectStreaming {

  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("DirectStreaming")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val kafkaStreaming: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream("first", ssc)
    kafkaStreaming.foreachRDD(rdd => {
      rdd.foreachPartition(partiton => {
        partiton.foreach(line=>{
          println(line.value())
        })
      })
    })

    ssc.start()
    ssc.awaitTermination()


  }
}

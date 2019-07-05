package sparkStreaming

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SparkStreamSql {

  def main(args: Array[String]): Unit = {

    //env
    val sparkConf: SparkConf = new SparkConf().setAppName("demo").setMaster("local[*]")
    val streamingContext = new StreamingContext(sparkConf, Seconds(20))
    val sparkContext: SparkContext = streamingContext.sparkContext
    sparkContext.setLogLevel("WARN")

    //create sparkstreaming
    val socketDStream: ReceiverInputDStream[String] = streamingContext.socketTextStream("localhost", 9999)

    // func 先声明，后使用
    val func = (rdd: RDD[String]) => {

      //sparkSession env
      val sparkSession: SparkSession = SparkSession
        .builder()
        .config(rdd.sparkContext.getConf)
        .getOrCreate()
      import sparkSession.implicits._

      // make DS
      val workDS: Dataset[Worker] = rdd.map(_.split(" "))
        .map((fields: Array[String]) => Worker(fields(0), fields(1).toFloat))
        .toDS()

      //spark-sql
      workDS.show()
      workDS.groupBy("name").sum("salary").show()
    }

    socketDStream.foreachRDD(func)

    //stop
    streamingContext.start()
    streamingContext.awaitTermination()

  }
}

case class Worker(name: String, salary: Float)
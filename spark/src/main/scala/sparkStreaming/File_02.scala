package sparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object File_02{

  def main(args: Array[String]): Unit = {


    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("socket")

    val ssc = new StreamingContext(sparkConf,Seconds(5))

    val fileDStream: DStream[String] = ssc.textFileStream("input")

    val word: DStream[String] = fileDStream.flatMap(_.split(" "))

    val word2one: DStream[(String, Int)] = word.map((_,1))

    val word2count: DStream[(String, Int)] = word2one.reduceByKey(_+_)

    word2count.print()

    ssc.start()

    ssc.awaitTermination()
  }

}

package sparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Transform_07 {

  def main(args: Array[String]): Unit = {

    //初始化ssc
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("socket")
    val ssc = new StreamingContext(sparkConf,Seconds(3))

    //监听端口
    val socketDStream: ReceiverInputDStream[String] = ssc.socketTextStream("localhost",9999)

    socketDStream.print()

    //切割
    val word: DStream[String] = socketDStream.flatMap(_.split(" "))

    //transform成元组
    val word2one: DStream[(String, Int)] = word.transform(rdd => rdd.map((_,1)))

    //聚合
    val reslut: DStream[(String, Int)] = word2one.reduceByKey(_+_)

    //打印
    //reslut.print()

    //启动
    ssc.start()
    ssc.awaitTermination()
  }
}

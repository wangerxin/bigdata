package sparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object Window_07 {

  def main(args: Array[String]): Unit = {

    //初始化ssc
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("socket")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    //读取端口数据
    val socketDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop102", 9999)

    // todo 使用窗口函数,目的主要是指定
    val windowDStrem: DStream[String] = socketDStream.window(Seconds(60),Seconds(10))




    //启动
    ssc.start()
    ssc.awaitTermination()
  }
}

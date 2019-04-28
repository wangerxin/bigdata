package sparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}

object Window_06 {

  def main(args: Array[String]): Unit = {

    //初始化ssc
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("socket")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    //读取端口数据
    val socketDStream: ReceiverInputDStream[String] = ssc.socketTextStream("hadoop102", 9999)

    //切割
    val word: DStream[String] = socketDStream.flatMap(_.split(" "))

    //映射成元组
    val word2one: DStream[(String, Int)] = word.map((_, 1))

    //聚合时使用窗口函数
    val reslut: DStream[(String, Int)] = word2one.reduceByKeyAndWindow((x:Int,y:Int)=>x+y,Seconds(20),Seconds(5))

    //打印
    reslut.print()

    //启动
    ssc.start()
    ssc.awaitTermination()
  }

}

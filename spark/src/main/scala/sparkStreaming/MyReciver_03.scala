package sparkStreaming

import java.io.{BufferedReader, InputStream, InputStreamReader}
import java.net.Socket

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.receiver.Receiver

/**
  * 自定义端口数据接收器
  * 核心分为三大块 : 启动接收器, 关闭接收器, 接收业务逻辑
  * @param hostName
  * @param port
  */
class MyReciver(hostName: String, port: Int) extends Receiver[String](StorageLevel.MEMORY_ONLY) {


  var socket: Socket = null

  //启动接收器, 接收数据
  override def onStart(): Unit = {

    new Thread(new Runnable {
      override def run(): Unit = {

        //接收数据
        reciver()
      }
    }).start()
  }

  //关闭接收器
  override def onStop(): Unit = {
    synchronized {
      if (socket != null) {
        socket.close()
        socket = null
      }
    }
  }

  //接收器业务逻辑
  def reciver(): Unit = {

    //创建socket
    socket = new Socket(hostName, port)

    //获取字节流
    val stream: InputStream = socket.getInputStream

    //转换为字符流
    val bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"))

    //开始读取数据
    var line: String = null
    while ((line = bufferedReader.readLine()) != null) {

      if (!"--end--".equals(line)) {

        //存储数据
        store(line)
      } else {
        return
      }
    }
  }
}

object TestMyReciver {
  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("socket")

    val ssc = new StreamingContext(sparkConf, Seconds(3))

    val socketDStream: ReceiverInputDStream[String] = ssc.receiverStream(new MyReciver("hadoop102", 9999))

    val word: DStream[String] = socketDStream.flatMap(_.split(" "))

    val word2one: DStream[(String, Int)] = word.map((_, 1))

    val word2count: DStream[(String, Int)] = word2one.reduceByKey(_ + _)

    word2count.print()

    ssc.start()

    ssc.awaitTermination()

  }
}

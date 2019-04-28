package sparkcore

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object BroadCastTest {

  def main(args: Array[String]): Unit = {

    //初始化sparkContext
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("jdbc")
    val sc = new SparkContext(conf)

    //创建广播变量
    val bc: Broadcast[Int] = sc.broadcast(100)

    //task 使用广播变量
    val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 4, 5))
    rdd.foreach {
      x => {
        println(bc.value + x)
      }
    }

  }

}

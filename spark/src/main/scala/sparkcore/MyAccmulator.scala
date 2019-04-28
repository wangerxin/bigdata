package sparkcore

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.util.AccumulatorV2

class MyAccmulator extends AccumulatorV2[Int, Int] {

  //定义累加器初始值
  private var sum = 0

  //累加器是否为"零值"
  override def isZero = sum == 0

  //复制累加器
  override def copy() = {
    val acc = new MyAccmulator
    acc.sum = this.sum
    acc
  }

  //清空累加器
  override def reset(): Unit = sum = 0

  //task 内部累加逻辑
  override def add(v: Int): Unit = sum += v

  //driver端合并逻辑
  override def merge(other: AccumulatorV2[Int, Int]): Unit = sum += other.value

  //累加器的值
  override def value = sum
}

object test{
  def main(args: Array[String]): Unit = {

    //初始化sparkContext
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("jdbc")
    val sc = new SparkContext(conf)

    //创建,注册累加器
    val acc = new MyAccmulator
    sc.register(acc)

    sc.makeRDD(Array(1,2,3,4,5)).foreach(x=> acc.add(1))
    println(acc.value)


  }
}

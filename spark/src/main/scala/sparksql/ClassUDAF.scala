package sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql._
import org.apache.spark.sql.expressions.Aggregator

/**
  * 作用: 自定义强类型的UDAF函数, 求年龄的平均值
  * @param id
  * @param name
  * @param age
  */

//准备UDAF函数中需要的类型
case class Student(id: Long, name: String, age: Long)
case class SumCount(var sum: Long, var count: Long)

class ClassUDAF extends Aggregator[Student, SumCount, Double] {

  //1.初始化缓冲区
  override def zero: SumCount = new SumCount(0,0)

  //2.更新缓冲区
  override def reduce(b: SumCount, a: Student): SumCount = {

    b.sum += a.age
    b.count += 1
    b
  }

  //3.合并缓冲区
  override def merge(b1: SumCount, b2: SumCount): SumCount = {

    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }

  //4.计算结果
  override def finish(reduction: SumCount): Double = reduction.sum.toDouble / reduction.count

  //5.编码
  override def bufferEncoder: Encoder[SumCount] = Encoders.product
  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}

object Test{
  def main(args: Array[String]): Unit = {

    //初始化sparkSession
    val sparkConf = new SparkConf().setMaster("local").setAppName("sql")
    val spark = SparkSession
      .builder()
      .config(sparkConf)
      .getOrCreate()
    //开启隐式转换
    import spark.implicits._

    //创建DS : 强类型的UDAF只能与DS搭配使用
     val ds: Dataset[Student] = spark.read.json("input/people_json").as[Student]

    //创建自定义函数, 并将其转换为列
    val classUDAF = new ClassUDAF
    val AgeAvg: TypedColumn[Student, Double] = classUDAF.toColumn.name("")

    //使用自定义函数
    ds.select(AgeAvg).show()
    spark.stop()
  }
}

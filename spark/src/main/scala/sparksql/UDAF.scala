package sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructType}

/**
  * 作用: 自定义弱类型的聚合函数,求年龄的平均值
  * 注意事项: StructType : 结构类型
  *           LongType : sparksql对Long类型的封装
  *           MutableAggregationBuffer : 缓冲区数据类型,是Row的子类
  *           Row : 表示一行数据
  */
class AvgAge extends UserDefinedAggregateFunction{

  //输入参数的结构类型
  override def inputSchema: StructType = new StructType().add("age",LongType)

  //缓冲区的结构类型
  override def bufferSchema: StructType = new StructType().add("sum",LongType).add("count",LongType)

  //结果的结构类型
  override def dataType: DataType = DoubleType

  //函数是否为一致性
  override def deterministic: Boolean = true

  //初始化缓冲区
  override def initialize(buffer: MutableAggregationBuffer): Unit = {

    buffer(0) = 0L
    buffer(1) = 0L
  }

  //更新缓冲区
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {

    buffer(0) = buffer.getLong(0) + input.getLong(0)
    buffer(1) = buffer.getLong(1) + 1
  }

  //合并各个缓冲区
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {

    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  //计算结果
  override def evaluate(buffer: Row): Any = buffer.getLong(0).toDouble / buffer.getLong(1)
}

object Test2{

  def main(args: Array[String]): Unit = {

    //初始化sparkSession
    val sparkConf = new SparkConf().setMaster("local").setAppName("sql")
    val spark = SparkSession
      .builder()
      .config(sparkConf)
      .getOrCreate()

    //准备数据, 弱类型的UDAF只能与DF搭配使用
    val df1: DataFrame = spark.read.json("input/people_json")
    df1.createOrReplaceTempView("people")

    //创建并注册UDAF函数
    val avgAge = new AvgAge
    spark.udf.register("getAvg",avgAge)

    //使用自定义函数
    spark.sql("select getAvg(age) from people").show()
    spark.stop()

  }
}

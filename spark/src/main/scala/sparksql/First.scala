package sparksql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

object First {

  def main(args: Array[String]): Unit = {

    //初始化sparkSession
    val sparkConf = new SparkConf().setMaster("local").setAppName("sql")
    val spark = SparkSession
      .builder()
      .config(sparkConf)
      .getOrCreate()
    //开启隐式转换
    //import spark.implicits._

    //创建DF
    val df1: DataFrame = spark.read.json("input/people_json")

    //创建视图
    df1.createGlobalTempView("people")

    //执行sql
    val df2: DataFrame = spark.sql("select * from global_temp.people")
    df2.show()

    //关闭资源
    spark.stop()
  }
}

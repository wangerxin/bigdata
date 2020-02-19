package sparksql

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
  * 功能：mysql的读取和保存，update操作需要修改源码
  *
  * 涉及的类：
  * 读：DataFrameReader(读取器),DataFrame（读取之后得到DataFrame）
  * 写: 1.DataFrameWriter（写入器）,unit（写入第三方储存设备）
  *     2.SaveMode四种模式（append，overwrite，ErrorIfExists，Ignore）
  */
object JdbcMysql {

  def main(args: Array[String]): Unit = {

    //初始化sparkSession
    val sparkConf = new SparkConf().setMaster("local").setAppName("sql")
    val spark = SparkSession
      .builder()
      .config(sparkConf)
      .getOrCreate()

    //读：方式1，通用
    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/spark")
      .option("spark", "user")
      .option("user", "root")
      .option("password", "235236")
      .load()

    //读：方式2，读取jdbc
    val connectionProperties = new Properties()
    connectionProperties.put("user", "root")
    connectionProperties.put("password", "235236")
    val jdbcDF2 = spark.read
      .jdbc("jdbc:mysql://localhost:3306/spark", "spark.user2", connectionProperties)

    // 写：方式1，通用
    jdbcDF.write
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/spark")
      .option("spark", "spark.user")
      .option("user", "root")
      .option("password", "235236")
      .save()

    // 写：方式2，写入jdbc
    jdbcDF2.write.jdbc("jdbc:mysql://localhost:3306/spark", "spark.user2", connectionProperties)

    //关闭资源
    spark.stop()
  }
}

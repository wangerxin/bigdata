package sparksql

import java.util.Properties

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object JDBC {

  def main(args: Array[String]): Unit = {

    //初始化sparkSession
    val sparkConf = new SparkConf().setMaster("local").setAppName("sql")
    val spark = SparkSession
      .builder()
      .config(sparkConf)
      .getOrCreate()

    //读
    val properties = new Properties()
    properties.setProperty("user","root")
    properties.setProperty("password","235236")
    val url = "jdbc:mysql://localhost:3306/anli"
    val tableName = "user"
    val df: DataFrame = spark.read.jdbc(url,tableName,properties)
    df.show()

    //写
    import spark.implicits._
    df.write.mode("append").jdbc(url,tableName,properties)


    spark.stop()
  }

}

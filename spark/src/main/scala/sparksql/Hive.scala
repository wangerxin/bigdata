package sparksql

import java.io.File

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * 功能：可以在spark程序用使用hive-sql语句,就像在hive-shell里一样。
  *
  * 注意：如果idea调试的时候，需要将hive-site.xml文件放在resource目录下面
  * 如果将jar提交到集群上面运行，需要将hive-site.xml放在$SPARK_HOME/conf下面
  *
  * 异常：Unable to instantiate SparkSession with Hive support because Hive classes are not found.（导入spark-hive依赖）
  */
object Hive {

  def main(args: Array[String]): Unit = {
    val warehouseLocation: String = new File("spark-warehouse").getAbsolutePath

    val spark = SparkSession
      .builder()
      .appName("Spark Hive Example")
      .master("local[*]")
      .enableHiveSupport()
      .getOrCreate()

    val peopleDF: DataFrame = spark.sql("select * from spark.people")
    peopleDF.show()

    spark.sql("insert overwrite table spark.people select * from spark.people where uid = 1")
  }
}

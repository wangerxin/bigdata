package sc

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

object DataSetTest {

  def main(args: Array[String]): Unit = {

    //配置
    val conf: SparkConf = new SparkConf().setAppName("spark").setMaster("local[4]")
    //创建SparkSession
    val spark: SparkSession = SparkSession.builder().config(conf).getOrCreate()

    //1.创建rdd
    val rdd: RDD[(Int, String)] = spark.sparkContext.makeRDD(Array((1, "zhangsan"), (2, "lisi")))
    //rdd中的每一个元素的类型是T（T表示泛型）
    println("-------rdd---------")
    rdd.foreach(t => {
      println(t._1 + ":" + t._2)

    })

    //2.rdd转换为DataFrame
    import spark.implicits._
    val df: DataFrame = rdd.toDF("id", "name")
    //dataFrame中每一个元素的数据类型是ROW，理解为一行吧,type DataFrame = Dataset[Row]
    println("-------df---------")
    df.foreach(row => {
      val id: Int = row.getAs[Int]("id")
      val name: String = row.getAs[String]("name")
      println(id + ":" + name)
    })

    //3.DataFrame转换为Dataset
    val ds: Dataset[People] = df.as[People]
    //dataSet中每一个元素是一个对象
    println("-------ds---------")
    ds.foreach(people => {
      println(people.id + ":" + people.name)
    })
    ds.show()
  }
}

case class People(id: Int, name: String)

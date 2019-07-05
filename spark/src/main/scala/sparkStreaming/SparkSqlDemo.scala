package sparkStreaming

import org.apache.spark.SparkContext
import org.apache.spark.sql.{Dataset, SparkSession}

object SparkSqlDemo {

  def main(args: Array[String]): Unit = {

    //env
    val sparkSession: SparkSession = SparkSession
      .builder()
      .master("demo")
      .master("local[*]")
      .getOrCreate()
    val sparkContext: SparkContext = sparkSession.sparkContext
    sparkContext.setLogLevel("WARN")
    import sparkSession.implicits._

    //create df
    val peopleDS: Dataset[People] = sparkContext
      .makeRDD(Array((1001, "aa"), (1002, "bb")))
      .map(t => People(t._1, t._2))
      .toDS()
    peopleDS.cache()


    //df api
    peopleDS.show()
    peopleDS.select($"id"+1,$"name").show()
    peopleDS.groupBy("name").sum("id").show()

    // spark.sql
    peopleDS.createOrReplaceTempView("people")
    sparkSession.sql("select * from people where id=1001").show()

    //func
    val func=(people:People)=>people.id
    peopleDS.map(func).show()

    //stop
    sparkSession.stop()
  }
}

case class People(id: Int, name: String)
package sc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Test {

  def main(args: Array[String]): Unit = {

    //配置
    val conf: SparkConf = new SparkConf().setAppName("workcount").setMaster("local[*]")

    //sc
    val sc: SparkContext = new SparkContext(conf)

    val rdd: RDD[(String, Int)] = sc.makeRDD(List(("abc",1),("abc",2),("acc",3)))


    val result: RDD[(String, Iterable[Int])] = rdd.groupByKey()
    result.foreach(println(_))

  }

}

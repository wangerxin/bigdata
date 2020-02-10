package sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object LeftJoinTest {

  def main(args: Array[String]): Unit = {

    //配置
    val conf: SparkConf = new SparkConf().setAppName("workcount").setMaster("local[*]")

    //sc
    val sc: SparkContext = new SparkContext(conf)

    //读取文件
    val rdd1: RDD[(Int, Int)] = sc.makeRDD(Array((1,11),(2,12)))
    val rdd2: RDD[(Int, Int)] = sc.makeRDD(Array((1,11),(3,13)))

    val leftRDD: RDD[(Int, (Int, Option[Int]))] = rdd1.leftOuterJoin(rdd2)
    val value: RDD[(Int, (Int, Option[Int]))] = leftRDD.filter {
      case (key, (v1, v2)) => {
        v2 == None
      }
    }
    val array: Array[(Int, (Int, Option[Int]))] = leftRDD.collect()
    println(array.mkString(","))

    //join
    /*val rdd1 = sc.parallelize(Array((1,"a"),(2,"b"),(3,"c")))
    val rdd2 = sc.parallelize(Array((1,4),(2,5),(4,"d")))
    println(rdd1.join(rdd2).collect().mkString(","))
    println(rdd1.cogroup(rdd2).collect().mkString(","))*/

  }
}

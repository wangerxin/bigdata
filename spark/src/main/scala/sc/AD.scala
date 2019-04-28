package sc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object AD {

  def main(args: Array[String]): Unit = {

    //spark
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("ad")
    val sc = new SparkContext(sparkConf)

    //read : 时间戳，省份，城市，用户，广告
    val proAD2one: RDD[((String, String), Int)] = sc.textFile("e://input").map { line =>
      val fields: Array[String] = line.split(" ")
      ((fields(1), fields(4)), 1)
    }

    //((省份,广告),次数)
    val proAD2Count: RDD[((String, String), Int)] = proAD2one.reduceByKey(_+_)

    //(省份,(广告,次数))
    val pro2adCount: RDD[(String, (String, Int))] = proAD2Count.map {
      case ((pro, ad), count) => (pro, (ad, count))
    }

    //(省份,(广告,次数)....)
    val pro2group: RDD[(String, Iterable[(String, Int)])] = pro2adCount.groupByKey()

    //(省份,(广告,次数)....前三)
    val reslut: RDD[(String, List[(String, Int)])] = pro2group.map {
      case (pro, iter) =>
        val ad2count: List[(String, Int)] = iter.toList.sortWith((t1, t2) => t1._2 > t2._2)
        (pro, ad2count.take(3))
    }

    reslut.map{
      case (key,value) => (Integer.parseInt(key),value)
    }.sortByKey().foreach(println(_))


  }

}

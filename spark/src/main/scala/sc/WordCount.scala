package sc

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

  def main(args: Array[String]): Unit = {

    //配置
    val conf: SparkConf = new SparkConf().setAppName("workcount").setMaster("local[*]")

    //sc
    val sc: SparkContext = new SparkContext(conf)

    //读取文件
    val lines: RDD[String] = sc.textFile("/input")

    //压平
    val words: RDD[String] = lines.flatMap(x => x.split(" "))

    //元祖
    val tunlp: RDD[(String, Int)] = words.map(x => (x, 1))

    //聚合
    val result: RDD[(String, Int)] = tunlp.reduceByKey((x, y) => (x + y))

    //输出
    //result.foreach(x=>println(x))

    val rdd = sc.parallelize(List(("a", 3), ("a", 2), ("c", 4), ("b", 3), ("c", 6), ("c", 8)), 2)

    /*val avg: RDD[(String, Int)] = rdd.groupByKey().map {
      case (key, iter) => (key, iter.sum / iter.size)
    }
    avg.foreach(println(_))*/

    /*rdd.reduceByKey((x: Int, y: Int) => x + y)
    rdd.reduceByKey((x, y) => x + y)
    rdd.reduceByKey(_ + _)*/

    // aggreateByKey 求和
    /*println(rdd.aggregateByKey(0)((x: Int, y: Int) => x + y, (x: Int, y: Int) => x + y).collect().mkString(","))
    println(rdd.aggregateByKey(0)((x, y) => x + y, (x, y) => x + y).collect())
    println(rdd.aggregateByKey(0)(_ + _, _ + _).collect())*/


    //aggreatebykey 求平均值
    //(List(("a", 3), ("a", 2), ("c", 4), ("b", 3), ("c", 6), ("c", 8)), 2)
    /*val rdd2: RDD[(String, (Int, Int))] = rdd.aggregateByKey((0,0))( (init,v)=>(init._1+v,init._2+1), (x,y)=>(x._1+y._1,y._2+y._2) )
    println(rdd2.collect().mkString(","))*/

    //combinerBykey 求平均值
    rdd.combineByKey(
      (_, 1),
      (init: (Int, Int), v) => (init._1 + v, init._2 + 1),
      (x: (Int, Int), y: (Int, Int)) => (x._1 + y._1, x._2 + y._2)).foreach(println(_)
    )

    //join
    /*val rdd1 = sc.parallelize(Array((1,"a"),(2,"b"),(3,"c")))
    val rdd2 = sc.parallelize(Array((1,4),(2,5),(4,"d")))
    println(rdd1.join(rdd2).collect().mkString(","))
    println(rdd1.cogroup(rdd2).collect().mkString(","))*/


  }

}

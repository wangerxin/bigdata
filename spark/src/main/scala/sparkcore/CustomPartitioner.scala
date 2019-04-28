package sparkcore

import org.apache.spark.rdd.RDD
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

class CustomPartitioner(partitionNum: Int) extends Partitioner {

  override def numPartitions = partitionNum

  override def getPartition(key: Any) = {

    val k: Int = key.toString.toInt

    k % partitionNum
  }
}

object TestPartition {
  def main(args: Array[String]): Unit = {

    val sparkConf: SparkConf = new SparkConf().setMaster("local[2]").setAppName("partiton")
    val sc = new SparkContext(sparkConf)

    val rdd: RDD[(Int, String)] = sc.makeRDD(Array((1, "a"), (2, "b"), (3, "c"), (4, "d")))
    //print(rdd.partitioner)
    //println(rdd.getNumPartitions)

    //val partRDD: RDD[(Int, String)] = rdd.partitionBy(new CustomPartitioner(2))
   // print(partRDD.partitioner)
    //println(partRDD.getNumPartitions)

    //写
    //rdd.saveAsSequenceFile("e:/output")

    //读
    val rdd2: RDD[(Int, String)] = sc.sequenceFile[Int, String]("e:/output")
    rdd2.collect().foreach(println(_))
  }
}
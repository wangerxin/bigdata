package com.atguigu.sparkmall.sparkmallnotonline

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.UUID

import com.atguigu.sparkmall.common.model.UserVisitAction
import com.atguigu.sparkmall.common.util.ConfigurationUtil
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.apache.spark.util.AccumulatorV2

import scala.collection.{immutable, mutable}

object Top10 {

  def main(args: Array[String]): Unit = {

    //初始化ssc
    val sparkConf: SparkConf = new SparkConf().setAppName("top10").setMaster("local")
    val sparkSession: SparkSession = SparkSession.
      builder().
      config(sparkConf).
      enableHiveSupport(). //spark提供了部分hive依赖, 需要添加hive支持加载更多依赖
      getOrCreate()
    import sparkSession.implicits._

    //todo 查询hive,将查询加过封装为rdd
    sparkSession.sql("use " + ConfigurationUtil.getValueFromConfig("hive.database"))

    val sql = new StringBuilder("select * from user_visit_action where 1 = 1 ")

    //添加条件startDtae
    val startDate = ConfigurationUtil.getValueFromCondition("startDate")
    if (ConfigurationUtil.isNotEmpty(startDate)) {
      sql.append("and action_time  >= '").append(startDate).append("'")
    }

    //添加条件endDtae
    val endtDate = ConfigurationUtil.getValueFromCondition("endDate")
    if (ConfigurationUtil.isNotEmpty(endtDate)) {
      sql.append("and action_time  <= '").append(endtDate).append("'")
    }

    //执行查询
    val activeRDD: RDD[UserVisitAction] = sparkSession.sql(sql.toString()).as[UserVisitAction].rdd


    // todo 获取分母
    val pageIdsStr: String = ConfigurationUtil.getValueFromCondition("targetPageFlow")
    val pageIdsArray: Array[String] = pageIdsStr.split(",")

    //过滤得到包含 pageFlow 的rdd
    val filterActiveRDD: RDD[UserVisitAction] = activeRDD.filter(active => {

      val pageID: String = active.page_id + ""

      if (pageIdsArray.contains(pageID)) {
        true
      } else {
        false
      }
    })

    //map => RDD(pageid,1)
    val pageIdToOne: RDD[(Long, Int)] = filterActiveRDD.map(active => {
      (active.page_id, 1)
    })

    // reducebykey => RDD(pageid,count)
    val PageIdToCount: RDD[(Long, Int)] = pageIdToOne.reduceByKey(_ + _)

    // collect => Array(pageid,count).toMap(pageid -> count)
    val pageidToCountMap: Map[Long, Int] = PageIdToCount.collect().toMap

    //println(pageidToCountMap.mkString(","))

    // todo 获取分子 (1-2,count)
    // map => sessionid , cid , date
    val mapRDD: RDD[(String, (Long, String))] = activeRDD.map(active => {
      (active.session_id, (active.page_id, active.action_time))
    })
    // group by all sessionid => sessionid, itre(cid, date)
    // sort itre => sessionid, itre(cid)
    val groupBySessionRDD: RDD[(String, Iterable[(Long, String)])] = mapRDD.groupByKey()

    val sessionidPageidsRDD: RDD[(String, List[Long])] = groupBySessionRDD.mapValues(data => {
      val list: List[(Long, String)] = data.toList.sortWith {
        case (left, right) => {
          left._2 < right._2
        }
      }

      list.map(data => data._1)
    })

    // map => itre(cid)
    val pageidsRDD: RDD[List[Long]] = sessionidPageidsRDD.map(data => data._2)

    // 过滤
    // zip => (1-2)
    val needZipArray: Array[(String, String)] = pageIdsArray.zip(pageIdsArray.tail)

    val userZipRDD: RDD[List[(String, String)]] = pageidsRDD.map(list => {
      val zipList: List[(Long, Long)] = list.zip(list.tail)
      zipList.map {
        case (left, right) => (left.toString, right.toString)
      }
    })
    val userZipRDD2: RDD[(String, String)] = userZipRDD.flatMap(x => x)
    val userZipFilterRDD: RDD[(String, String)] = userZipRDD2.filter(data => needZipArray.contains(data))

    // map => (1-2),1
    val pageFlowOne: RDD[(String, Int)] = userZipFilterRDD.map {
      case (left, right) => (left + "-" + right, 1)
    }

    // reduce => (1-2), count
    val pageFlowCount: RDD[(String, Int)] = pageFlowOne.reduceByKey(_ + _)
    println(pageFlowCount.count())
    pageFlowCount.foreach(println(_))

    // 分子/分母

    val resultRDD: RDD[(String, Int, Int, Double)] = pageFlowCount.map {
      case (pageFlow, count) => {

        val basecount: Int = pageidToCountMap.get(pageFlow.split("-")(0).toLong).get

        (pageFlow, count, basecount, count / basecount.toDouble)
      }
    }

    //写入数据库
    val url = ConfigurationUtil.getValueFromConfig("jdbc.url")
    val user = ConfigurationUtil.getValueFromConfig("jdbc.user")
    val password = ConfigurationUtil.getValueFromConfig("jdbc.password")

    resultRDD.foreachPartition { data => {

      Class.forName(ConfigurationUtil.getValueFromConfig("jdbc.driver.class"))
      val conn: Connection = DriverManager.getConnection(url, user, password)
      val ps: PreparedStatement = conn.prepareStatement("insert into category_top10 values (?,?,?,?,?)")

      data.foreach {
        case (pageFlow, count, basecount, rate) => {

          ps.setObject(1, pageFlow)
          ps.setObject(2, count)
          ps.setObject(3, basecount)
          ps.setObject(4, rate)

          ps.execute()
        }
      }
      ps.close()
      conn.close()
    }
    }

    sparkSession.stop()
  }
}

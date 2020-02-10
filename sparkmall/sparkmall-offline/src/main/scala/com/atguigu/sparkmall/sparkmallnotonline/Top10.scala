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
    val rdd: RDD[UserVisitAction] = sparkSession.sql(sql.toString()).as[UserVisitAction].rdd


    //todo 使用累加器,达到reducebykey的效果, 累加器的数据结构是HashMap[String, Long]
    val accumulator = new CategoryCountAccumulator
    sparkSession.sparkContext.register(accumulator)

    //遍历rdd, 往累加器中添加数据
    rdd.foreach {
      case userVisitAvtion => {

        if (userVisitAvtion.click_category_id != -1) {
          accumulator.add(userVisitAvtion.click_category_id.toString + "_click")
        } else if (userVisitAvtion.order_category_ids != null) {

          val categoryIds: Array[String] = userVisitAvtion.order_category_ids.split(",")
          for (categoryId <- categoryIds) {
            accumulator.add(categoryId + "_order")
          }
        } else if (userVisitAvtion.pay_category_ids != null) {

          val payIds: Array[String] = userVisitAvtion.pay_category_ids.split(",")
          for (payId <- payIds) {
            accumulator.add(payId + "_pay")
          }
        } else {}
      }
    }

    //打印 : Map(10_click -> 100,10_order -> 50 , ...)
    //println(accumulator.value.take(10))


    //todo 将累加器中的值分组合并
    val categoryIDtoActiveMap: mutable.HashMap[String, Long] = accumulator.value

    //分组之前: Map(1_click -> 5909,1_order -> 1000 , ...)
    //分组之后: 1 -> Map(1_click -> 5909,1_order -> 1000, 1_pay -> 3974)
    val caterotyid2activeCountmap: Map[String, mutable.HashMap[String, Long]] = categoryIDtoActiveMap.groupBy {
      case (categoryIDActive, count) => {
        categoryIDActive.split("_")(0)
      }
    }
    //println(caterotyid2activeCountmap.take(100))

    //todo 转化为样例类
    val taskID: String = UUID.randomUUID().toString
    val categoryTopIter: immutable.Iterable[CategoryTop10] = caterotyid2activeCountmap.map {
      case (categoryID, map) => {
        CategoryTop10(
          taskID,
          categoryID,
          map.getOrElse(categoryID + "_click", 0),
          map.getOrElse(categoryID + "_order", 0),
          map.getOrElse(categoryID + "_pay", 0)
        )
      }
    }
    println(categoryTopIter.take(10))

    //todo 排序, 取前十
    val sortList: List[CategoryTop10] = categoryTopIter.toList.sortWith {

      case (left, right) => {

        if (left.clickCount > right.clickCount) {
          true
        } else if (left.clickCount == right.clickCount) {
          if (left.orderCount > right.orderCount) {
            true
          } else if (left.orderCount == right.orderCount) {
            left.payCount > right.payCount
          } else {
            false
          }
        } else {
          false
        }
      }
    }
    sortList.take(10)

    //写入数据库

    val url = ConfigurationUtil.getValueFromConfig("jdbc.url")
    val user = ConfigurationUtil.getValueFromConfig("jdbc.user")
    val password = ConfigurationUtil.getValueFromConfig("jdbc.password")

    Class.forName(ConfigurationUtil.getValueFromConfig("jdbc.driver.class"))
    val conn: Connection = DriverManager.getConnection(url, user, password)

    val ps: PreparedStatement = conn.prepareStatement("insert into category_top10 values (?,?,?,?,?)")

    sortList.foreach {
      categoryTop10 => {
        ps.setObject(1, categoryTop10.taskid)
        ps.setObject(2, categoryTop10.categoryId)
        ps.setObject(3, categoryTop10.clickCount)
        ps.setObject(4, categoryTop10.orderCount)
        ps.setObject(5, categoryTop10.payCount)

        ps.execute()
      }
    }

    ps.close()
    conn.close()
    sparkSession.stop()
  }


}

class CategoryCountAccumulator extends AccumulatorV2[String, mutable.HashMap[String, Long]] {

  var map = new mutable.HashMap[String, Long]

  override def isZero: Boolean = map.isEmpty

  override def copy(): AccumulatorV2[String, mutable.HashMap[String, Long]] = new CategoryCountAccumulator

  override def reset(): Unit = map.clear()

  override def add(key: String): Unit = {

    //("lisi"->1) =>("lisi" -> 2)
    // 2=2
    map(key) = map.getOrElse(key, 0L) + 1
  }

  override def merge(other: AccumulatorV2[String, mutable.HashMap[String, Long]]): Unit = {

    //合并之后需要返回
    map = map.foldLeft(other.value) {
      case (bufferMap, (categoryID, count)) => {
        bufferMap(categoryID) = bufferMap.getOrElse(categoryID, 0L) + count
        bufferMap
      }
    }
  }

  override def value: mutable.HashMap[String, Long] = map
}

case class CategoryTop10(taskid: String, categoryId: String, clickCount: Long, orderCount: Long, payCount: Long)

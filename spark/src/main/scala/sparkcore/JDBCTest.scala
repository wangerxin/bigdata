package sparkcore

import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.rdd.{JdbcRDD, RDD}
import org.apache.spark.{SparkConf, SparkContext}

object JDBCTest {

  def main(args: Array[String]): Unit = {

    //初始化sparkContext
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("jdbc")
    val sc = new SparkContext(conf)

    //连接数据库参数
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost:3306/anli"
    val name = "root"
    val password = "235236"

    //---------------读取mysql数据-----------------------
    val result = new JdbcRDD(
      sc,
      () => {
        Class.forName(driver);
        DriverManager.getConnection(url, name, password)
      },
      "select * from user where id < ? and id <? ;",
      5,
      10,
      1,
      resultSet => {
        val id: Int = resultSet.getInt("id")
        val name: String = resultSet.getString("name")
        (id, name)
      }
    )

    //处理数据
    result.foreach(println(_))
     sc.stop()

    //-----------------写入到mysql-------------------------
    val rdd: RDD[(String, String)] = sc.makeRDD(Array(( "张三", "男"), ( "李四", "男")))

    rdd.foreachPartition {

      iter => {

        //连接数据库
        Class.forName(driver)
        val conn: Connection = DriverManager.getConnection(url, name, password)

        //写入数据
        iter.foreach { tunlp =>
          val ps: PreparedStatement = conn.prepareStatement("insert into user(name,sex) values (?,?)")
          ps.setString(1, tunlp._1)
          ps.setString(2, tunlp._2)
          ps.executeUpdate()
        }
        conn.close()
      }
    }
  }
}
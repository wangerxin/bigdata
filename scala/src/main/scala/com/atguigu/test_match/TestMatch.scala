package com.atguigu.test_match

object TestMatch {

  def main(args: Array[String]): Unit = {

    val name = "zhangsan"

    name match {

      case name2 => println("name:"+name+"  name2:"+name2)
      case "zhangsan" => println(name)
      case "lisi" => println(name)
      case _ => println("无法匹配")
    }

    val list: List[(String, Int)] = List("hello word", "hello atguigu").flatMap(_.split(" ")).groupBy(x => x).map(t => (t._1, t._2.length)).toList.sortWith(
      { case ((k1, v1), (k2, v2)) => v1 > v2 }
    )
    val str: String = list.mkString(",")

    println(str)
  }

}

package com.atguigu.chapter5

import java.util

object ImplictTest {

  class AA{

    def AAF={
      println("AAF...")
    }
  }

  implicit class BB(aa : Int){

    def bbF={
      println("BB的bbF。。。")
    }
  }

  def main(args: Array[String]): Unit = {

    val aa = new AA
    aa.AAF

    val array = Array(1, 2, 3)
    val clazz: Class[_ <: Array[Int]] = array.getClass
    val unit: Class[Array[Int]] = classOf[Array[Int]]





  }

}

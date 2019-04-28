package com.atguigu.chapter4

import scala.collection.immutable.HashMap
import scala.collection.mutable.ArrayBuffer

object TestOption {

  def main(args: Array[String]): Unit = {

    //option封装了一个
    val maybeInt: Option[Int] = Option(100)
    val someInt: Some[Array[Int]] = Some(Array(1,2,3))

    //从option中取值
    //println(maybeInt.getOrElse(0))

    //
    val hashMap: HashMap[String, String] = HashMap[String,String]("1" -> "a","2" -> null)
    val option: Option[String] = hashMap.get("2")
    //print(option.get)

    //数组
    val arr1 = Array(1,2,3)
    print(arr1.hashCode())
    arr1(0) = 2
    print(arr1.hashCode())

    val buffer = ArrayBuffer(1,2,3)




  }

}

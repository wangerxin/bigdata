package com.atguigu.chapter2

import scala.collection.immutable
import scala.util.control.Breaks

object HomeWork {

  def main(args: Array[String]): Unit = {


    val index: immutable.IndexedSeq[Int] = for ( i <- 1 to 10) yield i
    //print(index)

    val for5 = for(i <- Range(1,9)) yield i
    //println(for5)

    //4.倒序打印数字
    def reversePrint(n : Int): Unit ={
      for (i <- (0 to n).reverse){
        //print(i)
      }
    }

    //控制台输入
    var str : String = "123"
    str.toInt

    //5
    var sum : Long = 1
    for ( i <- "Hello".toCharArray){
      val unicode: Long = i.toLong
      sum *= unicode
    }
    //println(sum)

    //6
    var sum2 : Long= 1
    val result: Unit = "Hello".foreach(sum2 *= _.toLong)
   // println(sum2)

    //7.编写一个函数product(s:String)，计算字符串中所有字母的Unicode代码（toLong方法）的乘积
    def produce( s : String ) ={
      var sum : Long = 1
      s.foreach( str => sum *= str.toLong)
      println(sum)
    }
    produce("Hello")







  }
}

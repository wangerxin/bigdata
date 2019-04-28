package com.atguigu.chapter4

import scala.collection.immutable.StringLike
import scala.reflect.internal.util.StringOps

/**
  * 隐式转换：如果类型A可以隐式转换为类型B，那么类型A可以使用B的方法
  * 隐式转换函数：scala源码中有隐式转换，程序员可以自定义隐式转换
  */
object TestImplict {

  class AAA{
    def aaa(): Unit ={
      print("aaa")
    }
  }

  class BBB{
    def bbb(): Unit ={
      print("bbb")
    }
  }

  def main(args: Array[String]): Unit = {

    implicit def aaaTobbb(a : AAA) : BBB = {
      new BBB()
    }

    val aaa = new AAA()
    aaa.aaa()
    aaa.bbb()


  }

}

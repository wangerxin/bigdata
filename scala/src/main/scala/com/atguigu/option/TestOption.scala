package com.atguigu.option

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

object TestOption {

  def main(args: Array[String]): Unit = {


    val list = ListBuffer(1, 2, 3)

    list.foldLeft(10)(_-_)

    var arrarBuffer: ArrayBuffer[Int] = ArrayBuffer(1,2,3,4)

    var ints: ArrayBuffer[Int] = arrarBuffer :+ 5

    println(arrarBuffer==ints)


  }
}


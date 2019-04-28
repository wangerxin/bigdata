package com.atguigu.chapter5

object Test {
  def main(args: Array[String]): Unit = {

    //val aa = new AA

    //抽象类是可以new的
    // val cc: CC = new CC {}

    //动态混入的时候需要实现抽象方法
    //val dd = new DD with AA with BB


  }
}

//特质
trait AA {

  var name: String = _
  var age: Int = _
  println("AA")

  //def study

  def work() = {
    print("work..")
  }
}

trait BB {
  println("BB")
}

//抽象类
abstract class CC {
  print("抽象类")
}

//普通类
class DD {
  println("DD")
}



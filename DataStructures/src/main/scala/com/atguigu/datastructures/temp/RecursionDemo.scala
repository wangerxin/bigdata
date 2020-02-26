package com.atguigu.datastructures.temp

object RecursionDemo {
  def main(args: Array[String]): Unit = {
    //test(4)
    println(factorial(5))
  }

  def test(n: Int): Unit = {
    if (n > 2) {
      test(n -1)
    } else {
      println("n=" + n)
    }
  }

  def factorial(n: Int): Int = {
    if (n == 1) {
      1
    } else {
      factorial(n - 1) * n
    }
  }


}

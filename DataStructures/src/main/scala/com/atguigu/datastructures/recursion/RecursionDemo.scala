package com.atguigu.datastructures.recursion

object RecursionDemo {
  def main(args: Array[String]): Unit = {
    test(4) //输出什么?  2 , 3, 4
  }
  def test(n: Int): Unit = {
    if (n > 2) {
      test(n - 1)
    }
    println("n=" + n)
  }
}

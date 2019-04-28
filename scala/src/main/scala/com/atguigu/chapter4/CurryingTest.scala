package com.atguigu.chapter4

object CurryingTest {

  def main(args: Array[String]): Unit = {

    //sum(1)(2)(f)
    def sum(a: Int) = {

      def middle(b: Int) = {

        def inner(f: (Int, Int) => Int) = {
          print("inner")
          f(a, b)
        }

        inner _
      }

      middle _
    }

    val result: Int = sum(2)(5)((x, y) => {
      x + y
    })
    println(result)


  }

}

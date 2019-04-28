package com.atguigu.chapter1


class People {
  var name = ""
}

object HomeWork {

  def main(args: Array[String]): Unit = {

    val str : String  = "zhang"
    val str2: String = str * 2
    str2.length
    print(str2)

    var people1 = new People
    people1.name = "zhansan"
    people1 = null

    val people2 = new People
    people2.name="lisi"






  }

}

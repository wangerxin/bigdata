package com.atguigu.array_bufferArray

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object TestArray {
  def main(args: Array[String]): Unit = {
    var arr = ArrayBuffer[String]("zhangsan","lisi")

    import scala.collection.JavaConversions.bufferAsJavaList

    var javaArr = new ProcessBuilder(arr)

    var list = javaArr.command()

    val queue = new mutable.Queue[Any]

    //map
    //创建
    /*val ageMap = Map("age1"->18,"age2"->20,"age3"->22)
    println(ageMap)
    val age1 = ageMap("age1");println(age1) //18
    val age2 = ageMap.get("age2");println(age2) //some(20)
    val age3 = ageMap.get("age3").get;println(age3) //22
    val mutableMap = mutable.Map[String,Int]()
    println(mutableMap)*/

    //map高阶函数
    //单参数高阶函数
    /*val list1 = List(1,2,3)
    val list2 = list1.map(_*2)
    println(list2)*/

    //双参数高阶函数
    val list3 = List(1,2,3,4,5)
    val list4 = list3.reduce(_+_)
    val list5 = list3.map(_+2) ;
    println(list5)





  }

}

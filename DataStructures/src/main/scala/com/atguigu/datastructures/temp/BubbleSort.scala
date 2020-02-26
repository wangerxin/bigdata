package com.atguigu.datastructures.temp

import java.text.SimpleDateFormat
import java.util.Date


object BubbleSort {
  def main(args: Array[String]): Unit = {

    //val arr = Array(3, 9, -1, 10, 2)

    //创建一个80000个随机数据的数组
    val random = new util.Random()
    val arr = new Array[Int](80)
    for (i <- 0 until 80) {
      arr(i) = random.nextInt(8000000)
    }
    println("排序前")
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)
    println("排序前时间=" + date) //输出时间


    bubbleSort(arr)

    val now2: Date = new Date()

    val date2 = dateFormat.format(now2)
    println("排序前时间=" + date2) //输出时间
    //println("排序后" + arr.mkString(","))


  }

  def bubbleSort(arr: Array[Int]): Unit = {


    var temp = 0
    for (i <- 0 until arr.length - 1) {
      for (j <- 0 until arr.length - 1 - i) {
        if (arr(j) > arr(j + 1)) {
          temp = arr(j)
          arr(j) = arr(j + 1)
          arr(j + 1) = temp
        }
      }
      //println("第" + (i + 1) + "轮=" + arr.mkString(","))
    }

    /*

    //第1轮
    var temp = 0
    for(j <- 0 until arr.length - 1) {
      if(arr(j) > arr(j+1)) {
        temp = arr(j)
        arr(j) = arr(j+1)
        arr(j+1) = temp
      }
    }
    println("第1轮="+arr.mkString(","))

    for(j <- 0 until arr.length - 1 - 1 ) {
      if(arr(j) > arr(j+1)) {
        temp = arr(j)
        arr(j) = arr(j+1)
        arr(j+1) = temp
      }
    }
    println("第2轮="+arr.mkString(","))

    for(j <- 0 until arr.length - 1 - 2 ) {
      if(arr(j) > arr(j+1)) {
        temp = arr(j)
        arr(j) = arr(j+1)
        arr(j+1) = temp
      }
    }
    println("第3轮="+arr.mkString(","))

    for(j <- 0 until arr.length - 1 - 3 ) {
      if(arr(j) > arr(j+1)) {
        temp = arr(j)
        arr(j) = arr(j+1)
        arr(j+1) = temp
      }
    }
    println("第4轮="+arr.mkString(",")) */
  }

}

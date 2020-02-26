package com.atguigu.datastructures.temp

import java.text.SimpleDateFormat
import java.util.Date
import util.control.Breaks._
object InsertSort {
  def main(args: Array[String]): Unit = {
    println("快速排序~")
   // val arr = Array(101, 34, 119, 1)
   //创建一个80000个随机数据的数组
   val random = new util.Random()
    val arr = new Array[Int](8000000)
    for (i <- 0 until 8000000) {
      arr(i) = random.nextInt(8000000)
    }
    println("排序前")
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)
    println("排序前时间=" + date) //输出时间
    quickSort(0,arr.length-1,arr)

    val now2: Date = new Date()

    val date2 = dateFormat.format(now2)
    println("排序前时间=" + date2) //输出时间
   // println("arr=" + arr.mkString(","))
  }

  def insertSort(arr: Array[Int]): Unit = {


    //1次大的循环

    for (i <- 1 until arr.length) {
      val insertVal = arr(i)
      var insertIndex = i - 1
      while (insertIndex >= 0 && insertVal < arr(insertIndex)) {
        arr(insertIndex + 1) = arr(insertIndex)
        insertIndex -= 1
      }
      arr(insertIndex + 1) = insertVal
     // println("第" + i + "轮" + arr.mkString(","))
    }


  }

  def quickSort(left: Int, right: Int, arr: Array[Int]): Unit = {
    var l = left
    var r = right
    var pivot = arr((left + right) / 2)
    var temp = 0
    breakable {
      while (l < r) {
        while (arr(l) < pivot) {
          l += 1 }
        while (arr(r) > pivot) {
          r -= 1}
        if (l >= r) {
          break()
        }
        var temp = arr(l)
        arr(l) = arr(r)
        arr(r) = temp
        if (arr(l) == pivot) {
          r -= 1
        }
        if ((arr(r)) == pivot) {
          l += 1
        }}}
    if (l == r) {
      l += 1
      r -= 1
    }
    if (left < r) {
      quickSort(left, r, arr)
    }
    if (right > l) {
      quickSort(l, right, arr)
    }
  }

}

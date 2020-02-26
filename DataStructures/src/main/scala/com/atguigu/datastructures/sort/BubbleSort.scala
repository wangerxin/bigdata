package com.atguigu.datastructures.sort

import java.text.SimpleDateFormat
import java.util.Date
import util.control.Breaks._

object BubbleSort {
  def main(args: Array[String]): Unit = {
    //o(n^2)
    //val arr = Array(3, 9, -1, 10, 2) // 80000
    //创建一个80000个随机数据的数组
    val random = new util.Random()
    val arr = new Array[Int](80000)
    for (i <- 0 until 80000) {
      arr(i) = random.nextInt(8000000) //范围[0,8000000)
    }
    println("排序前")
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)
    println("排序前时间=" + date) //输出时间

    //测试一下时间

    bubbleSort(arr)

    val now2: Date = new Date()
    val date2 = dateFormat.format(now2)
    println("排序前时间=" + date2) //输出时间


  }

  def bubbleSort(arr: Array[Int]): Unit = {

    //因为1,2,3,4， 轮几乎一样，我们使用for循环嵌套即
    //对冒泡优化
    //思路
    //1. 如果我们发现数组已经是一个有序的了，则可以提前退出排序
    //2. 定义一个标识符 flag ,如果在1轮排序，一次交换都没有发生，说明数组已经有序
    var temp = 0
    var flag = false

    breakable {
      for (i <- 0 until arr.length - 1) {
        for (j <- 0 until arr.length - 1 - i) {
          if (arr(j) > arr(j + 1)) {
            flag = true
            temp = arr(j)
            arr(j) = arr(j + 1)
            arr(j + 1) = temp
          }
        }
        if (!flag) {
          break()
        } else {
          flag = false //下一轮，重写设置false
        }
        //println("第"+(i+1)+"轮的排序" + arr.mkString(","))
      }
    }

    /*
    第1轮 =>         将最大数，移动数组的最后
  (1)  3, 9, -1, 10, 2
  (2)  3, -1,9,10,2
  (3)  3, -1,9,10,2
  (4) 3, -1,9,2,10

     */
    /*
    var temp = 0
    for (j <- 0 until arr.length - 1 - 0) {
      if (arr(j) > arr(j + 1)) {
        temp = arr(j)
        arr(j) = arr(j + 1)
        arr(j + 1) = temp
      }
    }
    println("第l轮的排序" + arr.mkString(","))

    //第二轮
    for (j <- 0 until arr.length - 1 - 1) {
      if (arr(j) > arr(j + 1)) {
        temp = arr(j)
        arr(j) = arr(j + 1)
        arr(j + 1) = temp
      }
    }
    println("第2轮的排序" + arr.mkString(","))

    //第3轮
    for (j <- 0 until arr.length - 1 - 2) {
      if (arr(j) > arr(j + 1)) {
        temp = arr(j)
        arr(j) = arr(j + 1)
        arr(j + 1) = temp
      }
    }
    println("第3轮的排序" + arr.mkString(","))

    //第4轮
    for (j <- 0 until arr.length - 1 - 3) {
      if (arr(j) > arr(j + 1)) {
        temp = arr(j)
        arr(j) = arr(j + 1)
        arr(j + 1) = temp
      }
    }
    println("第4轮的排序" + arr.mkString(","))*/

  }
}

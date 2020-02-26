package com.atguigu.datastructures.sort

import java.text.SimpleDateFormat
import java.util._


object InsertSort {
  def main(args: Array[String]): Unit = {

    val arr = Array(101, 23,34, 119, 1)
    insertSort2(arr)
    println(arr.mkString(","))


    /* ------------时间复杂度测试
    val random = new java.util.Random()
    val arr = new Array[Int](800000)
    for (i <- 0 until 800000) {
      arr(i) = random.nextInt(8000000) //范围[0,8000000)
    }
    println("排序前")
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)
    println("排序前时间=" + date) //输出时间
    println("插入排序")

    insertSort(arr) //插入排序

    val now2: Date = new Date()
    val date2 = dateFormat.format(now2)
    println("排序前时间=" + date2) //输出时间
    */
  }

  /**
    * 插入排序
    * 思想: 1.数组左边第一个元素视为有序序列
    *       2. 右边的数据, 依次插入到左边有序序列中, 使得左边依然有序
    *       3. 这是老师的思想, 下面还有
    * @param arr
    */
  def insertSort(arr: Array[Int]): Unit = {


    var insertVal = 0 //待插入的值
    var index = 0 //左边有序序列的索引

    //从索引为1开始的数据, 插入到有序序列中
    for (i <- 1 until arr.length) {

      insertVal = arr(i) //保存待插入的数
      index = i - 1      //有序序列的索引

      //如果index >=0 比较 insertVal 和  arr(index)关系
      //insertVal < arr(index) 满足，说明还没有找到位置
      while (index >= 0 && (insertVal < arr(index))) {
        //让arr(index) 后移
        arr(index + 1) = arr(index)
        index -= 1
      }
      arr(index + 1) = insertVal
      //println("arr 第"+i+"轮" + arr.mkString(","))
    }
  }

  def insertSort2(arr: Array[Int]): Unit = {

    //变量
    var insertVal = 0
    var index = 0

    //从索引为1的数据开始, 遍历插入左边的序列
    for (i <- 1 until arr.length) {

      //保存插入值
      insertVal = arr(i)
      //左移指针
      index = i - 1

      //将将插入值和指针值比较,找到插入位置,退出循环说明找到了
      while (index >= 0 && insertVal < arr(index)) {

        //数据后移
        arr(index + 1) = arr(index)
        //指针前移
        index -= 1
      }

      //找到了,插入
      arr(index + 1) = insertVal
    }
  }


  //完成第1轮的排序(给 34)找位置
  //思路
  //1. 先 34 保存到一个变量
  //2  用一个变量保存 34的元素的前一个下标

  /*
  var insertVal = arr(1)
  var index = 1 - 1
  //说明
  //如果index >=0 比较 insertVal 和  arr(index)关系
  //insertVal < arr(index) 满足，说明还没有找到位置
  while (index >= 0 && (insertVal < arr(index))) {
    //让arr(index) 后移
    arr(index+1) = arr(index)
    index -= 1
  }
  arr(index+1) = insertVal
  println("arr 第1轮" + arr.mkString(","))

  //第2轮
  insertVal = arr(2)
  index = 2 - 1
  while (index >= 0 && (insertVal < arr(index))) {
    //让arr(index) 后移
    arr(index+1) = arr(index)
    index -= 1
  }
  arr(index+1) = insertVal
  println("arr 第2轮" + arr.mkString(","))

  //第3轮
  insertVal = arr(3)
  index = 3 - 1
  while (index >= 0 && (insertVal < arr(index))) {
    //让arr(index) 后移
    arr(index+1) = arr(index)
    index -= 1
  }
  arr(index+1) = insertVal
  println("arr 第3轮" + arr.mkString(","))*/

}

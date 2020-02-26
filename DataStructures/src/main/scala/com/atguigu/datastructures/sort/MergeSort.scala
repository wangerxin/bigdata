package com.atguigu.datastructures.sort

import java.text.SimpleDateFormat
import java.util.Date

object MergeSort {
  def main(args: Array[String]): Unit = {
    //val arr = Array(8, 4, 5, 7, 1, 3, 6, 2,-1)

    val random = new util.Random()
    val arr = new Array[Int](8000000)
    for (i <- 0 until 8000000) {
      arr(i) = random.nextInt(8000000) //范围[0,8000000)
    }
    println("排序前")
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)
    println("排序前时间=" + date) //输出时间
    println("归并排序")

    val temp = new Array[Int](arr.length)
    mergeSort(arr,0,arr.length-1,temp)

    val now2: Date = new Date()
    val date2 = dateFormat.format(now2)
    println("排序前时间=" + date2) //输出时间

    //println("排序后"+arr.mkString(","))

  }

  def mergeSort(arr: Array[Int],left:Int,right :Int,temp:Array[Int]): Unit = {
    if(left<right){
      val mid = (left+right)/2 //找到中间值
      mergeSort(arr,left,mid,temp) //向左递归，进行分
      mergeSort(arr,mid+1,right,temp) //向右递归进行分
      merge(arr,left,mid,right,temp) //合并
    }
  }


  /**
    *
    * @param arr 排序的原始数组
    * @param left 左边的有序数列初始索引
    * @param mid  //中间初始索引
    * @param right //右边的索引
    * @param temp  //做中转的数组
    */
  def merge(arr: Array[Int], left: Int, mid: Int, right: Int, temp: Array[Int]) {
    var i = left //初始化i
    var j = mid + 1 //初始j
    var t = 0 // 指向temp数组的当前索引

    //将左右两边有序数列安，从小到大的顺序拷贝到temp数组中
    while (i <= mid && j <= right) {
      //如果左边的有序数列的当前元素，小于右边的有序数列的当前元素
      //则将arr(i) 拷贝temp
      if (arr(i) <= arr(j)) {
        temp(t) = arr(i)
        t += 1
        i += 1
      } else { //将 arr(j) 拷贝到temp
        temp(t) = arr(j)
        t += 1
        j += 1
      }
    }

    //如果左边的有序数列有剩余，的依次拷贝到temp
    while (i <= mid) {
      temp(t) = arr(i)
      t += 1
      i += 1
    }
    //如果右边的有序数列有剩余，的依次拷贝到temp
    while (j <= right) {
      temp(t) = arr(j)
      t += 1
      j += 1
    }
    t = 0
    var tempLeft = left
    //是把temp 数组的元素拷贝到 arr
    while (tempLeft <= right) {
      arr(tempLeft) = temp(t)
      t += 1
      tempLeft += 1
    }
  }

}

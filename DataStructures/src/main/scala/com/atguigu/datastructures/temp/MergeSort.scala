package com.atguigu.datastructures.temp

object MergeSort {
  def main(args: Array[String]): Unit = {
    var arr = Array(8,4,5,7,1,3,6,2,8,0,-1);
    val temp = new Array[Int](arr.length) //临时的数组
    mergeSort(arr,0,arr.length-1,temp)
    println("arr" + arr.mkString(","))

  }

  def mergeSort(arr: Array[Int], left: Int, right: Int, temp: Array[Int]): Unit = {
    if (left < right) {
      val mid = (left + right) / 2
      mergeSort(arr, left, mid, temp)
      mergeSort(arr, mid + 1, right, temp)
      merge(arr, left, mid, right, temp)
    }
  }

  def merge(arr: Array[Int], left: Int, mid: Int, right: Int, temp: Array[Int]) {
    println("xx")
    var i = left
    var j = mid + 1
    var t = 0
    while (i <= mid && j <= right) {
      if (arr(i) <= arr(j)) {
        temp(t) = arr(i)
        t += 1
        i += 1
      } else {
        temp(t) = arr(j)
        t += 1
        j += 1
      }
    }
    while (i <= mid) {
      temp(t) = arr(i)
      t += 1
      i += 1
    }
    while (j <= right) {
      temp(t) = arr(j)
      t += 1
      j += 1
    }
    t = 0
    var tempLeft = left
    while (tempLeft <= right) {
      arr(tempLeft) = temp(t)
      t += 1
      tempLeft += 1
    }
  }

}

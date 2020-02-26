package com.atguigu.datastructures.temp

import scala.collection.mutable.ArrayBuffer
import util.control.Breaks._
object BinarySearch {
  def main(args: Array[String]): Unit = {
    val arr = Array(1,2,390, 999, 999,999,999,999, 999,999,999,1110)
//    val resIndex = binarySearch(arr,0,arr.length-1,999)
//    println("resIndex=" + resIndex)
    val arrayBuffer = binarySearch2(arr,0,arr.length-1,999)
    if(arrayBuffer.length == 0) {
      println("没有")
    }else {
      println(arrayBuffer)
    }
  }
  def binarySearch(arr:Array[Int], l:Int, r:Int, value:Int): Int = {
    if(l > r) {

      return -1
    }

    val mid = (l + r) / 2
    val midVal = arr(mid)
    if(value > midVal) { //右
      binarySearch(arr,mid+1,r,value)
    } else if(value < midVal) { //左
      binarySearch(arr,l,mid-1,value)
    } else  {
      return mid
    }
  }


  def binarySearch2(arr:Array[Int], l:Int, r:Int, value:Int): ArrayBuffer[Int] = {
    if(l > r) {

      return ArrayBuffer[Int]()
    }

    val mid = (l + r) / 2
    val midVal = arr(mid)
    if(value > midVal) { //右
      binarySearch2(arr,mid+1,r,value)
    } else if(value < midVal) { //左
      binarySearch2(arr,l,mid-1,value)
    } else  {
      //
      var res = ArrayBuffer[Int]()

      var temp = mid - 1
      //左
      breakable {
        while (true) {
          if (temp < 0 || arr(temp) != value) {
            break()
          }
          res.append(temp)
          temp -= 1
        }
      }
      //遍历
      println("mid=" + mid)
      res.append(mid)
      temp = mid+1
      breakable {
        while (true) {
          if (temp > arr.length - 1 || arr(temp) != value) {
            break()
          }
          res.append(temp)
          temp += 1
        }
      }

      return res

    }
  }

}

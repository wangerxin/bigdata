package com.atguigu.datastructures.search

import scala.collection.mutable.ArrayBuffer
import util.control.Breaks._

object BinarySearch {
  def main(args: Array[String]): Unit = {

    val arr = Array(1, 8, 10, 89, 1000, 1000, 1000,1000,1000)
    //binarySearch(arr,0,arr.length -1, 1000)
    val resIndexBuffer = binarySearch2(arr, 0, arr.length - 1, 1000)
    println("结果是=" + resIndexBuffer)

    //要求：
    /*
    课后思考题1： {1,8, 10, 89, 1000, 1000，1234}
    当一个有序数组中，有多个相同的数值时，如何将所有的数值对应下标都查找到，比如这里的 1000.

     */


  }

  //使用二分查找，需要将所有数都找到
  def binarySearch2(arr: Array[Int], leftIndex: Int, rightIndex: Int, findVal: Int): ArrayBuffer[Int] = {
    //判断什么时候是找不到
    if (rightIndex < leftIndex) {
      return ArrayBuffer() //如果没有找到，就返回ArrayBuffer大小=0
    }


    //得到中间的mid
    val midIndex = (leftIndex + rightIndex) / 2
    val midVal = arr(midIndex)

    //比较
    if (midVal > findVal) { //向左递归查找
      return binarySearch2(arr, leftIndex, midIndex - 1, findVal)
    } else if (midVal < findVal) { //向右递归查找
      return binarySearch2(arr, midIndex + 1, rightIndex, findVal)
    } else {
      //思路，当我们找到midIndex时，需要在midIndex 左右两边扫描，将满足条件的下标
      //全部放入到ArrayBuffer
      val resIndexBuffer = new ArrayBuffer[Int]()
      //向左
      var temp = midIndex - 1
      breakable {
        while (true) {
          //如果 temp < 0 已经把左边全部扫描完毕
          // arr(temp) != findVal: 在向左扫描过程中，一旦发现有一个元素不等于 findVal，就退出
          if (temp < 0 || arr(temp) != findVal) {
            break()
          }
          resIndexBuffer.append(temp)
          temp -= 1
        }
      }
      resIndexBuffer.append(midIndex) //加入中间索引
      //向右
      temp = midIndex + 1
      breakable {
        while (true) {
          //如果 temp > arr.length - 1 已经把右边全部扫描完毕
          // arr(temp) != findVal: 在向左扫描过程中，一旦发现有一个元素不等于 findVal，就退出
          if (temp > arr.length - 1 || arr(temp) != findVal) {
            break()
          }
          resIndexBuffer.append(temp)
          temp += 1
        }
      }
      return resIndexBuffer

    }
  }

  //使用递归的方式，编写二分查找
  /**
    *
    * @param arr        待查找的数组
    * @param leftIndex  数组左边的索引
    * @param rightIndex 数组右边的索引
    * @param findVal    你要查找的数
    */
  def binarySearch(arr: Array[Int], leftIndex: Int, rightIndex: Int, findVal: Int): Unit = {

    //判断什么时候是找不到
    if (rightIndex < leftIndex) {
      println("找不到")
      return
    }


    //得到中间的mid
    val midIndex = (leftIndex + rightIndex) / 2
    val midVal = arr(midIndex)

    //比较
    if (midVal > findVal) { //向左递归查找
      binarySearch(arr, leftIndex, midIndex - 1, findVal)
    } else if (midVal < findVal) { //向右递归查找
      binarySearch(arr, midIndex + 1, rightIndex, findVal)
    } else {
      println("找到了 索引为=" + midIndex)


    }

  }
}

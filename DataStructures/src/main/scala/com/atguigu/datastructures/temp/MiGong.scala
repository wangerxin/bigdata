package com.atguigu.datastructures.temp

object MiGong {

  def main(args: Array[String]): Unit = {

    //地图
    val map = Array.ofDim[Int](8, 7)
    //上下全部置1
    for (i <- 0 until 7) {
      map(0)(i) = 1
      map(7)(i) = 1
    }
    //左右全部置1
    for (i <- 0 until 8) {
      map(i)(0) = 1
      map(i)(6) = 1
    }

    //设置挡板
    map(3)(1) = 1
    map(3)(2) = 1
    //map(1)(2) = 1
    //map(2)(2) = 1
    //打印地图
    for (i <- 0 until 8) {
      for (j <- 0 until 7) {
        print(map(i)(j) + " ")
      }
      println()
    }

    setWay(map,1,1)
    println("============")
    //打印地图
    for (i <- 0 until 8) {
      for (j <- 0 until 7) {
        print(map(i)(j) + " ")
      }
      println()
    }

  }

  def setWay(map: Array[Array[Int]], i:Int,j:Int): Boolean = {
    if(map(6)(5) == 2) {
      return true
    }else {
      if(map(i)(j) == 0) {
        //说明没有走
        map(i)(j) = 2
        if(setWay(map,i+1,j)) {
          return true
        }else if(setWay(map,i,j+1)) {
          return true
        }else if(setWay(map,i-1,j)) {
          return true
        }else if(setWay(map,i,j-1)) {
          return true
        }else {
          map(i)(j) = 3
          return false
        }
      }else {
        return false
      }
    }
  }

}

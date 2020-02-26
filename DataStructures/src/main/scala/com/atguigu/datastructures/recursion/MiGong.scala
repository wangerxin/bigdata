package com.atguigu.datastructures.recursion

object MiGong {
  def main(args: Array[String]): Unit = {
    //使用二维数组模拟地图 map[8][7]
    val map = Array.ofDim[Int](8, 7)
    //给map初始化，使用1 表示墙

    //上下行设置1
    for (i <- 0 until 7) {
      map(0)(i) = 1
      map(7)(i) = 1
    }

    //左右设置为1
    for (i <- 0 until 8) {
      map(i)(0) = 1
      map(i)(6) = 1
    }
    //设置墙
    map(3)(1) = 1
    map(3)(2) = 1

    //验证看看当前地图是否
    for (row <- map) {
      for (ele <- row) {
        print(ele + " ")
      }
      println()
    }
    println()

    //测试迷宫
    setWay2(map, 1, 1)

    //看看迷宫的情况
    println("小球找路的结果:")
    for (row <- map) {
      for (ele <- row) {
        print(ele + " ")
      }
      println()
    }
    println()
  }

  //编写方法完成探路
  /**
    *
    * @param map 地图
    * @param i   表示当前探测点横坐标
    * @param j   表示当前探测点纵坐标
    * @return
    */
  def setWay(map: Array[Array[Int]], i: Int, j: Int): Boolean = {
    //如果map[6][5] == 2
    if (map(6)(5) == 2) { //找到通路
      return true
    } else {
      //找在地图中，使用1 表示墙，0 表示没有走过, 2 表示通路，3  表示该点已经探测过，但是走不通，死路
      if(map(i)(j) == 0) {
        //假定map(i)(j) 可以走通，实际不一定
        map(i)(j) = 2
        //安我们的策略开始探测
        //先确定一个策略  下->右->上->左
        if(setWay(map, i+1,j)) {
          return  true
        } else if (setWay(map, i,j+1)){ //右
          return true
        } else if (setWay(map, i-1,j)){ //上
          return true
        } else if (setWay(map, i,j-1)){ //左
          return true
        } else {
          //说明你的假定不正确
          map(i)(j) = 3
          return false
        }
      } else { //map(i)(j) != 0, 则map(i)(j) 1 2 3
        return  false
      }
    }
  }

  def setWay2(map: Array[Array[Int]], i: Int, j: Int): Boolean = {
    //如果map[6][5] == 2
    if (map(6)(5) == 2) { //找到通路
      return true
    } else {
      //找在地图中，使用1 表示墙，0 表示没有走过, 2 表示通路，3  表示该点已经探测过，但是走不通，死路
      if(map(i)(j) == 0) {
        //假定map(i)(j) 可以走通，实际不一定
        map(i)(j) = 2
        //安我们的策略开始探测
        //先确定一个策略  上->右->下->左
        if(setWay2(map, i-1,j)) { //上
          return  true
        } else if (setWay2(map, i,j+1)){ //右
          return true
        } else if (setWay2(map, i+1,j)){ //下
          return true
        } else if (setWay2(map, i,j-1)){ //左
          return true
        } else {
          //说明你的假定不正确
          map(i)(j) = 3
          return false
        }
      } else { //map(i)(j) != 0, 则map(i)(j) 1 2 3
        return  false
      }
    }
  }
}



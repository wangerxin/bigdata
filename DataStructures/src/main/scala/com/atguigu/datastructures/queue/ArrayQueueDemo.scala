package com.atguigu.datastructures.queue

import scala.io.StdIn

object ArrayQueueDemo {
  def main(args: Array[String]): Unit = {
    //创建一个队列对象
    val queue = new ArrayQueue(3)
    var key = "" //接收用户的输入
    //简单写个菜单
    while (true) {
      println("show : 显示队列")
      println("add: 添加数据到队列")
      println("get: 从队列头取出元素")
      println("peek: 查看队列头元素")
      key = StdIn.readLine()
      key match {
        case "show" => queue.showQueue()
        case "add" =>
          println("请输入一个数吧")
          val value = StdIn.readInt()
          queue.addQueue(value)
        case "get" =>
          val res = queue.getQueue()//取出
          //判断res 的类型
          if(res.isInstanceOf[Exception]) {
            //输出异常信息
            println(res.asInstanceOf[Exception].getMessage)
          }else {
            printf("取出的队列头元素是%d", res)
          }
        case "peek" =>
          val res = queue.peek()//查看
          //判断res 的类型
          if(res.isInstanceOf[Exception]) {
            //输出异常信息
            println(res.asInstanceOf[Exception].getMessage)
          }else {
            printf("队列头元素是%d", res)
          }
      }
    }
  }
}

//定义一个类ArrayQueue 表队列
//该类会实现队列的相关方法
//数据结构 创建-遍历-测试-修改-删除
class ArrayQueue(arrMaxSize: Int) {
  val maxSize = arrMaxSize
  val arr = new Array[Int](maxSize) //队列的数据存放的数组
  var front = -1 // 表示指向队列的第一个元素的前一个位置
  var rear = -1 //表示指向队列的最后个元素

  //查看队列的头元素，但是不取出
  def peek(): Any = {
    if(isEmpty()) {
      return new Exception("队列空，没有数据返回");
    }
    return arr(front + 1)
  }

  //判断队列是否满
  def isFull(): Boolean = {
    rear == maxSize - 1
  }

  //判断队列是空
  def isEmpty(): Boolean = {
    rear == front
  }

  //从队列中取出数据
  //异常时可以加入业务逻辑
  def getQueue(): Any = {
    if(isEmpty()) {
      return new Exception("队列空，没有数据返回");
    }
    //将front 后移一位
    front += 1
    return arr(front)
  }

  //给队列添加数据
  def addQueue(num: Int): Unit = {
    if (isFull()) {
      println("队列满，不能加入!!")
      return
    }
    //添加时调整rear
    //1. 先将rear 后移
    rear += 1
    arr(rear) = num
  }

  //遍历队列
  def showQueue(): Unit = {
    if (isEmpty()) {
      println("队列空!!")
      return
    }

    for (i <- (front + 1) to rear) {
      printf("arr(%d)=%d\t", i, arr(i))
    }
    println()
  }

}

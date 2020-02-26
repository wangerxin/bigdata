package com.atguigu.datastructures.queue

import scala.io.StdIn
object CircleArrayQueueDemo {
  def main(args: Array[String]): Unit = {

    //创建一个队列对象
    val queue = new CircleArrayQueue(4)
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

//定义一个类CircleArrayQueue 表环形队列
//该类会实现队列的相关方法
//数据结构 创建-遍历-测试--删除
class CircleArrayQueue(arrMaxSize: Int) {
  val maxSize = arrMaxSize
  val arr = new Array[Int](maxSize) //队列的数据存放的数组
  var front = 0 // front 初始化 = 0 ， front 约定 指向队列的头元素
  var rear = 0 // rear 初始化为 = 0, rear 指向队列的 最后元素的后一个位置, 因为需要空出一个位置做约定

  //判断队列是否满
  def isFull(): Boolean = {
    (rear + 1) % maxSize == front
  }
  //判断队列是空，和前面一样
  def isEmpty(): Boolean = {
    rear == front
  }


  //从队列中取出数据
  //异常时可以加入业务逻辑
  def getQueue(): Any = {
    if(isEmpty()) {
      return new Exception("队列空，没有数据返回");
    }
    //返回front指向的值
    //1. 先把  arr(front) 保存到一个临时变量
    //2. front += 1
    //3. 返回临时变量
    var temp = arr(front)
    front = (front + 1) % maxSize
    return temp
  }

  //查看队列的头元素，但是不取出
  def peek(): Any = {
    if(isEmpty()) {
      return new Exception("队列空，没有数据返回");
    }
    return arr(front)
  }

  //给队列添加数据
  def addQueue(num: Int): Unit = {
    if (isFull()) {
      println("队列满，不能加入!!")
      return
    }
    //先把数据放入到arr(rear) 位置，然后后移rear
    arr(rear) = num
    rear = (rear + 1) % maxSize
  }

  //遍历队列
  def showQueue(): Unit = {
    if (isEmpty()) {
      println("队列空!!")
      return
    }
    //动脑筋
    //1. 从front 开始打印 ， 打印几个元素
    for (i <- front until (front + size()) ) {
      printf("arr(%d)=%d\t", (i % maxSize) , arr(i % maxSize))
    }
    println()
  }

  //求出当前队列共有多少个数据
  def size(): Int = {
    (rear + maxSize - front) % maxSize
  }

}

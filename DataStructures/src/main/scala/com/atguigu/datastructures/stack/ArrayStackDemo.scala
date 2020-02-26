package com.atguigu.datastructures.stack

import scala.io.StdIn

object ArrayStackDemo {
  def main(args: Array[String]): Unit = {
    //测试栈
    val stack = new ArrayStack(4)
    var key = ""

    while (true) {
      println("list: 显示栈")
      println("push: 入栈")
      println("pop: 出栈")
      println("peek: 查看栈顶")
      key = StdIn.readLine()
      key match {
        case "list" => stack.list()
        case "push" =>
          println("请输入一个数")
          val value = StdIn.readInt()
          stack.push(value)
        case "pop" =>
          val res = stack.pop()
          if(res.isInstanceOf[Exception]) {
            println(res.asInstanceOf[Exception].getMessage)
          }else{
            printf("取出栈顶的元素是%d", res)
          }
        case "peek" =>
          val res = stack.peek()
          if(res.isInstanceOf[Exception]) {
            println(res.asInstanceOf[Exception].getMessage)
          }else{
            printf("栈顶的元素是%d", res)
          }
          //默认处理

      }
    }


  }
}

//ArrayStack 表示栈
class ArrayStack(arrMaxSize:Int) {
  var top = -1
  val maxSize = arrMaxSize
  val arr = new Array[Int](maxSize)

  //判断栈空
  def isEmpty(): Boolean = {
    top == -1
  }
  //栈满
  def isFull(): Boolean = {
    top == maxSize - 1
  }

  //入栈操作
  def push(num:Int): Unit = {
    if(isFull()) {
      println("栈满，不能加入")
      return
    }
    top += 1
    arr(top) = num
  }

  //出栈操作
  def pop(): Any = {
    if(isEmpty()) {
      return new Exception("栈空，没有数据")
    }
    val res = arr(top)
    top -= 1
    return res
  }

  //遍历栈
  def list(): Unit = {
    if(isEmpty()) {
      println("栈空")
      return
    }
    //使用for
    for(i <- 0 to top reverse) { //逆序打印
      printf("arr(%d)=%d\n", i, arr(i))
    }
    println()
  }

  //查看栈的元素是什么，但是不会真的把栈顶的数据弹出
  def peek(): Any = {
    if(isEmpty()) {
      return  new Exception("栈空")
    }
    return arr(top)
  }

}

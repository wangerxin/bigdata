package com.atguigu.datastructures.linkedlist

import util.control.Breaks._
object JosephuDemo {
  def main(args: Array[String]): Unit = {
    //先创建 Josephu
    val josephu = new Josephu()
    josephu.addBoy(5)
    josephu.list()

    //测试一把
    //josephu.countBoy(2, 2, 5) // 3 -> 5 - 2 -> 1-> 4
    josephu.countBoy2(2, 2, 5) // 3 -> 5 - 2 -> 1-> 4

  }
}

//思路-》代码
class Josephu {
  //定义一个first指针，指向第一个小孩
  //当first == null， 表示环形链表为空
  var first: Boy = null

  //根据 n k m 的值，得到小孩出圈的顺序
  //思路
  /*
  2)  需要创建一个辅助指针helper ，在 first后
  3)  当first 指向了要删除的结点时，first = first.next  helper.next = fisrt
  4)  流程继续
  5)  当 first == helper 就退出

   */
  def countBoy(startNO:Int, countNum:Int, nums:Int ): Unit = {
    //简单的数据校验
    if(first == null || startNO > nums ||  startNO < 1 ) {
      println("参数有误")
      return
    }

    //需要创建一个辅助指针helper ，在 first后
    var helper = first
    breakable {
      while (true) {
        if (helper.next == first) {
          break()
        }
        helper = helper.next
      }
    }

    //需要将 first 移动到   startNO 这个小孩节点
    //helper 需要跟在first
    //移动的次数是  startNO - 1
    for(i <- 0 until startNO - 1) {
      first = first.next
      helper = helper.next
    }

    breakable {
      while (true) {
        //判断是否只有一个小孩了
        if (helper == first) {
          break()
        }
        //现在开始数 countNum 下，移动次数是 countNum - 1
        for (i <- 0 until countNum - 1) {
          first = first.next
          helper = helper.next
        }
        //输出一下出圈的小孩的编号
        printf("出圈的小孩编号%d\n", first.no)
        first = first.next
        helper.next = first
      }
    }
    printf("最后留在圈中的小孩是%d", first.no)
  }

  def countBoy2(startNO:Int, countNum:Int, nums:Int ): Unit = {
    println("countBoy2 完成 出圈")
    //简单的数据校验
    if(first == null || startNO > nums ||  startNO < 1 ) {
      println("参数有误")
      return
    }

    //需要创建一个辅助指针helper ，在 first后
    var helper = first
    breakable {
      while (true) {
        if (helper.next == first) {
          break()
        }
        helper = helper.next
      }
    }

    //需要将 first 移动到   startNO 这个小孩节点
    //helper 需要跟在first
    //移动的次数是  startNO - 1
    for(i <- 0 until startNO - 1) {
      helper = helper.next
    }
    //当上门的for循环结束后，helper指向了要数数的小孩的前一个节点

    breakable {
      while (true) {
        //判断是否只有一个小孩了
        if (helper == helper.next) {
          break()
        }
        //现在开始数 countNum 下，移动次数是 countNum - 1
        for (i <- 0 until countNum - 1) {
          helper = helper.next
        }
        //输出一下出圈的小孩的编号
        printf("出圈的小孩编号%d\n", helper.next.no)
        helper.next = helper.next.next
      }
    }
    printf("最后留在圈中的小孩是%d", helper.no)
  }

  //先创建环形链
  def addBoy(nums: Int): Unit = {
    //思路
    //创建一个辅助指针，帮助我们创建这个环形链表
    var curBoy: Boy = null
    for (i <- 1 to nums) {
      val boy = new Boy(i)
      if (i == 1) { //  1. 先处理第一个小孩，
        first = boy
        first.next = first
        curBoy = first
      } else {
        curBoy.next = boy
        boy.next = first
        curBoy = boy
      }
    }
  }

  //遍历环形链表
  def list(): Unit = {
    //遍历时，因为first 不能动，因此我们使用辅助指针完成
    if(first == null) {
      println("环形链表空, 无法遍历")
      return
    }
    var curBoy = first
    breakable {
      while (true) {
        printf("小孩编号%d\n", curBoy.no)

        //判断是否需要退出链表
        if (curBoy.next == first) {
          break()
        }
        curBoy = curBoy.next //后移
      }
    }
  }
}

//小孩结点
class Boy(bNo: Int) {
  val no = bNo
  var next: Boy = null
}
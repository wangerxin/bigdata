package com.atguigu.datastructures.temp
import util.control.Breaks._
object JosephuDemo {
  def main(args: Array[String]): Unit = {
    val josephu = new Josephu()
    josephu.addBoy(5)
    josephu.list()
    josephu.countBoy(2,2,5)// 2->4->1->5->3
  }
}

class Boy(bNo: Int) {
  val no = bNo
  var next: Boy = null
}

class Josephu {
  var first: Boy = null

  def countBoy(startNO:Int, countNum:Int, nums:Int): Unit = {
    if(first == null || startNO < 1 || startNO > nums) {
      println("参数有误")
      return
    }

    //1.
    var helper = first
    breakable {
      while (true) {
        if (helper.next == first) {
          break()
        }
        helper = helper.next
      }
    }
    //2.
    //helper移动到startNO前一个节点
    for(i <- 0 until (startNO - 1) ) {
      helper = helper.next
      //first = first.next
    }
    //3.循环的报数
    breakable {
      while (true) {
        if (helper.next == helper) {
          break()
        }
        //移动 countNum - 1
        for (i <- 0 until countNum - 1) {
          helper = helper.next
          //first = first.next
        }
        printf("出圈的小孩编号%d\n", helper.next.no)
        helper.next = helper.next.next
      }
    }
    printf("最后小孩编号%d", helper.no)

  }

  def addBoy(nums: Int): Unit = {

    //辅助指针
    var curBoy = first
    for (i <- 1 to nums) {
      //创建
      val boy = new Boy(i)
      if (i == 1) {
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

  //显示
  def list(): Unit = {
    if(first == null) {
      println("空")
      return
    }
    var curBoy = first
    breakable {
      while (true) {
        printf("小孩编号 %d\n", curBoy.no)
        if (curBoy.next == first) {
          break()
        }
        curBoy = curBoy.next
      }
    }
  }
}

package com.atguigu.datastructures.linkedlist

import scala.util.control.Breaks.{break, breakable}

object DoubleLinkedListDemo {
  def main(args: Array[String]): Unit = {
    //测试双向链表的添加和遍历
    val node1 = new HeroNode2(1, "宋江", "及时雨")
    val node2 = new HeroNode2(2, "卢俊义", "玉麒麟")
    val node3 = new HeroNode2(3, "吴用", "智多星")
    val node4 = new HeroNode2(3, "林冲", "豹子头")

    val doubleLinkedList = new DoubleLinkedList()
    doubleLinkedList.add(node3)
    doubleLinkedList.add(node1)
    doubleLinkedList.add(node2)


    println("双向链表的情况")
    doubleLinkedList.list()

    doubleLinkedList.update(node4)
    println("双向链表修改后")
    doubleLinkedList.list()

    //做删除
    doubleLinkedList.del(3)
    println("双向链表删除结点后")
    doubleLinkedList.list()


  }
}

//定义一个带头结点的双向链表
//添加，遍历， 修改， 删除
class DoubleLinkedList  {
  //定义一个头结点
  //1 . 头结点不能动
  val head = new HeroNode2(0, "", "")
  // 增加一个 var last 在我们的双向链表的最后
  // 1. 顺序遍历list
  // 2. 逆序遍历last-> pre ...

  //双向链表的第二种添加方式(顺序添加)
  //add2

  //删除-直接找到要删除的结点，自我删除
  def del(no:Int): Unit = {
    //先去检测是否为空链表，是退出
    if(head.next == null) {
      println("链表为空，不能遍历~")
      return
    }
    //直接让temp指向head.next,然后定位
    var temp = head.next
    var flag = false
    breakable {
      while (true) {
        if (temp.no == no) {
          flag = true
          break() //找到
        }
        if (temp.next == null) {
          break(); //没有这个删除的结点
        }
        temp = temp.next
      }
    }

    //判断是否需要删除
    if(flag) {
      //删除
      temp.pre.next = temp.next
      if(temp.next != null) {//!!!
        temp.next.pre = temp.pre
      }

    }else {
      //没有找到
      printf("你要删除的no=%d 结点不存在", no)
    }


  }

  //修改和前面一样的
  //修改
  //1. 给我一个新的结点，根据结点的no，去该对应的链表中 的结点的信息
  def update(heroNode: HeroNode2): Unit = {
    //先去检测是否为空链表，是退出
    if(head.next == null) {
      println("链表为空，不能遍历~")
      return
    }
    //先找到要修改的结点
    var temp = head.next
    //设置一个标识变量，表示是否找到要修改的结点[小技巧]
    var flag = false
    breakable {
      while (true) {
        if (temp.no == heroNode.no) {
          //说明找到
          flag = true
          break()
        }
        if (temp.next == null) {
          break()
        }
        temp = temp.next //后移
      }
    }

    if(flag) {
      //修改
      temp.name = heroNode.name
      temp.nickName = heroNode.nickName
    }else {
      printf("你要修改的节点no=%d 不存在~", heroNode.no)
    }

  }

  //双向链表的遍历和单向链表一样(指的是从head开始)
  //遍历
  def list(): Unit = {
    //先去检测是否为空链表，是退出
    if(head.next == null) {
      println("链表为空，不能遍历~")
      return
    }

    //1.定义一个辅助变量，帮助遍历整个链表
    //因为有效的节点是从head后一个
    var temp = head.next
    breakable {
      while (true) {
        //先输出temp指向的节点信息
        printf("结点信息为 no = %d name=%s nickname=%s\n", temp.no, temp.name, temp.nickName)
        //判断是否temp是不是最后一个结点
        if (temp.next == null) {
          break()
        }
        temp = temp.next //让temp后移，实现遍历
      }
    }

  }

  //添加结点到双向链表
  def add(heroNode: HeroNode2): Unit = {
    //1. 先找到链表的最后结点
    //2. 先定义一个辅助变量(指针),指向 head
    var temp = head
    //3.遍历链表，直到到temp.next == null
    breakable {
      while (true) {
        if (temp.next == null) {
          //temp已经是最后结点
          break()
        }
        temp = temp.next //temp后移
      }
    }
    //4. 当退出while时，temp 指向最后结点
    temp.next = heroNode
    heroNode.pre = temp
  }
}

//创建一个HeroNode 表示节点
class HeroNode2(hNo: Int, hName: String, hNickname: String) {
  val no = hNo
  var name = hName
  var nickName = hNickname
  var next: HeroNode2 = null //next默认为null
  var pre: HeroNode2 = null
}


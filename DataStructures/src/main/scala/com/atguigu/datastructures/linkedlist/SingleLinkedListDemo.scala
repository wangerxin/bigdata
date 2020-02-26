package com.atguigu.datastructures.linkedlist

import util.control.Breaks._
import java.util
object SingleLinkedListDemo {
  def main(args: Array[String]): Unit = {
    val node1 = new HeroNode(1, "宋江", "及时雨")
    val node2 = new HeroNode(2, "卢俊义", "玉麒麟")
    val node3 = new HeroNode(3, "吴用", "智多星")
   // val node4 = new HeroNode(3, "林冲", "豹子头")

    //创建后一个单向链表
    val singleLinkedList = new SingleLinkedList()
    //加入
    singleLinkedList.add(node1)
    singleLinkedList.add(node3)
    singleLinkedList.add(node2)


//    singleLinkedList.addByOrder(node2)
//    singleLinkedList.addByOrder(node1)
//    singleLinkedList.addByOrder(node3)
//    singleLinkedList.addByOrder(node4)


    println("链表的情况是")
    singleLinkedList.list()

    println("使用栈机制，逆序打印链表")
    singleLinkedList.reversePrint()



    /*
    //测试修改
    val node4 = new HeroNode(3, "武松", "行者")
    singleLinkedList.update(node4)
    println("链表修改后的情况")
    singleLinkedList.list()

    //测试删除节点

    singleLinkedList.del(3)
    singleLinkedList.del(1)
    singleLinkedList.del(2)
    println("删除后的链表")
    singleLinkedList.list() */

  }
}

//创建一个SingleLinkedLis 表示单向链表
class SingleLinkedList {
  //定义一个头结点
  //1 . 头结点不能动
  val head = new HeroNode(0, "", "")


  //使用栈逆序打印单向链表
  //要求不能破坏链表本身结构
  //思路
  //1. 先创建一个栈 stack
  //2. 遍历单向链表，把每个结点push 到 stack
  //3. 遍历stack 即可
  def reversePrint(): Unit = {
    if(head.next == null) {
      println("链表空")
      return
    }
    //目前单向链表是有head
    val stack = new util.Stack[HeroNode]()
    var temp = head.next

    //遍历单向链表，把每个结点push 到 stack
    breakable {
      while (true) {
        //将temp push到stack
        stack.push(temp)
        if (temp.next == null) { //temp是最后结点
          break()
        }
        temp = temp.next
      }
    }

    //遍历stack
    while (!stack.empty()) {
      val node = stack.pop()
      printf("结点信息 %d %s %s\n", node.no, node.name,node.nickName)
    }


  }

  //删除
  //需求：因为我们是单向链表，因此我们要找到要删除节点的前一个节点，才能删除该节点
  def del(no:Int): Unit = {
    //先去检测是否为空链表，是退出
    if(head.next == null) {
      println("链表为空，不能遍历~")
      return
    }
    //因为head不能动，需要辅助变量
    var temp = head
    var flag = false
    breakable {
      //遍历，找到要删除的节点的前一个节点
      while (true) {
        if (temp.next.no == no) {
          //找到了
          flag = true
          break()
        }
        //退出条件
        if (temp.next.next == null) {
          break()
        }
        temp = temp.next //遍历
      }
    }
    if(flag) { //找到
        temp.next = temp.next.next
    } else {
      printf("你要删除的节点no=%d 不存在~", no)
    }
  }

  //修改
  //1. 给我一个新的结点，根据结点的no，去该对应的链表中 的结点的信息
  def update(heroNode: HeroNode): Unit = {
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

  //添加结点到链表
  //第二种方式在添加英雄时，根据排名将英雄插入到指定位置(如果有这个排名，则添加失败，并给出提示)
  def addByOrder(heroNode: HeroNode): Unit = {
    var temp = head

    var flag = false
    breakable {
      while (true) {

        if (temp.next == null) {
          //加入到链表的最后
          flag = true
          break()
        }

        if (temp.next.no > heroNode.no) {
          //说明 heroNode 添加到temp的后面
          flag = true
          break()
        } else if (temp.next.no == heroNode.no) {
          //在链表中已经存在这个编号
          break()
        }

        temp = temp.next //后移
      }
    }

    if(flag) { //加入到temp的后面
      heroNode.next = temp.next //
      temp.next = heroNode
    }else {
      printf("你要加入的编号%d 已经存在不能添加\n", heroNode.no)
    }
  }

  //添加结点到单向链表
  def add(heroNode: HeroNode): Unit = {
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
  }
}

//创建一个HeroNode 表示节点
class HeroNode(hNo: Int, hName: String, hNickname: String) {
  val no = hNo
  var name = hName
  var nickName = hNickname
  var next: HeroNode = null //next默认为null
}


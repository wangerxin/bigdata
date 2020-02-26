package com.atguigu.datastructures.hashtable

import util.control.Breaks._
object HashTableDemo {
  def main(args: Array[String]): Unit = {
    val emp1 = new Emp(1,"tom")
    val emp2 = new Emp(2,"mary")
    val emp3 = new Emp(3,"smith")
    val emp4 = new Emp(4,"terry")
    val emp5 = new Emp(5,"tomcat")
    val emp6 = new Emp(11,"king")

    //创建HashTable
    val hashTable = new HashTable(7)
    hashTable.addEmp(emp1)
    hashTable.addEmp(emp2)
    hashTable.addEmp(emp3)
    hashTable.addEmp(emp4)
    hashTable.addEmp(emp5)
    hashTable.addEmp(emp6)

    //显示
    hashTable.list()

    //测试查找
    val emp = hashTable.findByNo(111)
    if(emp != null) {
      printf("找到了" + emp.no + " " + emp.name )
    } else {
      println("没有找到")
    }
  }
}


//创建HashTable , 管理7条链表
class HashTable(size:Int) {
  val linkedArr: Array[EmpLinkedList] = new Array[EmpLinkedList](size)
  //将 linkedArr 数组中的每一个链表对象，再new
  for(i <- 0 until linkedArr.length ) {
    linkedArr(i) = new EmpLinkedList()
  }

  //查找
  def findByNo(no:Int): Emp = {
    //先确定到哪条链表去查找
    println("在" + hash(no) + "去找雇员")
    return linkedArr(hash(no)).findByNO(no)
  }

  //添加方法， 由 HashTable 来通过哈希得到应该加入的链表
  def addEmp(emp: Emp): Unit = {
    val linkedListNO = hash(emp.no)//散列
    linkedArr(linkedListNO).add(emp)
  }
  //遍历整个哈希表
  def list(): Unit = {
    for(i <- 0 until linkedArr.length) {
      linkedArr(i).list(hash(i))
      println()
    }
  }

  /**
    *
    * @param no 传入雇员的no
    * @return 返回该雇员应该加入到哪个链表中
    */
  def hash(no:Int): Int = {
    no % size
  }
}

//创建Emp 类，表示雇员
class Emp(eNo: Int, eName: String) {
  val no = eNo
  var name = eName
  var next: Emp = null
}

//创建EmpLinkedList 存放Emp的单链表
class EmpLinkedList {
  var head: Emp = null//链表头
  //添加
  def add(emp: Emp): Unit = {
    if(head == null) { //添加的第一个Emp
      head = emp
    } else {
      //使用辅助指针完成添加, 简单处理直接加入到链表的最后
      var curEmp = head
      while (curEmp.next != null) {
        curEmp = curEmp.next
      }
      //退出while后curEmp就指向最后
      curEmp.next = emp
    }
  }

  //查找
  //如果找到返回 Emp对象，没有则返回null
  def findByNO(no:Int): Emp = {
    if(head==null) {
      println("空链表")
      return null
    }
    var curEmp = head

    breakable {
      while (true) {
        if (curEmp.no == no) {
          //找到
          break() //这时curEmp就是你要找到Emp
        }
        if (curEmp.next == null) {
          //这时curEmp就是是最后
          curEmp = null
        }

        curEmp = curEmp.next
      }
    }

    return curEmp
  }

  //遍历
  def list(linkedListNO:Int): Unit = {
    //判断链表是否为空
    if(head == null) {
      println("第"+linkedListNO+"链表为空")
      return
    }
    var curEmp = head
    print("第"+linkedListNO+"链表雇员 : ")
    while(curEmp != null) {
      //输出curEmp信息
      printf("雇员的信息 no=%d name = %s => ", curEmp.no, curEmp.name)
      curEmp = curEmp.next
    }
    println()

  }
}
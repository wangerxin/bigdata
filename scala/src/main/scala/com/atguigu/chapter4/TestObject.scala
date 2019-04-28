package com.atguigu.chapter4

object TestObject {

  def main(args: Array[String]): Unit = {

    val people = new People("zhangsan")
    println(people.name)

    val people2 = new People("lisi",20)
    println(people2.name+":"+people2.age)


  }

}

class People(inName : String){

  var name = inName
  var age : Int = _
  println("主构造器执行了")

  def this(inName : String ,inAge : Int){
    this("haha")
    name = inName
    age = inAge

    println("辅助构造器执行了")
  }

}



package com.atguigu.chapter3


import scala.collection.immutable.{HashMap, SortedSet}
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random


object HemoWork {

  def main(args: Array[String]): Unit = {

    //编写一段代码，将a设置为一个n个随机整数的数组，要求随机数介于0和n之间。
    val array = new Array[Long](10)

    for (i <- Range(1, 9)) {
      array(i) = Random.nextInt(10)
    }
    //array.foreach(println(_))

    //设置一个映射，其中包含你想要的一些装备，以及它们的价格。然后根据这个映射构建另一个新映射，采用同一组键，但是价格上打9折。
    val map1 = HashMap("apple" -> 100, "orange" -> 200, "book" -> 300)
    val map2: Iterable[(String, Double, Int)] = for (t <- map1) yield (t._1, 0.9 * t._2, 10)
    //map2.foreach(println(_))

    //闭包:一个函数使用到了函数外部的"局部变量"
    var b = 100

    def add(num: Int): Int = {
      num + b
    }
    //println(add(1))

    //科里化
    def sum(x: Int)(y: Int): Int = {
      x + y
    }
    //println(sum(10)(10))

    //wordcount
    def wordCount(str: String): Int = {
      val array: Array[String] = str.split(" ")

      array.length
    }

    //println(wordCount("ni hai hao ma"))

    //数组最大值和最小值的对偶
    def tunlp(array: Array[Int]): (Int, Int) = {
      (array.max, array.min)
    }

    val tuple: (Int, Int) = tunlp(Array(1, 3, 5, 2, 4))
    //println(tuple)

    //集合
    var list: List[Int] = List(5, 6, 7)
    list = list :+ 4
    //print(list)

    //匹配
    val name = "zhangsan"
    val result: Any = name match {
      case "1" => 1
      case "zhangsan" => "zhangsan"
      case _ => "haha"
    }
    //print(result)

    val queue: mutable.Queue[Int] = mutable.Queue(1, 2, 3)
    queue += 4
    queue ++= list
    //println(queue)

    val set: SortedSet[Double] = SortedSet(1, 2, 7, 4, 5.0)
    //println(set)

    val names = List("Alice", "Bob", "Nick")
    //println(names.map(_.toUpperCase))

    val str = "+-3!"
    for (i <- str.indices) {
      var sign = 0
      var digit = 0

      str(i) match {
        case '+' => sign = 1
        case '-' => sign = -1
        //case ch => println(ch)
        case _ =>
      }
      //println(str(i) + " " + sign + " " + digit)
    }

    implicit var aaa: String = "li"

    implicit def test(implicit name: String): Unit = {
      println(name)
    }
    //test("aaaa")

    val a = 8
    val obj = if(a == 1) 1
    else if(a == 2) "2"
    else if(a == 3) BigInt(3)
    else if(a == 4) Map("aa" -> 1)
    else if(a == 5) Map(1 -> "aa")
    else if(a == 6) Array(1, 2, 3)
    else if(a == 7) Array("aa", 1)
    else if(a == 8) Array("aa")
    val r1 = obj match {
      case x: Int => x
      case s: String => s.toInt
      case BigInt => -1 //不能这么匹配
      case _: BigInt => Int.MaxValue
      case m: Map[String, Int] => "Map[String, Int]类型的Map集合"
      case m: Map[_, _] => "Map集合"
      case a: Array[Int] => "It's an Array[Int]"
      case a: Array[String] => "It's an Array[String]"
      case a: Array[_] => "It's an array of something other than Int"
      case _ => 0
    }
    //println(r1 + ", " + r1.getClass.getName)

    val set2: Set[Int] = Set(1,2,3,4,5)

    val list3 = List(2,4,3,1)
    val intToList: Map[Int, List[Int]] = list3.groupBy(_%2)
    //println(intToList.mkString)

    /*println(list3.reduceLeft(_ - _))//-6
    println(list3.reduceRight(_ - _))//0

    println(list3.foldLeft(10)(_ - _))//0
    println(list3.foldRight(10)(_ - _)) //10,1,2,3,4,*/


    //切割
    val words: List[String] = List("hello word","hello atguigu").flatMap(_.split(" "))

    //分组
    val stringToList: Map[String, List[String]] = words.groupBy(x=>x)

    //求出长度
    val map: Map[String, Int] = stringToList.map({
      case (k, v) => (k, v.length)
    })

    //排序
    val list4: List[(String, Int)] = map.toList
    val list5: List[(String, Int)] = list4.sortWith({
      case ((k1, v1), (k2, v2)) => v1 > v2
    })
   // println(list5.mkString(","))











  }
}
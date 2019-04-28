object WordCount {

  def main(args: Array[String]): Unit = {

    val lines = List("Atguigu,Scala","Hello,Scala","Hi,Atguigu,Scala")

    val words: List[String] = lines.flatMap(_.split(","))

    val group: Map[String, List[String]] = words.groupBy(x=>x)

    val wordLength: Map[String, Int] = group.map(t=>(t._1,t._2.length))

    val list: List[(String, Int)] = wordLength.toList

    val list2: List[(String, Int)] = list.sortWith({
      case ((k1, v1), (k2, v2)) => k1 < k2
    })

    //println(list2.mkString(","))

    val list3 = List(1,2,3,4)
    println(list3)

  }

}

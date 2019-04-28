object FoldLeftTest {
  def main(args: Array[String]): Unit = {

    val map1 = Map("a" -> 1, "b" -> 2, "c" -> 3)

    val map2 = Map("a" -> 3, "c" -> 2, "d" -> 1)

    val map: Map[String, Int] = map1.foldLeft(map2)({
      case (map2, (k1, v1)) => {
        println(map2.mkString(","))
        println((k1, v1))
        map2 + (k1 -> (v1 + map2.getOrElse(k1, 0)))
      }
    })
    println(map.mkString(","))




  }


}

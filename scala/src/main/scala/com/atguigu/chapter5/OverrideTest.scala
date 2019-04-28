package com.atguigu.chapter5

abstract class OverrideTest {

  def study

}

class AAA extends OverrideTest{

  override def study: Unit = {

  }
}

class Pair1[T <: Comparable[T]](val first: T, val second: T) {
  def smaller = if (first.compareTo(second) < 0) first else second
}

object Main1 extends App{
  override def main(args: Array[String]): Unit = {
    val p = new Pair1("Fred", "Brooks")
    println(p.smaller)
  }
}

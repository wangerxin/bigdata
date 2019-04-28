package com.atguigu.sparkmall.online

import redis.clients.jedis.Jedis

object Test {

  def main(args: Array[String]): Unit = {
    val jedis = new Jedis("hadoop102",6379)
    println(jedis.smembers("a"))

  }

}

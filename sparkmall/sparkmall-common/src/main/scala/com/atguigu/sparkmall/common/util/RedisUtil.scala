package com.atguigu.sparkmall.common.util

import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object RedisUtil {

  var jedisPool:JedisPool=null

  def getJedisClient: Jedis = {

    if(jedisPool==null){
      // 配置
      val jedisPoolConfig = new JedisPoolConfig()
      jedisPoolConfig.setMaxTotal(1000)  //最大连接数
      jedisPoolConfig.setMaxIdle(20)   //最大空闲
      jedisPoolConfig.setMinIdle(20)     //最小空闲
      jedisPoolConfig.setBlockWhenExhausted(true)  //忙碌时是否等待
      jedisPoolConfig.setMaxWaitMillis(1000)//忙碌时等待时长 毫秒
      jedisPoolConfig.setTestOnBorrow(true) //每次获得连接的进行测试

      // 创建redis连接池
      val redisHost = ConfigurationUtil.getValueFromConfig("redis.host")
      val redisPort = ConfigurationUtil.getValueFromConfig("redis.port")
      jedisPool=new JedisPool(jedisPoolConfig,redisHost,redisPort.toInt)
    }

    // 返回一个jedis
    //println(s"jedisPool.getNumActive = ${jedisPool.getNumActive}")
    jedisPool.getResource
  }

}

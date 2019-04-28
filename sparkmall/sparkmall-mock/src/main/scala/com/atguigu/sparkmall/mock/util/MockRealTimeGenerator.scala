import java.util.Properties

import com.atguigu.sparkmall.common.model.CityInfo
import com.atguigu.sparkmall.common.util.ConfigurationUtil
import com.atguigu.sparkmall.mock.util.{RanOpt, RandomOptions}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object MockRealTimeGenerator {

  /**
    *  模拟生成数据
    * 格式 ：timestamp area  city  userid  adid
    *         时间戳   地区  城市  用户id  广告id
    */
  def generateMockData(): Array[String] = {
    val array = ArrayBuffer[String]()
    val CityRandomOpt = RandomOptions(
      RanOpt( CityInfo(1,"北京","华北"),30),
      RanOpt(CityInfo(1,"上海","华东"),30),
      RanOpt(CityInfo(1,"广州","华南"),10),
      RanOpt(CityInfo(1,"深圳","华南"),20),
      RanOpt(CityInfo(1,"天津","华北"),10))


    // 模拟实时数据：timestamp province city userid adid
    val random = new Random()
    for (i <- 1 to 50) {
      val timestamp = System.currentTimeMillis()
      val cityInfo = CityRandomOpt.getRandomOpt()
      val city = cityInfo.city_name
      val area = cityInfo.area
      val adid = 1+random.nextInt(6)
      val userid = 1+random.nextInt(6)

      // 拼接实时数据
      array += timestamp + " " + area + " " + city + " " + userid + " " + adid
    }
    array.toArray
  }

  /*
  *  创建kafka生产者
  * */
  def createKafkaProducer(broker: String): KafkaProducer[String, String] = {

    // 创建配置对象
    val prop = new Properties()
    // 添加配置
    prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker)
    prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")

    // 创建Kafka生产者
    new KafkaProducer[String, String](prop)
  }


  def main(args: Array[String]): Unit = {

    // 获取配置文件commerce.properties中的Kafka配置参数
    val broker: String = ConfigurationUtil.getValueFromConfig("kafka.broker.list")
    val topic =  "sparkmall3"

    // 不断生成数据, 发送到kafka
    val kafkaProducer = createKafkaProducer(broker)
    while (true) {
      for (line <- generateMockData()) {
        kafkaProducer.send(new ProducerRecord[String, String](topic, line))
        println(line)
      }
      Thread.sleep(5000)
    }
  }
}
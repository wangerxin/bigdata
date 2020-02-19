//
//package sparkStreaming
//
//import kafka.message.MessageAndMetadata
//import kafka.serializer.StringDecoder
//import org.apache.spark.SparkConf
//import org.apache.spark.streaming.kafka010.{HasOffsetRanges, KafkaUtils, OffsetRange}
//import org.apache.spark.streaming.{Seconds, StreamingContext}
//import utils.ZkUtil
//
////https://blog.csdn.net/Lin_wj1995/article/details/80080359
//object ManagerOffsetRanges extends App{
//
//  //StreamingExamples.setStreamingLogLevels()
//  val sparkConf = new SparkConf().setAppName("ManagerOffsetRanges").setIfMissing("spark.master","local[*]")
//  val ssc = new StreamingContext(sparkConf, Seconds(3))
//
//  val topic = "first"
//  val brokers = "hadoop102:9092,hadoop103:9092,hadoop104:9092"
//  val fromOffSets = ZkUtil.getOffset(topic)
//  val messageHandler = (mmd: MessageAndMetadata[String,String]) => (mmd.message())
//  val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers, "group.id" -> "ManagerOffsetRanges")
//  val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder, String](ssc, kafkaParams, fromOffSets, messageHandler)
//
//  //用于保存每个rdd的offset
//  var offsetRanges = Array[OffsetRange]()
//
//  messages.transform(rdd => {
//    // 获取offsetRanges
//    offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
//    rdd
//  }).foreachRDD(rdd => {
//    //用于保存每个rdd的offset
//    val offsets = scala.collection.mutable.ArrayBuffer[String]()
//    for (o <- offsetRanges){
//      println(s"${o.topic} ${o.partition} ${o.fromOffset} ${o.untilOffset}")
//      offsets += s"${o.topic},${o.partition},${o.untilOffset}"
//    }
//    //todo offset保存的时间点 根据需求而定
//    ZkUtil.setOffset(offsets.toArray)
//
//    //todo 业务逻辑
//    println("#################")
//    //rdd.foreach(println)
//    println(rdd.count())
//  })
//
//  ssc.start()
//  ssc.awaitTermination()
//}
//
//

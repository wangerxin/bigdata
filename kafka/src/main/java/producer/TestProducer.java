package producer;


import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class TestProducer {
    public static void main(String[] args) {

        //1.配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.1.102:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("partitioner.class", "producer.CustomPartitioner");

        //2.创建生产者
        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        for (int i = 0; i < 10; i++) {

            //3.发送数据
            //3.1 封装数据
            ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("spark2kudu2", Integer.toString(i), Integer.toString(i));
            //3.2 发送数据,并且回调
            producer.send(producerRecord, new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (metadata !=null){
                        System.out.println(metadata.topic()+"-"+metadata.partition()+"-"+metadata.offset());
                    }
                }
            });
        }
        //4.关闭资源,刷写数据
        producer.close();
    }
}

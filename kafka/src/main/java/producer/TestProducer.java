package producer;

import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.Random;
import java.util.UUID;

/**
0.9.0版本连接kafka集群推荐使用bootstrap.servers，如果使用zookeeper则元数据保存在zookeeper。
 */
public class TestProducer {
    private String bootstrapServers;
    private String producerAcks;
    private Integer kafkaProducerRetries;
    private Integer batchSize;
    private Integer lingerMs;
    private Integer bufferMemory;
    private String topic;

    public void producer() {
        this.bootstrapServers = "10.1.20.25:9092,10.1.20.26:9092,10.1.20.27:9092,10.1.20.28:9092,10.1.20.29:9092,10.1.20.30:9092";
        this.batchSize = 16348;
        this.lingerMs = 0;
        this.producerAcks = "all";
        this.bufferMemory = 33554432;
        this.kafkaProducerRetries = 1;
        Properties properties = new Properties();
        properties.put("bootstrap.servers", this.bootstrapServers);
        properties.put("acks", this.producerAcks);
        properties.put("retries", this.kafkaProducerRetries);
        properties.put("batch.size", this.batchSize);
        properties.put("linger.ms", this.lingerMs);
        properties.put("buffer.memory", this.bufferMemory);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);

        JSONObject json = new JSONObject(16);
        ProducerRecord<String,String> producerRecord = null;
        String topic = "test_wex";
        json.put("user_id", new Random().nextInt(5) + 1);
        //json.put("item_id", 31000);
        //json.put("category_id", 41000);
        //json.put("behavior", "order");
        json.put("ts", "a\nb\rc\td");
        String jsonStr = json.toJSONString();
        String keyWord = UUID.randomUUID().toString();
        System.out.println(jsonStr);
        producerRecord = new ProducerRecord(topic, null, System.currentTimeMillis(), keyWord, jsonStr);

        if (true) {

            kafkaProducer.send(producerRecord, (metadata, exception) -> {
                if (exception != null) {
                    exception.printStackTrace();
                } else {
                }
            });

        } else {
            try {
                RecordMetadata recordMetadata = kafkaProducer.send(producerRecord).get();
                System.out.println("offset:" + recordMetadata.offset());

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        kafkaProducer.close();
    }

    public static void main(String[] args) throws Exception {
        TestProducer testProducer = new TestProducer();

        for (int i = 0; i < 1000; i++) {
            Thread.sleep(2000);
            testProducer.producer();
        }
    }
}

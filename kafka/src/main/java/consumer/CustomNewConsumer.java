package consumer;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class CustomNewConsumer {

    public static void main(String[] args) {

        //1.配置
        Properties props = new Properties();
        // 定义kakfa 服务的地址
        props.put("bootstrap.servers", "hadoop102:9092,hadoop103:9092,hadoop104:9092");
        // 消费者组id
        props.put("group.id", "test");
        // 是否自动提交offset
        props.put("enable.auto.commit", "true");
        // 自动确认offset的时间间隔
        props.put("auto.commit.interval.ms", "100");
        // key的反序列化类
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // value的反序列化类
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //2.创建消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        // 消费者订阅的topic, 可同时订阅多个.翻译:subscribe订阅
        consumer.subscribe(Arrays.asList("kafka2kudu2"));

        //3.消费者消费topic中的数据
        while (true) {
            // 3.1消费者一次拉取得到多条数据
            ConsumerRecords<String, String> records = consumer.poll(2);

            // 3.2处理每一条数据
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }
    }
}
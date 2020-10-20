package consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;

public class KafkaComsumerDemo {

    public static void main(String[] args) throws InterruptedException {

        Properties props = new Properties();
        props.put("bootstrap.servers", "10.1.20.25:9092,10.1.20.26:9092,10.1.20.27:9092,10.1.20.28:9092,10.1.20.29:9092,10.1.20.30:9092");
        props.put("group.id", "group_wex");
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "6000");
        props.put("max.poll.records", 5);
        //props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        consumer.subscribe(Arrays.asList("test_wex"));
        ConsumerRecords<String, String> msg = consumer.poll(5);
        for (ConsumerRecord<String, String> stringStringConsumerRecord : msg) {
            System.out.println(stringStringConsumerRecord.value());
        }
    }
}
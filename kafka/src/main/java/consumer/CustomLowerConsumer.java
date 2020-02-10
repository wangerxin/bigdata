package consumer;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.MessageAndOffset;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomLowerConsumer {

    public static void main(String[] args) {

        //1.参数信息
        ArrayList<String> brokers = new ArrayList<String>();
        brokers.add("hadoop102");
        brokers.add("hadoop103");
        brokers.add("hadoop104");
        int port = 9092;//连接kafka集群的端口号
        String topic = "kafka2kudu2";//待消费的主题
        int partition = 0;//待消费的分区
        long offset = 0;//待消费的位置信息

        //2.获取leader
        String leader = getLeader(brokers, port, topic, partition);

        //3.从指定的offset
        getData(leader, port, topic, partition, offset);
    }

    //获取某个partiton的leader主机名
    private static String getLeader(ArrayList<String> brokers, int port, String topic, int partition) {

        for (String broker : brokers) {

            //1.创建发送请求的对象
            SimpleConsumer consumer = new SimpleConsumer(broker, port, 5000, 1024 * 5, "consumerLeader");

            //2.请求topic的元数据信息
            TopicMetadataRequest request = new TopicMetadataRequest(Arrays.asList(topic));
            TopicMetadataResponse response = consumer.send(request);

            //3.分析topic的元数据信息
            //3.1 得到topic元数据集合
            List<TopicMetadata> topicMetadatas = response.topicsMetadata();
            for (TopicMetadata topicMetadata : topicMetadatas) {
                //3.2得到partition元数据集合
                List<PartitionMetadata> partitionMetadatas = topicMetadata.partitionsMetadata();
                for (PartitionMetadata partitionMetadata : partitionMetadatas) {
                    //3.3 得到某个指定分区的leader
                    if (partition == partitionMetadata.partitionId()) {
                        String leader = partitionMetadata.leader().host();
                        System.out.println(partition + "分区的leader是:" + leader);
                        return leader;
                    }
                }
            }
        }
        return null;
    }

    //从offset开始消费数据
    private static void getData(String leader, int port, String topic, int partition, long offset) {

        //1.创建消费者客户端,用于发送请求
        SimpleConsumer simpleConsumer = new SimpleConsumer(leader, port, 5000, 1024 * 5, "consumerData");

        //2.请求从offset开始消费数据
        //2.封装请求
        FetchRequestBuilder fetchRequestBuilder = new FetchRequestBuilder();
        fetchRequestBuilder.addFetch(topic, partition, offset, 1024 * 5);
        FetchRequest request = fetchRequestBuilder.build();
        FetchResponse response = simpleConsumer.fetch(request);

        //3.解析消费到的数据
        //3.1 得到消息和偏移量的集合
        ByteBufferMessageSet messageAndOffsets = response.messageSet(topic, partition);
        for (MessageAndOffset messageAndOffset : messageAndOffsets) {
            //3.2 得到消息
            ByteBuffer byteBuffer = messageAndOffset.message().payload();
            byte[] bytes = new byte[byteBuffer.limit()];
            byteBuffer.get(bytes);//将byteBuffer中的数据拷贝到bytes中
            String data = new String(bytes);
            //3.3 得到偏移量
            long getOffset = messageAndOffset.offset();
            //3.4 打印数据和偏移量
            System.out.println( getOffset+":"+data);
        }
    }

}

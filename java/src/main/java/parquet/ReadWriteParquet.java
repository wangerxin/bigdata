package parquet;

import org.apache.avro.Schema;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import parquet.column.ParquetProperties;
import parquet.example.data.Group;
import parquet.example.data.GroupFactory;
import parquet.example.data.simple.SimpleGroupFactory;
import parquet.hadoop.ParquetReader;
import parquet.hadoop.ParquetWriter;
import parquet.hadoop.example.GroupReadSupport;
import parquet.hadoop.example.GroupWriteSupport;
import parquet.schema.MessageType;
import parquet.schema.MessageTypeParser;

import java.util.ArrayList;
import java.util.List;

public class ReadWriteParquet {
    public static void main(String[] args) throws Exception {

        /**
         * 创建schema
         * REQUIRED, OPTIONAL, REPEATED
         */
        MessageType schema = MessageTypeParser.parseMessageType("message people {" +
                " required int32 id;" +
                " optional binary name (UTF8);" +
                " optional binary city (UTF8);" +
                "}");

        // 创建GroupWriteSupport
        Configuration configuration = new Configuration();
        GroupWriteSupport writeSupport = new GroupWriteSupport();
        writeSupport.setSchema(schema, configuration);

        // 创建ParquetWriter
        Path path = new Path("data.parquet");
        ParquetWriter<Group> writer = new ParquetWriter<Group>(path, writeSupport,
                ParquetWriter.DEFAULT_COMPRESSION_CODEC_NAME,
                ParquetWriter.DEFAULT_BLOCK_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE,
                ParquetWriter.DEFAULT_PAGE_SIZE, /* dictionary page size */
                ParquetWriter.DEFAULT_IS_DICTIONARY_ENABLED,
                ParquetWriter.DEFAULT_IS_VALIDATING_ENABLED,
                ParquetProperties.WriterVersion.PARQUET_1_0,
                configuration
        );

        // 构建工厂
        GroupFactory factory = new SimpleGroupFactory(schema);
        // 构建数据
        Group group = factory
                .newGroup()
                .append("id", 1)
                .append("name", "zhangsan")
                .append("city","beijing");

        // 写出数据
        writer.write(group);
        // 关闭资源
        writer.close();

        // 创建GroupReadSupport
        GroupReadSupport readSupport = new GroupReadSupport();
        // 创建ParquetReader
        ParquetReader<Group> reader = new ParquetReader<Group>(path, readSupport);
        // 读取数据
        Group result = reader.read();
        System.out.println(result);
    }
}

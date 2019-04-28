package partiton;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PartitionMapper extends Mapper<LongWritable,Text,Text,IntWritable> {

    Text k=new Text();
    IntWritable v=new IntWritable(1);
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        // 1 获取一行: 张三 男
        String line = value.toString();

        // 2 截取
        String[] fields = line.split(" ");

        // 3 封装对象
        k.set(fields[1]);

        // 4 写出
        context.write(k, v);
    }
}

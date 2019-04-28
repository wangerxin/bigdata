package writable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import word_count.WordCountDriver;
import word_count.WordCountMapper;
import word_count.WordCountReduce;

import java.io.IOException;

public class FlowDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.创建job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        //2.封装类
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReduce.class);
        job.setJarByClass(FlowDriver.class);
        //3.设置输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Flow.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Flow.class);
        //4.设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path("e:/data/phone.txt"));
        FileOutputFormat.setOutputPath(job,new Path("e:/data/phone_result"));
        //5.提交
        boolean result = job.waitForCompletion(true);
        System.out.println("运行结束");
        System.exit(result ? 0:1);
    }
}

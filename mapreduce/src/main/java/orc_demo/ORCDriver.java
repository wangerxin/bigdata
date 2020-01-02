package orc_demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.orc.mapred.OrcStruct;
import org.apache.orc.mapreduce.OrcInputFormat;
import org.apache.orc.mapreduce.OrcOutputFormat;

import java.io.IOException;
// https://www.2cto.com/net/201607/531847.html
public class ORCDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

//        //1.创建job
//        Configuration configuration = new Configuration();
//        Job job = Job.getInstance(configuration);
//        //2.设置3个类
//       job.setMapperClass(ORCMapper.class);
//        job.setReducerClass(ORCReduce.class);
//        job.setJarByClass(ORCDriver.class);
//        //3.设置输出类型
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(IntWritable.class);
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(IntWritable.class);
//        //4.设置输入输出路径
//        OrcInputFormat.addInputPath(job, new Path("hdfs://hadoop102:9000/user/hive/warehouse/test_orc"));
//        FileInputFormat.setInputPaths(job,new Path("hdfs://hadoop102:9000/user/hive/warehouse/test_orc"));
//        FileOutputFormat.setOutputPath(job,new Path("hdfs://hadoop102:9000/orc_output"));
//        //5.提交
//        boolean result = job.waitForCompletion(true);
//        System.out.println("运行结束");
//        System.exit(result ? 0:1);


         // 创建job
         Configuration conf = new Configuration();
         conf.set("orc.mapred.output.schema","struct<name:string,city:string>");
         Job job = Job.getInstance(conf);
         job.setNumReduceTasks(1);

         // 设置3个jar
         job.setJarByClass(ORCDriver.class);
         job.setMapperClass(ORCMapper.class);
         job.setReducerClass(ORCReduce.class);

         //设置输出类型
         job.setMapOutputKeyClass(Text.class);
         job.setMapOutputValueClass(Text.class);
         job.setOutputKeyClass(NullWritable.class);
         job.setOutputValueClass(OrcStruct.class);

         //设置输入输出路径
         FileInputFormat.addInputPath(job, new Path("hdfs://hadoop102:9000/user/hive/warehouse/test_orc"));
         FileOutputFormat.setOutputPath(job, new Path("hdfs://hadoop102:9000/user/hive/warehouse/test_orc2"));

         //设置输入输出格式
         job.setInputFormatClass(OrcInputFormat.class);
         job.setOutputFormatClass(OrcOutputFormat.class);

         //启动
         System.exit(job.waitForCompletion(true) ? 0 : 1);

    }
}



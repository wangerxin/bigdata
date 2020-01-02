package orc_demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
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
public class Mee2Driver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

         // 创建job
         Configuration conf = new Configuration();
         //conf.set("mapreduce.framework.name", "local");
         conf.set("orc.mapred.output.schema",
                 "struct<msisdn_1:string," +
                         "msimsi_1:string," +
                         "msimei_1:string," +
                         "geohash6:string," +
                         "start_time:bigint," +
                         "stay_time:bigint>");
         Job job = Job.getInstance(conf);
         //job.setNumReduceTasks(1);

         // 设置3个jar
         job.setJarByClass(Mee2Driver.class);
         job.setMapperClass(Mee2Mapper.class);
         job.setReducerClass(Mee2Reduce.class);

         //设置输出类型
         job.setMapOutputKeyClass(Text.class);
         job.setMapOutputValueClass(LongWritable.class);
         job.setOutputKeyClass(NullWritable.class);
         job.setOutputValueClass(OrcStruct.class);

         //设置输入输出路径
         FileInputFormat.addInputPath(job, new Path(args[0]));
         FileOutputFormat.setOutputPath(job, new Path(args[1]));

         //设置输入输出格式
         job.setInputFormatClass(OrcInputFormat.class);
         job.setOutputFormatClass(OrcOutputFormat.class);

         //启动
         job.waitForCompletion(true);
    }
}



package orc;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.orc.mapreduce.OrcInputFormat;

public class OrcDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

//        // 1 获取配置信息以及封装任务
//        Configuration configuration = new Configuration();
//        Job job = Job.getInstance(configuration);
//        job.setUser("atguigu");
//
//        // 2 设置jar加载路径
//        job.setJarByClass(OrcDriver.class);
//
//        // 3 设置map和reduce类
//        job.setMapperClass(OrcMapper.class);
//        //job.setReducerClass(WordcountReducer.class);
//
//        // 4 设置map输出 ,最终输出
//        job.setMapOutputKeyClass(Text.class);
//        job.setMapOutputValueClass(IntWritable.class);
//
//        job.setOutputKeyClass(Text.class);
//        job.setOutputValueClass(IntWritable.class);
//
//        // 5 设置输入和输出路径
//        FileInputFormat.setInputPaths(job, new Path("file:///E:/maven/input"));
//        FileOutputFormat.setOutputPath(job, new Path("file:///E:/maven/output"));
//
//        // 6 提交
//        boolean result = job.waitForCompletion(true);
//        System.exit(result ? 0 : 1);

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(OrcDriver.class);
        //job.setJobName("parquetthrfit");

        String in = "file:///E:/maven/input";
        String out = "file:///E:/maven/output";

        job.setMapperClass(OrcMapper.class);
        OrcInputFormat.addInputPath(job, new Path(in));
        job.setInputFormatClass(OrcInputFormat.class);
        job.setNumReduceTasks(0);

        job.setOutputFormatClass(TextOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(out));

        job.waitForCompletion(true);
    }
}

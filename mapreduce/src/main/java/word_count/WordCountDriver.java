package word_count;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.List;

public class WordCountDriver {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        //1.创建job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        //2.封装类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReduce.class);
        job.setJarByClass(WordCountDriver.class);
        //3.设置输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        //4.设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path("e:/xxx"));
        FileOutputFormat.setOutputPath(job,new Path("e/xxx"));
        //5.提交
        boolean result = job.waitForCompletion(true);
        System.out.println("运行结束");
        System.exit(result ? 0:1);
    }
}



package weibo_commonFriend.step2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FriendDriver2 {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Job job = Job.getInstance(new Configuration());

        //封装类
        job.setJarByClass(FriendDriver2.class);
        job.setMapperClass(FriendMapper2.class);
        job.setReducerClass(FriendReducer2.class);

        //设置输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputKeyClass(Text.class);

        //设置路径
        FileInputFormat.setInputPaths(job,new Path("e:/data/weibo_result1"));
        FileOutputFormat.setOutputPath(job,new Path("e:/data/weibo_result2"));

        //提交
        boolean result = job.waitForCompletion(true);
        System.exit(result? 0:1);
    }
}

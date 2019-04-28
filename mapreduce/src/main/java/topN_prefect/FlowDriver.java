package topN_prefect;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class FlowDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        //1.创建job
        Configuration configuration = new Configuration();
        Job job = Job.getInstance(configuration);
        //2.封装类
        job.setMapperClass(FlowMapper.class);
        job.setReducerClass(FlowReducer.class);
        job.setJarByClass(FlowDriver.class);
        //3.设置输出类型
        job.setMapOutputKeyClass(Flow.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Flow.class);
        //4.设置输入输出路径
        FileInputFormat.setInputPaths(job,new Path("E:/data/phone_result/part-r-00000"));
        FileOutputFormat.setOutputPath(job,new Path("e:/data/topN_result"));

        job.setGroupingComparatorClass(FlowComparator.class);

        //5.提交
        boolean result = job.waitForCompletion(true);
        System.out.println("运行结束");
        System.exit(result ? 0:1);
    }
}

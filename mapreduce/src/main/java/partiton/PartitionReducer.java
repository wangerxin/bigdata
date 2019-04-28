package partiton;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PartitionReducer extends Reducer<Text,IntWritable,Text,IntWritable> {

    IntWritable v=new IntWritable();
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        int sum=0;
        //1.累加
        for (IntWritable value : values) {
            sum+=value.get();
        }

        //2.封装
        v.set(sum);

        //3.写出
        context.write(key,v);

    }
}

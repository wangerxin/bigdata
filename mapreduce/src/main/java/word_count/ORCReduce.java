package word_count;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ORCReduce extends Reducer<Text,IntWritable,Text,IntWritable> {

    int sum;
    IntWritable v=new IntWritable();
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        //1.累加
        sum=0;
        for (IntWritable value : values) {
            sum +=value.get();
        }
        //2.封装value
        v.set(sum);
        //3.写出
        context.write(key,v);
    }
}

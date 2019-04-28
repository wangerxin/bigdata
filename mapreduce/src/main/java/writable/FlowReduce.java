package writable;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReduce extends Reducer<Text, Flow, Text,Flow> {

    Flow v=new Flow();
    @Override
    protected void reduce(Text key, Iterable<Flow> values, Context context) throws IOException, InterruptedException {

        //1.累加
        Integer upFlow=0;
        Integer downFlow=0;
        for (Flow value : values) {
            upFlow+=value.getUpFlow();
            downFlow+=value.getDownFlow();
        }
        //2.封装
        v.setFlow(upFlow,downFlow);
        //3.写出
        context.write(key,v);
    }
}

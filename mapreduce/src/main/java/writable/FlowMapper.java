package writable;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable,Text,Text,Flow>{

    Text k=new Text();
    Flow v=new Flow();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //1	13736230513	192.196.100.1	www.atguigu.com	2481	24681	200
        //1.读取一行
        String line = value.toString();
        //2.切割
        String[] fileds = line.split("\t");
        //3封装
        k.set(fileds[1]);
        Integer upFlow=Integer.parseInt(fileds[fileds.length-3]);
        Integer downFolw=Integer.parseInt(fileds[fileds.length-2]);
        v.setFlow(upFlow,downFolw);
        //4写出
        context.write(k,v);
    }
}

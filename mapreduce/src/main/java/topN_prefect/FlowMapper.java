package topN_prefect;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable,Text,Flow,Text> {

    private Flow flow=new Flow();
    private Text phone=new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        //1.读取一行
        String line = value.toString();
        //2.切割
        String[] fields = line.split("\t");
        //3.封装
        phone.set(fields[0]);
        flow.setFlow(Integer.parseInt(fields[1]),Integer.parseInt(fields[2]));
        //4.写出
        context.write(flow,phone);
    }
}

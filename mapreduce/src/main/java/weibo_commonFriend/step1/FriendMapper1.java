package weibo_commonFriend.step1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FriendMapper1 extends Mapper<LongWritable,Text,Text,Text> {

    Text k=new Text();
    Text v=new Text();
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        // 1 获取一行 A:B,C,D,F,E,O
        String line = value.toString();
        //2.切割
        String[] fields = line.split(":");
        //3.封装
        v.set(fields[0]);
        String[] friends = fields[1].split(",");
        //4.写出
        for (String s : friends) {
            k.set(s);
            context.write(k,v);
        }
    }
}

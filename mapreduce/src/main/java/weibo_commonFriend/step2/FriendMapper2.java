package weibo_commonFriend.step2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;

public class FriendMapper2 extends Mapper<LongWritable, Text, Text, Text> {

    Text k = new Text();
    Text v = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        //A	I,K,C,B,G,F,H,O,D,
        //1.读取一行
        String line = value.toString();
        //2.切割
        String[] split = line.split("\t");
        //3.封装
        v.set(split[0]);

        String[] peoples = split[1].split(",");
        Arrays.sort(peoples);
        for (int i = 0; i < peoples.length - 1; i++) {
            for (int j = i + 1; j < peoples.length; j++) {

                k.set(peoples[i] + "-" + peoples[j]);
                //4.写出
                context.write(k, v);
            }
        }
    }
}

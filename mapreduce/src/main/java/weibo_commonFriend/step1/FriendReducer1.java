package weibo_commonFriend.step1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FriendReducer1 extends Reducer<Text, Text, Text, Text> {

    StringBuilder sb = new StringBuilder();
    Text v = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        //sb清零
        sb.delete(0,sb.length());

        //1.累加
        for (Text value : values) {
            sb.append(value).append(",");
        }
        //2.封装
        v.set(sb.toString());

        //3.写出:  我是哪些人的好友
        context.write(key, v);
    }
}

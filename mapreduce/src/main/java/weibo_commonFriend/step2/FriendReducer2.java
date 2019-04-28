package weibo_commonFriend.step2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FriendReducer2 extends Reducer<Text,Text,Text,Text> {

    StringBuilder sb=new StringBuilder();
    Text v=new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        //1.清零
        sb.delete(0,sb.length());
        //2.累加
        for (Text value : values) {
            sb.append(value).append(",");
        }
        //3.封装
        v.set(sb.toString());
        //4.写出
        context.write(key,v);
    }
}

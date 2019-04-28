package topN;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class FlowMapper extends Mapper<LongWritable, Text, Flow, Text> {


    TreeMap<Flow, Text> treeMap = new TreeMap();
    Text v = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        Flow k = new Flow();

        //13470253144	180	180	360
        //1.读取
        String line = value.toString();
        //2.切割
        String[] fields = line.split("\t");
        //3.1封装key,vlaue
        String phoneNumber = fields[0];
        int upFlow = Integer.parseInt(fields[1]);
        int downFlow = Integer.parseInt(fields[2]);
        k.setFlow(upFlow, downFlow);
        v.set(phoneNumber);
        //3.2 封装treemap
        treeMap.put(k, v);
        if (treeMap.size() > 10) {
            treeMap.remove(treeMap.lastKey());
        }

        //遍历treemap
        Collection<Text> values = treeMap.values();
        for (Text text : values) {
            System.out.println("text = " + text.toString());
        }

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        Collection<Text> values = treeMap.values();
        for (Text value : values) {
            System.out.println("value = " + value.toString());
        }
        //4.写出
        Iterator<Flow> iterator = treeMap.keySet().iterator();
        while (iterator.hasNext()) {
            Flow key = iterator.next();
            context.write(key, treeMap.get(key));
        }
    }
}

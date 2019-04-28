package topN;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

public class FlowReduce extends Reducer<Flow, Text, Text, Flow> {

    TreeMap<Flow, Text> treeMap = new TreeMap<>();

    @Override
    protected void reduce(Flow key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        //key:180	180	360   vlaue:13470253144

        //遍历
        for (Text phone : values) {
            Flow vFlow = new Flow();
            vFlow.setFlow(key.getUpFlow(), key.getDownFlow());
            //封装treemap
            treeMap.put(vFlow, new Text(phone));

            //留10个最大的
            if (treeMap.size() > 10) {
                treeMap.remove(treeMap.lastKey());
            }
        }

    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {

        Collection<Text> values = treeMap.values();
        for (Text value : values) {
            System.out.println("value = " + value.toString());
        }
        Iterator<Flow> iterator = treeMap.keySet().iterator();
        while (iterator.hasNext()) {
            Flow flow = iterator.next();
            context.write(treeMap.get(flow), flow);
        }
    }
}

package topN_prefect;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class FlowReducer extends Reducer<Flow,Text,Text,Flow> {
    @Override
    protected void reduce(Flow key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        Iterator<Text> iterator = values.iterator();
        for (int i = 0; i < 10; i++) {
            context.write(iterator.next(),key);
        }
    }
}

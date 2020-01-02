package orc_demo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.orc.TypeDescription;
import org.apache.orc.mapred.OrcStruct;

import java.io.IOException;

/**
 * 1.schema
 * 2.OrcStruct
 * 3.pair.setFieldValue(0,value)
 * 4.context.write(NullWritable.get(),OrcStruct)
 */
public class ORCReduce extends Reducer<Text,Text,NullWritable,OrcStruct> {


    private final NullWritable k = NullWritable.get();
    private TypeDescription table_schema = TypeDescription.fromString("struct<name:string,city:string>");
    private OrcStruct v = (OrcStruct) OrcStruct.createValue(table_schema);

    private Text name = new Text();
    private Text city = new Text();

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

        city.set(values.iterator().next().toString()+"_");

        v.setFieldValue("name",key);
        v.setFieldValue("city",city);
        context.write(k,v);
    }
}

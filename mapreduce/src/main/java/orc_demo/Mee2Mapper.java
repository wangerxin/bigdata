package orc_demo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.orc.mapred.OrcStruct;

import javax.xml.bind.ValidationEvent;
import java.io.IOException;

public class Mee2Mapper extends Mapper<NullWritable, OrcStruct, Text, LongWritable> {
    Text k = new Text();
    LongWritable v = new LongWritable();

    @Override
    protected void map(NullWritable key, OrcStruct value, Context context) throws IOException, InterruptedException {

        /**
         2: msisdn_1
         3: msimsi_1
         4: msimei_1
         48: geohash6
         0: capture_time
         */
        k.set(value.getFieldValue(0).toString() + "&" +
                value.getFieldValue(1).toString() + "&" +
                value.getFieldValue(2).toString() + "&" +
                value.getFieldValue(3).toString());
        v.set(((LongWritable) value.getFieldValue(4)).get());
        context.write(k, v);
    }
}

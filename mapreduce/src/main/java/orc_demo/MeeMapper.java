package orc_demo;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.orc.mapred.OrcStruct;

import java.io.IOException;

public class MeeMapper extends Mapper<NullWritable, OrcStruct, Mee, LongWritable> {
    Mee k = new Mee();
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

        k.setMsisdn_1((value.getFieldValue(0)).toString());
        k.setMsimsi_1(value.getFieldValue(1).toString());
        k.setMsimei_1(value.getFieldValue(2).toString());
        k.setGeohash6(value.getFieldValue(4).toString());
        k.setCapture_time(((LongWritable)value.getFieldValue(4)).get());
        v.set(((LongWritable) value.getFieldValue(4)).get());

        context.write(k,v);
    }
}

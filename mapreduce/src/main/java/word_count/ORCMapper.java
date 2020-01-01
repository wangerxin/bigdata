package word_count;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.orc.mapred.OrcStruct;

import java.io.IOException;

public class ORCMapper extends Mapper<NullWritable,OrcStruct,Text,IntWritable> {

    Text k = new Text();
    IntWritable v = new IntWritable(1);

    @Override
    protected void map(NullWritable key, OrcStruct value, Context context) throws IOException, InterruptedException {

            context.write((Text)value.getFieldValue(0),v);
            context.write((Text)value.getFieldValue(1),v);
        }
}

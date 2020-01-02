package orc_demo;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.orc.mapred.OrcStruct;

import java.io.IOException;

public class ORCMapper extends Mapper<NullWritable,OrcStruct,Text,Text> {
    @Override
    protected void map(NullWritable key, OrcStruct value, Context context) throws IOException, InterruptedException {

            context.write((Text)value.getFieldValue(0),(Text)value.getFieldValue(1));
        }
}

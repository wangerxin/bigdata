package orc;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.orc.mapred.OrcStruct;
import org.apache.orc.mapreduce.OrcInputFormat;
import java.io.IOException;

public class OrcReaderMR {

    public static class OrcMap extends Mapper<NullWritable, OrcStruct, Text, IntWritable> {

        // Assume the ORC file has type: struct<s:string,i:int>
        public void map(NullWritable key, OrcStruct value, Context output) throws IOException, InterruptedException {
            // take the first field as the key and the second field as the value
            output.write((Text) value.getFieldValue(0), (IntWritable) value.getFieldValue(1));
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        //job.setJarByClass(ParquetThriftWriterMR.class);
        job.setJobName("parquetthrfit");

        String in = "hdfs://localhost:9000/user/work/warehouse/test_orc";
        String out = "hdfs://localhost:9000/test/orc";

        job.setMapperClass(OrcMap.class);
        OrcInputFormat.addInputPath(job, new Path(in));
        job.setInputFormatClass(OrcInputFormat.class);
        job.setNumReduceTasks(0);

        job.setOutputFormatClass(TextOutputFormat.class);

        FileOutputFormat.setOutputPath(job, new Path(out));


        job.waitForCompletion(true);
    }

}

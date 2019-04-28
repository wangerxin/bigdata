package mapreduce_hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HbaseToHbaseDriver implements Tool {

    private Configuration conf = null;

    public int run(String[] strings) throws Exception {

        //获取job
        Job job = Job.getInstance(conf);

        //设置jar
        job.setJarByClass(HbaseToHbaseDriver.class);

        //设置map
        Scan scan = new Scan();
        scan.setCacheBlocks(false);
        scan.setCaching(100);

        TableMapReduceUtil.initTableMapperJob(
                "student",
                scan,
                HbaseToHbaseMapper.class,
                ImmutableBytesWritable.class,
                Put.class,
                job
        );

        //设置reduce
        TableMapReduceUtil.initTableReducerJob(
                "student2",
                HbaseToHbaseReduce.class,
                job
        );

        //提交
        boolean result = job.waitForCompletion(true);
        return result ? 0 : 1;
    }

    public void setConf(Configuration configuration) {

        conf = configuration;
    }

    public Configuration getConf() {
        return conf;
    }

    public static void main(String[] args) {

        try {
            int result = ToolRunner.run(new HbaseToHbaseDriver(), args);
            System.exit(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

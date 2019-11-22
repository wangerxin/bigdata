
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;


public class HDFSclient {

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        //这里指定使用的是 hdfs文件系统
        conf.set("fs.defaultFS", "hdfs://10.110.83.17:8020");

        //通过这种方式设置java客户端身份
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        FileSystem fs = FileSystem.get(conf);

        fs.copyFromLocalFile(new Path("/Users/erxinwang/20190721.py"),new Path("/3commas/data/dwd/"));
        fs.close();
    }
}

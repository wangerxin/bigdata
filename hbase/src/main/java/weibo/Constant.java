package weibo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.util.Bytes;

public class Constant {

    //配置
    public static Configuration CONFIGURATON=HBaseConfiguration.create();

    //微博内容表的表名,列族
    public static final byte[] CONTENT_TABLE = Bytes.toBytes("weibo:content");
    public static final byte[] CONTENT_CF = Bytes.toBytes("info");

    //用户关系表的表名,列族
    public static final byte[] RELATIONS_TABLE = Bytes.toBytes("weibo:relations");
    public static final byte[] RELATIONS_ATTENTS = Bytes.toBytes("attents");
    public static final byte[] RELATIONS_FANS= Bytes.toBytes("fans");

    //微博收件箱表的表名,列族
    public static final byte[] BOX_TABLE= Bytes.toBytes("weibo:box");
    public static final byte[]  INBOX_CF= Bytes.toBytes("info");
}

package ddl_dml.ddl;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;


public class HbaseDDL {

    private static Configuration configuration;

    static {
        //1.配置
        configuration = HBaseConfiguration.create();
        //2.连接hbase
        configuration.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
    }

    /**
     * 需求:创建命名空间
     * @param nameSpace
     * @throws IOException
     * 注意事项:无法提前判断命名空间是否已经存在,只能try catch
     */
    public static void createNameSpace(String nameSpace) throws IOException {
        //1.创建hbase客户端admin对象,用于操作DDL
        Connection connection = ConnectionFactory.createConnection(configuration);
        Admin admin = connection.getAdmin();
        //2.创建命名空间描述器
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(nameSpace).build();
        try {
            //创建命名空间
            admin.createNamespace(namespaceDescriptor);
            System.out.println(nameSpace+"命名空间创建成功");
        } catch (NamespaceExistException e) {
            System.out.println(nameSpace+"命名空间已经存在");
        }
    }

    /**
     * 需求:判断表是否存在
     * @param tableName
     * @return
     * @throws IOException
     * 注意事项:hbase客户端操作表的时候,表名需要被封装成TableName对象
     */
    public static boolean isTableExist(String tableName) throws IOException {

        //1.创建hbase客户端admin对象,用于操作DDL
        Connection connection = ConnectionFactory.createConnection(configuration);
        Admin admin = connection.getAdmin();
        //2.判断表是否存在
        return admin.tableExists(TableName.valueOf(tableName));
    }

    /**
     * 创建表
     * @param tableName
     * @param cf
     * @throws IOException
     */
    public static void createTable(String tableName, String... cf) throws IOException {
        //1.创建hbase客户端admin对象,用于操作DDL
        Connection connection = ConnectionFactory.createConnection(configuration);
        Admin admin = connection.getAdmin();
        //2.判断是否传入列族
        if (cf.length<1) {
                System.out.println("请输入列族");
                return;
        }
        //3.创建表
        if (isTableExist(tableName)) {
            System.out.println(tableName + "已经存在");
        } else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            for (String c : cf) {
                hTableDescriptor.addFamily(new HColumnDescriptor(c));
            }
            admin.createTable(hTableDescriptor);
            System.out.println(tableName+"表创建成功");
        }
    }

    /**
     * 删除表
     * @param tableName
     * @throws IOException
     */
    public static void deleteTable(String tableName) throws IOException {
        //1.创建hbase客户端admin对象,用于操作DDL
        Connection connection = ConnectionFactory.createConnection(configuration);
        Admin admin = connection.getAdmin();
        //2.删除表
        if (isTableExist(tableName)){
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf(tableName));
            System.out.println(tableName+"删除成功");
        }else {
            System.out.println(tableName+"表不存在");
        }
    }
}

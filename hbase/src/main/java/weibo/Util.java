package weibo;

import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

public class Util {

    /**
     * 关闭资源
     * @param admin
     * @param connection
     */
    private static void close(Admin admin,Connection connection)  {
        if (null !=admin){
            try {
                admin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (null !=connection){
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建命名空间
     * @param nameSpace
     * @throws IOException
     */
    public static void createNameSpace(String nameSpace) throws IOException {

        //1.连接hbase
        Connection connection = ConnectionFactory.createConnection(Constant.CONFIGURATON);

        //2.创建hbase客户端admin对象,用于操作DDL
        Admin admin = connection.getAdmin();

        //3.创建命名空间
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(nameSpace).build();
        try {
            admin.createNamespace(namespaceDescriptor);
            System.out.println(nameSpace+"命名空间创建成功");
        } catch (NamespaceExistException e) {
            System.out.println(nameSpace+"命名空间已经存在");
        }finally {
            close(admin,connection);
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

        //1.连接hbase
        Connection connection = ConnectionFactory.createConnection(Constant.CONFIGURATON);

        //2.创建hbase客户端admin对象,用于操作DDL
        Admin admin = connection.getAdmin();

        //3.判断表是否存在
        return admin.tableExists(TableName.valueOf(tableName));
    }

    /**
     * 创建表
     * @param tableName
     * @param cfs
     * @throws IOException
     */
    public static void createTable(String tableName,int  versions,String... cfs) throws IOException {

        //1.连接hbase
        Connection connection = ConnectionFactory.createConnection(Constant.CONFIGURATON);

        //2.创建hbase客户端admin对象,用于操作DDL
        Admin admin = connection.getAdmin();

        //4.判断是否传入列族
        if (cfs.length<1) {
            System.out.println("请输入列族");
            return;
        }
        //5.创建表
        if (isTableExist(tableName)) {
            System.out.println(tableName + "已经存在");
        } else {
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            for (String cf : cfs) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);
                hColumnDescriptor.setMaxVersions(versions);//设置版本
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            admin.createTable(hTableDescriptor);
            System.out.println(tableName+"表创建成功");
        }

        //6.关闭资源
        close(admin,connection);
    }
}

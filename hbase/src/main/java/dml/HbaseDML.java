package dml;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HbaseDML {

    private static Configuration configuration;

    static {
        //1.创建配置
        configuration = HBaseConfiguration.create();
        //2.连接hbase
        configuration.set("hbase.zookeeper.quorum", "hadoop102,hadoop102,hadoop104");
        configuration.set("hbase.zookeeper.property.clientPort", "2181");
    }

    /**
     * 需求:插入一个数据,cell级别
     * 注意事项:hbase中所有数据都是以字节数据的格式存储的
     *
     * @param tableName
     * @param rowkey
     * @param columnFamily
     * @param colume
     * @param value
     * @throws IOException 思考: 1.参数如果是按照可变数组传入,如何处理
     *                     2.校验参数的正确性
     */
    public static void addRowData(String tableName, String rowkey, String columnFamily, String colume, String value) throws IOException {
        //1.创建操作hbase的客户端对象htable,用于操作dml语句
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf(tableName));
        //2.创建put对象封装数据,put对象与rowkey一一对应,可以封装多个列
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(colume), Bytes.toBytes(value));
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes("18"));
        //3.插入数据,put方法是表级别的,可以插入一到多个put对象(多行数据)
        table.put(put);
        System.out.println("插入成功");
        //4.关闭资源
        table.close();
    }

    /**
     * 需求:删除多行数据
     * 测试结果:
     * 1.传入的rowkey如果不存在,不会报错正常运行
     * 2.delete数据的时候,如果数据有多个版本,只会删除最新的版本,旧版本会替代新版本
     * 3.deletes数据的时候,会将所有版本全部删除
     * @param tableName
     * @param rows
     * @throws IOException
     */
    public static void deleteRows(String tableName, String... rows) throws IOException {
        //1.创建操作hbase的客户端对象htable,用于操作dml语句
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf(tableName));
        //2.封装被删除的数据
        List<Delete> deletes = new ArrayList();
        for (String row : rows) {
            //创建delete对象封装数据,delete与rowkey一一对应,可以封装多个列
            Delete delete = new Delete(Bytes.toBytes(row));
            deletes.add(delete);
        }
        //3.删除数据,delete方法是表级别的,可以删除一到多个delete对象(多行数据)
        table.delete(deletes);
        System.out.println("删除成功");
        //3.关闭资源
        table.close();
    }

    /**
     * 需求:查询整张表
     * @param tableName
     * @throws IOException
     */
    public static void scanTable(String tableName) throws IOException {
        //1.创建操作hbase的客户端对象:htable,用于操作dml语句
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf(tableName));
        //2.扫描表
        Scan scan = new Scan();
        ResultScanner results = table.getScanner(scan);
        //3.分析结果
        for (Result result : results) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                String cell_rowkey = Bytes.toString(CellUtil.cloneRow(cell));
                String cell_family = Bytes.toString(CellUtil.cloneFamily(cell));
                String cell_qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
                String cell_vlaue = Bytes.toString(CellUtil.cloneValue(cell));
                System.out.println(cell_rowkey+":"+cell_family+":"+cell_qualifier+":"+cell_vlaue);
            }
        }
        //4.关闭资源
        System.out.println("扫描完毕");
        table.close();
    }

    /**
     * 需求:查询一个rowkey下的数据,查询一个列族下的数据,查询一个列下的数据
     * 测试结果:当输入的参数不存在的时候,查询不到结果,但是不会报错
     * @param tableName
     * @param rowkey
     * @param columnFamily
     * @param column
     * @throws IOException
     */
    public static void getData(String tableName,String rowkey,String columnFamily,String column) throws IOException {
        //1.创建操作hbase的客户端对象htable,用于操作dml语句
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf(tableName));
        //2查询数据
        //2.1查询一个rowkey下的数据
        Get get = new Get(Bytes.toBytes(rowkey));
        //2.2查询一个列族下的数据
        //get.addFamily(Bytes.toBytes(columnFamily));
        //2.3查询一个列下的数据
        //get.addColumn(Bytes.toBytes(columnFamily),Bytes.toBytes(column));
        //2.4查询
        Result result = table.get(get);
        //3.分析数据
        Cell[] cells = result.rawCells();
        for (Cell cell : cells) {
            String cell_rowkey = Bytes.toString(CellUtil.cloneRow(cell));
            String cell_family = Bytes.toString(CellUtil.cloneFamily(cell));
            String cell_qualifier = Bytes.toString(CellUtil.cloneQualifier(cell));
            String cell_vlaue = Bytes.toString(CellUtil.cloneValue(cell));
            System.out.println(cell_rowkey+":"+cell_family+":"+cell_qualifier+":"+cell_vlaue);
        }
        //4.关闭资源
        System.out.println("查询完毕");
        table.close();
    }

}

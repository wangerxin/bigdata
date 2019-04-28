package weibo;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 1.发布微博
 * 2.关注用户
 * 3.取消关注用户
 */
public class DAO {

    public static void publish(String uid, String content) throws IOException {

        //a.向内容表中插入数据
        //a.1 连接Hbase
        Connection connection = ConnectionFactory.createConnection(Constant.CONFIGURATON);
        //a.2.创建内容表对象
        Table contentTable = connection.getTable(TableName.valueOf(Constant.CONTENT_TABLE));
        //a.2 设计rowkey: uid+时间戳
        long time = System.currentTimeMillis();
        String rowKey = uid + "_" + time;
        //a.3 插入微博内容
        Put Putcontent = new Put(Bytes.toBytes(rowKey));
        Putcontent.addColumn(Constant.CONTENT_CF, Bytes.toBytes("content"), Bytes.toBytes(content));
        contentTable.put(Putcontent);

        //b.从用户关系表中查询出粉丝
        //b.1创建关系表对象
        Table relationsTable = connection.getTable(TableName.valueOf(Constant.RELATIONS_TABLE));
        //b.2查询粉丝id
        List<byte[]> fansList = new ArrayList();
        Get get = new Get(Bytes.toBytes(uid));
        get.addFamily(Constant.RELATIONS_FANS);
        Result result = relationsTable.get(get);
        for (Cell cell : result.rawCells()) {
            byte[] fans = CellUtil.cloneQualifier(cell);
            fansList.add(fans);
        }
        if (fansList.size() <= 0) {
            return;
        }

        //c.收信箱表中粉丝插入偶像的微博内容
        //c.1创建收件箱表
        Table boxTable = connection.getTable(TableName.valueOf(Constant.BOX_TABLE));
        //c.2 使用put封装数据
        List<Put> puts = new ArrayList();
        for (byte[] fans : fansList) {
            Put put = new Put(fans);
            put.addColumn(Constant.INBOX_CF, Bytes.toBytes(uid), Bytes.toBytes(rowKey));
            puts.add(put);
        }
        //批量插入数据
        boxTable.put(puts);

        //关闭资源
        connection.close();
        contentTable.close();
        relationsTable.close();
        boxTable.close();

    }

    /**
     * 添加关注用户
     *
     * @param uid
     * @param attends
     */
    public static void attends(String uid, String... attends) throws IOException {

        //过滤
        if (attends == null || attends.length <= 0) {
            return;
        }

        //第一部分: 操作用户关系表,为粉丝添加偶像, 为偶像添加粉丝
        //获取连接
        Connection connection = ConnectionFactory.createConnection(Constant.CONFIGURATON);
        //获取用户关系表对象
        Table relationsTable = connection.getTable(TableName.valueOf(Constant.RELATIONS_TABLE));
        //准备put集合
        ArrayList<Put> puts = new ArrayList<Put>();
        Put fanPutAttends = new Put(Bytes.toBytes(uid));
        //遍历偶像
        for (String attend : attends) {
            //粉丝添加偶像
            fanPutAttends.addColumn(Constant.RELATIONS_ATTENTS, Bytes.toBytes(attend), null);
            //偶像添加粉丝
            Put attendPutFan = new Put(Bytes.toBytes(attend));
            attendPutFan.addColumn(Constant.RELATIONS_FANS, Bytes.toBytes(uid), Bytes.toBytes(""));
            puts.add(attendPutFan);
        }
        puts.add(fanPutAttends);
        //批量put
        relationsTable.put(puts);

        //第二部分: 查询新关注偶像的微博rowkey, 添加到我的收信箱
        //创建表对象
        Table boxTable = connection.getTable(TableName.valueOf(Constant.BOX_TABLE));
        Table contentTable = connection.getTable(TableName.valueOf(Constant.CONTENT_TABLE));

        //创建put封装一行数据
        Put fanputcontent = new Put(Bytes.toBytes(uid));
        //遍历新关注的偶像
        for (String attend : attends) {
            //取出一个偶像的所有微博
            Scan scan = new Scan(Bytes.toBytes(attend), Bytes.toBytes(attend + "|"));
            ResultScanner results = contentTable.getScanner(scan);
            Iterator<Result> resultIterator = results.iterator();
            //取出每条微博的rowkey
            while (resultIterator.hasNext()) {
                Result result = resultIterator.next();
                byte[] rowkey = result.getRow();
                //将rowkey封装到put
                fanputcontent.addColumn(Constant.INBOX_CF,
                        Bytes.toBytes(attend),
                        System.currentTimeMillis() + 1,
                        rowkey);
            }
        }
        //收信箱表插入一个put
        boxTable.put(fanputcontent);

        //关闭资源
        boxTable.close();
        contentTable.close();
        relationsTable.close();
        connection.close();
    }

    public static void cancelAttents(String uid, String... cancelAttents) throws IOException {

        //创建表
        Connection connection = ConnectionFactory.createConnection(Constant.CONFIGURATON);
        Table relationTable = connection.getTable(TableName.valueOf(Constant.RELATIONS_TABLE));

        //第一步: 操作用户关系表
        //准备集合装delete
        ArrayList<Delete> deletes = new ArrayList<Delete>();
        Delete deleteAttent = new Delete(Bytes.toBytes(uid));
        //遍历每一个取关
        for (String cancelAttent : cancelAttents) {
            //取消关注
            deleteAttent.addColumn(Constant.RELATIONS_ATTENTS, Bytes.toBytes(cancelAttent));
            //删除粉丝
            Delete deleteFans = new Delete(Bytes.toBytes(cancelAttent));
            deleteFans.addColumn(Constant.RELATIONS_FANS, Bytes.toBytes(uid));
            deletes.add(deleteFans);
        }
        //批量处理删除
        deletes.add(deleteAttent);
        relationTable.delete(deletes);

        //第二部分: 操作收信箱表
        //创建表对象
        Table boxTable = connection.getTable(TableName.valueOf(Constant.BOX_TABLE));
        //创建delete封装数据
        Delete deleteContent = new Delete(Bytes.toBytes(uid));
        //遍历
        for (String cancelAttent : cancelAttents) {
            deleteAttent.addColumns(Constant.INBOX_CF,Bytes.toBytes(cancelAttent));
        }
        //删除偶像的微博
        boxTable.delete(deleteContent);
    }

    /**
     * 作用: 查询某个人的所有微博内容
     * @param uid
     * @throws IOException
     */
    public static void userGetContent(String uid) throws IOException {

        //连接hbase
        Connection connection = ConnectionFactory.createConnection(Constant.CONFIGURATON);
        //创建表对象
        Table conTable = connection.getTable(TableName.valueOf(Constant.CONTENT_TABLE));
        //创建scan
        Scan scan = new Scan();
        RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator(uid));
        scan.setFilter(rowFilter);
        //执行查询
        ResultScanner resultScanner = conTable.getScanner(scan);
        //解析结果
        for (Result result : resultScanner) {
            for (Cell cell : result.rawCells()) {
                byte[] content = CellUtil.cloneValue(cell);
                System.out.println(uid+":"+Bytes.toString(content));
            }
        }
    }
}

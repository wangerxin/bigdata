package com.atguigu.ddl;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import java.util.LinkedList;
import java.util.List;

public class CreateTable {

    /**
     * 添加新列
     * @param name
     * @param type
     * @param iskey
     * @return
     */
    private static ColumnSchema addColumn(String name, Type type, boolean iskey,boolean allowNull) {
        ColumnSchema.ColumnSchemaBuilder column = new ColumnSchema.ColumnSchemaBuilder(name, type);
        column.key(iskey);
        column.nullable(allowNull);
        return column.build();
    }

    public static void main(String[] args) throws KuduException {

        // master地址
        String masteraddr = "hadoop103";
        // 创建kudu的数据库链接
        KuduClient client = new KuduClient.KuduClientBuilder(masteraddr).defaultSocketReadTimeoutMs(10000).build();

        // 设置列
        List<ColumnSchema> columns = new LinkedList<ColumnSchema>();
        // 与 RDBMS 不同，Kudu 不提供自动递增列功能，因此应用程序必须始终在插入期间提供完整的主键
        columns.add(addColumn("id", Type.INT32, true,false));
        columns.add(addColumn("name", Type.STRING, false,true));
        columns.add(addColumn("city", Type.STRING, false,true));

        Schema schema = new Schema(columns);

        //创建表时提供的所有选项
        CreateTableOptions options = new CreateTableOptions();
        // 设置表的分区规则
        List<String> parcols = new LinkedList<String>();
        parcols.add("id");
        //设置表的备份数
        options.setNumReplicas(1);
        //设置hash分区和数量
        options.addHashPartitions(parcols, 3);
        try {
            client.createTable("user", schema, options);
        } catch (KuduException e) {
            e.printStackTrace();
        } finally {

            client.close();
        }
    }
}
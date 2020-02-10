package com.atguigu.ddl;

import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Type;
import org.apache.kudu.client.AlterTableOptions;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;

public class AddColumn {

    public static ColumnSchema addColumn(String name, Type type, boolean iskey,boolean allowNull) {
        ColumnSchema.ColumnSchemaBuilder column = new ColumnSchema.ColumnSchemaBuilder(name, type);
        column.key(iskey);
        column.nullable(allowNull);
        return column.build();
    }

    public static void addColumn(KuduClient kuduClient,String tableName,String colName,Type type,boolean iskey,boolean allowNull){

        ColumnSchema.ColumnSchemaBuilder columnSchemaBuilder = new ColumnSchema.ColumnSchemaBuilder(colName, type);
        columnSchemaBuilder.key(iskey);
        columnSchemaBuilder.nullable(allowNull);
        ColumnSchema columnSchema = columnSchemaBuilder.build();

        AlterTableOptions alterTableOptions = new AlterTableOptions();
        alterTableOptions.addColumn(columnSchema);
        try {
            kuduClient.alterTable(tableName,alterTableOptions);
        } catch (KuduException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        // master地址
        String masteraddr = "hadoop103";
        // 创建kudu的数据库链接
        KuduClient client = new KuduClient.KuduClientBuilder(masteraddr).defaultSocketReadTimeoutMs(10000).build();
        AddColumn.addColumn(client,"user","age",Type.INT32,false,true);
    }
}

package com.atguigu.ddl;

import org.apache.kudu.client.*;

public class ScanTable {
    public static void main(String[] args) throws KuduException {
        // master地址
        final String masteraddr = "hadoop103";
        // 创建kudu的数据库链接
        KuduClient client = new KuduClient.KuduClientBuilder(masteraddr).defaultSocketReadTimeoutMs(100000).build();
        //打开kudu表
        KuduTable student = client.openTable("user");
        //创建scanner扫描
        KuduScanner scanner = client.newScannerBuilder(student).build();
        //遍历数据
        while (scanner.hasMoreRows()){
            for (RowResult rowResult : scanner.nextRows()) {
                Integer id = null;
                Integer age = null;
                String name = null;
                String city = null;
                if (!rowResult.isNull("id")){
                    id = rowResult.getInt("id");
                }
                if (!rowResult.isNull("name")){
                    name = rowResult.getString("name");
                }
                if (!rowResult.isNull("city")){
                    city = rowResult.getString("city");
                }if (!rowResult.isNull("age")){
                    age = rowResult.getInt("age");
                }
                System.out.println(id + "," + name + "," + city + "," + age);
            }
        }
        scanner.close();
        client.close();
    }
}

package com.atguigu.ddl;

import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;

public class DropTable {
    public static void main(String[] args) throws KuduException {
        String masterAddress = "hadoop103";
        KuduClient client = new KuduClient.KuduClientBuilder(masterAddress).defaultSocketReadTimeoutMs(6000).build();
        try {
            client.deleteTable("user");
        } catch (KuduException e) {
            e.printStackTrace();
        } finally {
            client.close();
        }
    }
}

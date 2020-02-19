package client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import handler.CanalHandler;

import java.net.InetSocketAddress;
import java.util.List;

public class CanalClient {


    public static void main(String[] args) {

        //canal连接器 [example是canal默认的消息队列名]
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress("hadoop102", 11111), "example", "", "");
        while (true) {

            // 从mysql的order_info表中抓取变化的数据
            canalConnector.connect();
            canalConnector.subscribe("gmall1111.order_info");
            Message message = canalConnector.get(100);
            int size = message.getEntries().size();

            if (size == 0) {
                System.out.println("没有数据！！休息5秒");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {

                // message(entrys) : entry(一条sql) : rowChange(一条sql) : rowDatasList(行集) = 1 : n : n :  n*m
                for (CanalEntry.Entry entry : message.getEntries()) {

                    //entry类型为数据集才处理
                    if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {

                        CanalEntry.RowChange rowChange = null;
                        try {

                            //entry需要被反序列化解析
                            rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
                        } catch (InvalidProtocolBufferException e) {
                            e.printStackTrace();
                        }
                        String tableName = entry.getHeader().getTableName();// 表名
                        CanalEntry.EventType eventType = rowChange.getEventType();//insert update delete？
                        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();//行集  数据

                        //将变化的数据发送到kafka
                        CanalHandler.handle(tableName, eventType, rowDatasList);
                    }
                }
            }
        }
    }
}

package handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;

import com.atguigu.gmall1111.commom.constant.GmallConstant;
import com.google.common.base.CaseFormat;
import util.MyKafkaSender;


import java.util.List;


public class CanalHandler {

    /**
     * 将order_info表中新增以及变化的数据发送到kafka
     * @param tableName
     * @param eventType
     * @param rowDataList
     */
    public static void  handle(String tableName , CanalEntry.EventType eventType, List<CanalEntry.RowData> rowDataList){


        if("order_info".equals(tableName)&& CanalEntry.EventType.INSERT==eventType){
            //遍历行集
            for (CanalEntry.RowData rowData : rowDataList) {

                //获取列集
                List<CanalEntry.Column> columnsList = rowData.getAfterColumnsList();

                //将一条记录封装为一条josn
                JSONObject jsonObject = new JSONObject();
                for (CanalEntry.Column column : columnsList) {
                    System.out.println(column.getName()+"------"+column.getValue());
                    String columnName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column.getName());
                    jsonObject.put(columnName,column.getValue());
                }
                //发送到kafka
                MyKafkaSender.send(GmallConstant.KAFKA_TOPIC_ORDER,jsonObject.toJSONString());
            }
        }
    }
}

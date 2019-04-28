package com.atguigu.udtf;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * 将et中的json数组拆分
 */

class EventJsonUDTF  extends GenericUDTF {

    @Override
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {

        ArrayList<String> fieldNames = new ArrayList<String>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>();

        fieldNames.add("event_name");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldNames.add("event_json");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    public void process(Object[] objects) throws HiveException {

        //取值
        String etStr = objects[0].toString();
        if (StringUtils.isBlank(etStr)) {
            return;
        }
        try {
            //转换为json数组
            JSONArray jsonArray = new JSONArray(etStr);

            //遍历
            for (int i = 0; i < jsonArray.length(); i++) {

                String[] eventStr = new String[2];
                JSONObject eachEventJsonObject = jsonArray.getJSONObject(i);

                //封装事件名
                eventStr[0] = eachEventJsonObject.getString("en");
                //封装事件
                eventStr[1] = eachEventJsonObject.toString();

                //返回
                forward(eventStr);
                //打印
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void close() throws HiveException {

    }
}
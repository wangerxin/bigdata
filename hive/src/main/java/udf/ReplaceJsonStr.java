

package udf;


import org.apache.hadoop.hive.ql.exec.UDF;
import org.mortbay.util.ajax.JSON;

import java.util.HashMap;
import java.util.Map;


public class ReplaceJsonStr extends UDF {

    public static String evaluate(final String jsonStr,String replaceStr) {

        if (jsonStr == null || replaceStr == null) {
            return null;
        }

        Map<String,String> jsonMap = (Map) JSON.parse(jsonStr);
        HashMap<String, String> map = new HashMap<>();

        //去除
        for (Map.Entry<String, String> entry : jsonMap.entrySet()) {

            String key=entry.getKey();
            if (key.startsWith(replaceStr)){
                map.put(key.substring(1),entry.getValue());
            }else {
                map.put(key,entry.getValue());
            }
        }

        return JSON.toString(map);
    }

}
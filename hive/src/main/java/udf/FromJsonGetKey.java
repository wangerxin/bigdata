package udf;

import com.alibaba.fastjson.JSON;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.List;
import java.util.Map;

public class FromJsonGetKey extends UDF {

    /**
     * 步骤：1.json转map 2.从map拿到key
     * @param json
     * @return
     */
    public  String evaluate(String json) {

        Map<String, List<Integer>> map = (Map<String, List<Integer>>) JSON.parse(json);
        String scidStr = "";
        for (Map.Entry entry : map.entrySet()) {
            String scid = (String) entry.getKey();
            scidStr = scidStr + scid + ",";
        }
        String result = scidStr.substring(0, scidStr.length() - 1);
        return result;
    }
}

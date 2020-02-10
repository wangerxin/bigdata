package com.atguigu.udf;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONException;
import org.json.JSONObject;

 public class BaseFieldUDF  extends UDF {

    //1552649242849|{"cm":{"ln":"-116.7","sv":"V2.0.1","os":"8.0.2","g":"ZIQ9FW6O@gmail.com","mid":"m751","nw":"3G","l":"pt","vc":"16","hw":"640*1136","ar":"MX","uid":"u506","t":"1552570650547","la":"-23.2","md":"sumsung-0","vn":"1.0.4","ba":"Sumsung","sr":"O"},"ap":"gmall","et":[{"ett":"1552582319640","en":"start","kv":{"entry":"5","loading_time":"19","action":"1","open_ad_type":"1","detail":"433"}}]}   2019-03-15

    public String evaluate(String log, String keys) throws JSONException {

        //切割
        String[] split = log.trim().split("\\|");
        //判断
        if (split.length != 2) {
            return "";
        }

        if (StringUtils.isBlank(split[1])){
            return "";
        }

        //创建cm_json对象
        JSONObject jsonObject = new JSONObject(split[1]);
        JSONObject cmJSONObject = jsonObject.getJSONObject("cm");

        //切割keys
        StringBuilder sb = new StringBuilder();
        String[] keyArray = keys.split(",");
        for (String key : keyArray) {
            if (cmJSONObject.has(key)){
                //添加cm下各个value
                sb.append(cmJSONObject.get(key)).append("\t");
            }else{
                sb.append("").append("\t");
            }
        }

        //添加事件和时间
        sb.append(jsonObject.get("et")).append("\t");
        sb.append(split[0]);

        return sb.toString();

    }
}

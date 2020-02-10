package com.atguigu.udtf;


import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.json.JSONException;
import com.atguigu.udf.BaseFieldUDF;

public class TestClass {

    public static void main(String[] args) throws JSONException, HiveException {

        //测试udf
        /*String data = "1541217850324|{\"cm\":{\"mid\":\"m7856\",\"uid\":\"u8739\",\"ln\":\"-74.8\",\"sv\":\"V2.2.2\",\"os\":\"8.1.3\",\"g\":\"P7XC9126@gmail.com\",\"nw\":\"3G\",\"l\":\"es\",\"vc\":\"6\",\"hw\":\"640*960\",\"ar\":\"MX\",\"t\":\"1541204134250\",\"la\":\"-31.7\",\"md\":\"huawei-17\",\"vn\":\"1.1.2\",\"sr\":\"O\",\"ba\":\"Huawei\"},\"ap\":\"weather\",\"et\":[{\"ett\":\"1541146624055\",\"en\":\"display\",\"kv\":{\"newsid\":\"n4195\",\"copyright\":\"ESPN\",\"content_provider\":\"CNN\",\"extend2\":\"5\",\"action\":\"2\",\"extend1\":\"2\",\"place\":\"3\",\"showtype\":\"2\",\"category\":\"72\",\"newstype\":\"5\"}},{\"ett\":\"1541213331817\",\"en\":\"loading\",\"kv\":{\"extend2\":\"\",\"loading_time\":\"15\",\"action\":\"3\",\"extend1\":\"\",\"type1\":\"\",\"type\":\"3\",\"loading_way\":\"1\"}},{\"ett\":\"1541126195645\",\"en\":\"ad\",\"kv\":{\"entry\":\"3\",\"show_style\":\"0\",\"action\":\"2\",\"detail\":\"325\",\"source\":\"4\",\"behavior\":\"2\",\"content\":\"1\",\"newstype\":\"5\"}},{\"ett\":\"1541202678812\",\"en\":\"notification\",\"kv\":{\"ap_time\":\"1541184614380\",\"action\":\"3\",\"type\":\"4\",\"content\":\"\"}},{\"ett\":\"1541194686688\",\"en\":\"active_background\",\"kv\":{\"active_source\":\"3\"}}]}";

        String cmKeys = "mid,uid,vc,vn,l,sr,os,ar,md,ba,sv,g,hw,nw,ln,la,t";
        String result = new BaseFieldUDF().evaluate(data, cmKeys);
        System.out.println(result);*/

        /* -----------------测试结果
       第一部分: 公共字段     m7856	u8739	6	1.1.2	es	O	8.1.3	MX	huawei-17	Huawei	V2.2.2	P7XC9126@gmail.com	640*960	3G	-74.8	-31.7	1541204134250
       第二部分: 事件数组    [
                              {"ett":"1541146624055","en":"display","kv":{"newsid":"n4195","copyright":"ESPN","content_provider":"CNN","extend2":"5","action":"2","extend1":"2","place":"3","showtype":"2","category":"72","newstype":"5"}},
                              {"ett":"1541213331817","en":"loading","kv":{"extend2":"","loading_time":"15","action":"3","extend1":"","type1":"","type":"3","loading_way":"1"}},
                              {"ett":"1541126195645","en":"ad","kv":{"entry":"3","show_style":"0","action":"2","detail":"325","source":"4","behavior":"2","content":"1","newstype":"5"}},
                              {"ett":"1541202678812","en":"notification","kv":{"ap_time":"1541184614380","action":"3","type":"4","content":""}},{"ett":"1541194686688","en":"active_background","kv":{"active_source":"3"}}
                              ]
       第三部分: 时间戳      1541217850324
        */

        //测试udtf
        String[] etArry = new String[10];
        etArry[0]="[{\"ett\":\"1541146624055\",\"en\":\"display\",\"kv\":{\"newsid\":\"n4195\",\"copyright\":\"ESPN\",\"content_provider\":\"CNN\",\"extend2\":\"5\",\"action\":\"2\",\"extend1\":\"2\",\"place\":\"3\",\"showtype\":\"2\",\"category\":\"72\",\"newstype\":\"5\"}},{\"ett\":\"1541213331817\",\"en\":\"loading\",\"kv\":{\"extend2\":\"\",\"loading_time\":\"15\",\"action\":\"3\",\"extend1\":\"\",\"type1\":\"\",\"type\":\"3\",\"loading_way\":\"1\"}},{\"ett\":\"1541126195645\",\"en\":\"ad\",\"kv\":{\"entry\":\"3\",\"show_style\":\"0\",\"action\":\"2\",\"detail\":\"325\",\"source\":\"4\",\"behavior\":\"2\",\"content\":\"1\",\"newstype\":\"5\"}},{\"ett\":\"1541202678812\",\"en\":\"notification\",\"kv\":{\"ap_time\":\"1541184614380\",\"action\":\"3\",\"type\":\"4\",\"content\":\"\"}},{\"ett\":\"1541194686688\",\"en\":\"active_background\",\"kv\":{\"active_source\":\"3\"}}]\t";

        EventJsonUDTF eventJsonUDTF = new EventJsonUDTF();
        eventJsonUDTF.process(etArry);

    }
}

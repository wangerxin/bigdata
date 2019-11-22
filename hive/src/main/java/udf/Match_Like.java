package udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.ArrayList;
import java.util.List;

public class Match_Like extends UDF {

    /**
     * 字符串与正则的匹配度
     *
     * @param str
     * @param reg
     * @param split
     * @return
     * 案例1：str="a,z,b,c,d" reg="a,b,c,d" return 4 完全匹配abcd
     * 案例2：str="a,z,b" reg="a,b,c,d" return 2 匹配ab
     */
    public static int evaluate(String str, String reg, String split) {
        //.*a.*b.*c.*
        String[] regArr = reg.split(split);
        String anyReg = ".*";
        String newReg = "";

        //从完整匹配到不完整匹配
        int regLength = regArr.length;
        for (int i = regLength; i > 0; i--) {
            newReg = getReg(regArr, i, anyReg);
            if (str.matches(newReg)) {
                return i; //
            }
        }
        return 0;
    }

    /**
     * 拼接数组前n个元素
     * @param strArr
     * @param before
     * @param separator
     * @return
     */
    public static String getReg(String[] strArr, int before, String separator) {

        //校验参数
        if (strArr == null || before > strArr.length) {
            return null;
        }

        //拼接正则
        String reg = separator;
        for (int i = 0; i < before; i++) {
            reg = reg + strArr[i] + separator;
        }
        return reg;
    }


    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {

        //================测试正则================
        String[] strArr = {"aaa", "bbb", "ccc"};
        String separator = ".*";
        //System.out.println(getReg(strArr, 1, separator));

        //测试evaluate
        Match_Like match_like = new Match_Like();
        String str = "b,c,d,a,c,b,c";
        String reg = "a,b,c";
        String split = ",";

        for (int i = 0; i < 1000000; i++) {
            match_like.evaluate(str,reg,",");
        }
    }

}

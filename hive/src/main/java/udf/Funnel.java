package udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.HashMap;
import java.util.Map;

public class Funnel extends UDF{

    public static void main(String[] argvs){

        String userPath = "B-C-A";
        String order = "A-B-C";
        String[] path = userPath.split("-");
        System.out.println(evaluate(userPath, order, "-"));

    }

    /**
     * 功能：实现顺序可间隔漏斗
     * @param userPath  用户实际路径  A-B-C-B-C
     * @param order     期望路径      A-B-C
     * @param separator 路径分隔符    -
     * @return          用户最深路径  3
     */
    public static int evaluate(String userPath,String order,String separator){

        /**
         * 1 切割参数，得到pathArr,orderArr
         * 2 创建pathCountMap，pathLastPathMap
         * 3 遍历pathArr，order1直接+1，order2先判断order1是否更大，更大则+1，以此类推
         * 4 反向遍历orderArr，取出orderCountMap里面的值，大于1就返回orderArr角标
         */

        //切割参数
        String[] userPathArr = userPath.split(separator);
        String[] orderArr = order.split(separator);
        String endPath = orderArr[orderArr.length-1];

        // 用于存储用户有效步骤，以及有效步骤的次数，例如 A:1 B:2 C:2
        Map<String, Integer> pathCountMap = new HashMap<>();
        for (int i = 0; i < orderArr.length; i++) {
            pathCountMap.put(orderArr[i],0);
        }

        // 用于存储期望路径的上一路径，例如 A:null B:A C:B
        Map<String, String> pathAndLastPathMap = new HashMap<>();
        for (int i = 0; i < orderArr.length; i++) {
            if (i != 0){
                pathAndLastPathMap.put(orderArr[i],orderArr[i-1]);
            }else {
                pathAndLastPathMap.put(orderArr[i],null);
            }
        }

        // 如果是A，pathCountMap+1
        // 如果是B，从pathAndLastPathMap查到B->A，然后从pathCountMap中查到Acount，如果Acount>Bcount则B+1
        for (String path : userPathArr) {

            String lastPath = pathAndLastPathMap.get(path);
            if (lastPath != null){
                if(pathCountMap.get(lastPath)>pathCountMap.get(path)){
                    pathCountMap.put(path,pathCountMap.get(path)+1);
                    if (path.equals(endPath)){
                        return orderArr.length;
                    }
                }
            }else {
                pathCountMap.put(path,pathCountMap.get(path)+1);
            }
        }

        for (int i = orderArr.length-2; i >= 0; i--) {

            if (pathCountMap.get(orderArr[i])>0){
                return (i+1);
            }
        }
        return 0;
    }
}

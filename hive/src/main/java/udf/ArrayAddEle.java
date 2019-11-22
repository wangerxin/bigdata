package udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayAddEle extends UDF {

    public static List<String> evaluate(List<String> list, String element) {

        List<String> newList = new ArrayList<String>();

        if (list == null) {
            newList.add(element);
            return newList;
        } else if (list.size()==1 && list.get(0).length()== 0) {
            newList.add(element);
            return newList;
        } else {
            list.add(element);
            return list;
        }
    }

    /*public static void main(String[] args) {

        System.out.println("======1=======");
        System.out.println(Arrays.toString(evaluate(null, "1").toArray()));

        System.out.println("======2=======");
        List<String> list=new ArrayList();
        list.add("");
        System.out.println(Arrays.toString(evaluate(list,"2").toArray()));

        System.out.println("======3=======");
        List<String> list2=new ArrayList();
        list2.add("test");
        System.out.println(Arrays.toString(evaluate(list2,"3").toArray()));

    }*/
}

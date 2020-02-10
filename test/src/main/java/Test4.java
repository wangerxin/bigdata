import java.util.Arrays;
import java.util.Collections;

public class Test4 {

    public static void main(String[] args) {

        //测试结果: a=b=c !=d
        String a = "abc";
        String b = "abc";
        String c = "ab" + "c";
        String d = new String("abc");

        //测试substring
        String e = "abcde";
        System.out.println(e.substring(1));//bcde
        System.out.println(e.substring(1, 3));//bc

        //测试浮点数相等
        float f = 1.111111111111112f;
        float g = 1.111111111111113f;
        System.out.println(f == g);
        System.out.println(Math.abs(g - f) == 0);

        //数组排序
        String h = "数字1,数字3,数字2";
        String[] splitArr = h.split(",");
        Arrays.sort(splitArr);
        System.out.println(Arrays.toString(splitArr));

        //数据类型
        short short1 = 1;
        int int1 = 1;

        //hash
        System.out.println("a".hashCode() == "a".hashCode());

        //拆箱
        Integer i1 = 127;
        Integer i2 = 127;
        Integer i3 = new Integer(127);
        Integer i4 = new Integer(127);
        System.out.println(i1==i2);
        System.out.println(i2==i3);
        System.out.println(i3==i4);

    }

}


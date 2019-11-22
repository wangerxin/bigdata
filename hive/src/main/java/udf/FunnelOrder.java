package udf;

public class FunnelOrder {

    public static void main(String[] argvs){
        String userPath = "A,B,C,A,B,C,D,D,E"; //用户实际完成路径
        String[] path = userPath.split(",");
        // 创建4个标量，用来统计有用的ABCD个数
        int i1 = 0, i2 = 0, i3 = 0, i4 = 0 , i5=0;
        //A>=B>=C>=D 的数量.所有用户完成的路径元素,遍历.拿到当前的元素.比如B,看数量是否小于A,如果小,说明之前计数了A(B的下标一定大于A)以此类推
        for (int j = 0; j < path.length; j++)
        {
            switch (path[j])
            {
                case "A":
                    i1++;
                    break;
                case "B":
                    if (i2 < i1)
                    {
                        i2++;
                    }
                    break;
                case "C":
                    if (i3 < i2)
                    {
                        i3++;
                    }
                    break;
                case "D":
                    if (i4 < i3)
                    {
                        i4++;
                    }
                    break;
                case "E":
                    if (i5 < i4)
                    {
                        i5++;
                    }
                    break;
            }
        }
        System.out.println("完成有效的A次数："+i1);
        System.out.println("完成有效的B次数："+i2);
        System.out.println("完成有效的C次数："+i3);
        System.out.println("完成有效的D次数："+i4);
        System.out.println("完成有效的E次数："+i5);

        System.out.println("单独完成有效的A次数："+(i1-i2));
        System.out.println("单独完成有效的AB次数："+(i2-i3));
        System.out.println("单独完成有效的ABC次数："+(i3-i4));
        System.out.println("单独完成有效的ABCD次数："+(i4-i5));
        System.out.println("单独完成有效的ABCDE次数："+i5);

    }
}

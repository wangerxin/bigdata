package designMode;

/**
 * 懒汉式:
 *
 */
class Single {

    // 方法一: 双重检查
    /*private static Single single;
    private Single(){}

    public static Single getSingle(){

        if (single ==null){
            synchronized (Single.class){
                if (single == null){
                    single = new Single();
                    return single;
                }
            }
        }
        return single;
    }*/

    // 方法二 : 静态内部类
    private Single(){}
    static class InnerSingle{
        private static Single single = new Single();
    }

    public static Single getSingle(){

        return InnerSingle.single;
    }



    public static void main(String[] args) {

        Single single1 = Single.getSingle();
        Single single2 = Single.getSingle();

        System.out.println(single1==single2);
    }

}

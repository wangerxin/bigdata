package juc181111;


@FunctionalInterface
interface Foo
{
    public int add(int x, int y);
}

/**
 * @auther zzyy
 *lambda Express
 *1 使用lambda Express的前提，这个接口必须首先是一个函数式接口（一个方法）
 *2 小口诀
 *   拷贝小括号,写死右箭头，落地大括号
 */
public class LambdaExpressDemo02
{
    public static void main(String[] args)
    {
        /*Foo foo = new Foo()
        {
            @Override
            public void sayHello()
            {
                System.out.println("*******hello 181111");
            }

            @Override
            public int add(int x, int y)
            {
                return 0;
            }
        };
        foo.sayHello();*/

        Foo foo = (x ,y) -> {
            System.out.println("*****come in here");
            return x + y;
        };
        System.out.println(foo.add(3, 15));

    }
}

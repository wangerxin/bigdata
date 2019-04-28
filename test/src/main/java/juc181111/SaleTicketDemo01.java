package juc181111;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Ticket // 资源类 = 变量+ 方法
{
    private int number = 30;
    private Lock lock = new ReentrantLock();//可重入锁

    public void sale()
    {
        lock.lock();
        try
        {
            if(number > 0)
            {
                System.out.println(Thread.currentThread().getName()+"\t 卖出第："+(number--)+"\t 还剩下："+number);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}


/**
 * @auther zzyy
 *
 * 题目：三个售票员         卖出          30张票
 * 如何编写企业级的多线程程序的模板+讨论
 *
 * 1） implements Runnable
 * 2)  匿名内部类
 * 3)  Lambda Express
 *
 * 1 在高内聚低耦合的前提下，线程       操作(某一个具体的业务方法，实例方法)        资源类
 */
public class SaleTicketDemo01
{
    public static void main(String[] args)//主线程，一切程序的入口
    {
        Ticket ticket = new Ticket();

        new Thread(() -> {for (int i = 1; i <=40; i++) ticket.sale();},"A").start();
        new Thread(() -> {for (int i = 1; i <=40; i++) ticket.sale();},"B").start();
        new Thread(() -> {for (int i = 1; i <=40; i++) ticket.sale();},"C").start();



    }
}






  /*new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 1; i <=40; i++)
                {
                    ticket.sale();
                }
            }
        }, "A").start();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 1; i <=40; i++)
                {
                    ticket.sale();
                }
            }
        }, "B").start();
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 1; i <=40; i++)
                {
                    ticket.sale();
                }
            }
        }, "C").start();*/
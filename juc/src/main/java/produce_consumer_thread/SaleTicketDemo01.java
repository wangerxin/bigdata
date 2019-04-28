package produce_consumer_thread;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程卖30张票
 */
class Ticket
{
    // 共享资源
    private int number = 30;
    // 锁
    private Lock lock = new ReentrantLock();

    // 卖票
    public void sale()
    {
        // 1.上锁, 使线程安全
        //lock.lock();

        //2.业务
        try
        {
            if(number > 0)
            {
                System.out.println(Thread.currentThread().getName()+"\t 卖出第："+(number--)+"\t 还剩下："+number);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            //3.释放锁
            //lock.unlock();
        }
    }
}



// 口诀: 线程操作资源类
public class SaleTicketDemo01
{
    public static void main(String[] args)//主线程，一切程序的入口
    {
        //资源类
        Ticket ticket = new Ticket();

        // 线程操作资源类
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
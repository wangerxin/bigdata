package produce_consumer_thread;


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 需求: 并发时, 唤醒指定的线程
 */
class ShareData
{
    // 标记
    private int flag = 1;// 1 : A 2 : B 3 : C
    // 锁与条件变量
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();

    public void print1_3()
    {
        lock.lock();
        try
        {
            //1 判断
            while(flag != 1)
            {
                c1.await();
            }
            //2 干活
            for (int i = 1; i <=3 ;i++)
            {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            //3 通知
            flag = 2;
            c2.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void print4_6()
    {
        lock.lock();
        try
        {
            //1 判断
            while(flag != 2)
            {
                c2.await();
            }
            //2 干活
            for (int i =4; i <=6 ;i++)
            {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            //3 通知
            flag = 3;
            c3.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public void print7_9()
    {
        lock.lock();
        try
        {
            //1 判断
            while(flag != 3)
            {
                c3.await();
            }
            //2 干活
            for (int i = 7; i <=9 ;i++)
            {
                System.out.println(Thread.currentThread().getName()+"\t"+i);
            }
            //3 通知
            flag = 1;
            c1.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

/**
 * @auther zzyy
 * @create 2019-04-13 13:10
 * 备注：多线程之间按顺序调用，实现A->B->C 然后继续A->B->C  .....
 */
public class SignalDemo5
{
    public static void main(String[] args)
    {
        ShareData shareData = new ShareData();

        new Thread(() -> {
            for (int i = 1; i <=5; i++)
            {
                shareData.print1_3();
            }
        },"A").start();
        new Thread(() -> {
            for (int i = 1; i <=5; i++)
            {
                shareData.print4_6();
            }
        },"B").start();
        new Thread(() -> {
            for (int i = 1; i <=5; i++)
            {
                shareData.print7_9();
            }
        },"C").start();

    }
}

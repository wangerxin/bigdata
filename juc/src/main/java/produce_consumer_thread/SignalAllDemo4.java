package produce_consumer_thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**考点: 并发时,广播唤醒机制
 * 题目：现在多个线程，可以操作初始值为零的一个变量，
 * 实现一个线程对该变量加1，一个线程对该变量减1，
 * 实现交替，来10轮，变量初始值为零。
 */
public class SignalAllDemo4 {

    //初始值
    private int number = 0;

    //准备锁与条件变量
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    // 加1操作
    public void add() {

        //1.上锁
        lock.lock();
        try {

            //2.什么条件不干活
            while (number != 0) {
                condition.await();
            }

            //3.开始干活
            number++;
            System.out.println(Thread.currentThread().getName() + "number=" + number);

            //4.告诉所有等待线程, 我干好了
            condition.signalAll();

        } catch (Exception e) {
            e.getLocalizedMessage();
        } finally {

            //5. 解锁
            lock.unlock();
        }
    }

    // 减1操作
    public void reduce() {

        //1.上锁
        lock.lock();
        try {

            //2.什么条件不干活
            while (number != 1) {
                condition.await();
            }

            //3.开始干活
            number--;
            System.out.println(Thread.currentThread().getName() + "number=" + number);

            //4.告诉所有等待线程, 我干好了
            condition.signalAll();

        } catch (Exception e) {
            e.getLocalizedMessage();
        } finally {

            //5. 解锁
            lock.unlock();
        }
    }
}

class Test{
    public static void main(String[] args) {

        //资源类
        SignalAllDemo4 resourceClass = new SignalAllDemo4();

        //线程操作资源类
        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                resourceClass.add();
            }
        },"A").start();

        new Thread(()->{
            for (int i = 0; i < 10; i++) {
                resourceClass.reduce();
            }
        },"B").start();

    }
}

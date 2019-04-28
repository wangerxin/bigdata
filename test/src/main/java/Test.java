import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

    public static void main(String[] args) {

        final Ticket ticket = new Ticket();
        // 线程操作共享资源
        new Thread(()->{for (int i = 0; i <30 ; i++) ticket.sellTicket(); },"A").start();
        new Thread(()->{for (int i = 0; i <30 ; i++) ticket.sellTicket(); },"B").start();



    }
}

class Ticket {

    private int ticketNum = 30;
    Lock lock = new ReentrantLock();

    public void sellTicket() {

        lock.lock();
        try {
            while (ticketNum > 0) {
                System.out.println(Thread.currentThread().getName() + ": 还剩下" + ticketNum-- + "张票");
            }
        }finally {
            lock.unlock();
        }
    }
}

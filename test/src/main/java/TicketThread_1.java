import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class TicketThread_1 extends Thread {

    String name;
    static int ticket = 200;
    Lock lock = new ReentrantLock();

    public TicketThread_1() {
    }

    public TicketThread_1(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        while (true) {
            if (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + "还有余票数：" + ticket--);
            } else {
                return;
            }
        }
    }
}

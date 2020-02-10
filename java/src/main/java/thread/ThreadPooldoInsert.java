package thread;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程方式,向mysql中插入数据
 */
public class ThreadPooldoInsert {
    public static void main(String[] args) {
        long startTimes = System.currentTimeMillis();
        String[] names = new String[]{"LXL", "MQJ", "JOE", "JON", "JACK", "LILY", "LUCY", "NOB", "FDSE", "GTX"};
        int threadCount = 10;
        int total = 1000;
        int every = total / threadCount;//10000
        final CountDownLatch latch = new CountDownLatch(threadCount);

        //使用线程池可以减少创建和销毁线程的次数，每个工作线程都可以重复使用。
        ExecutorService executor = Executors.newFixedThreadPool(10);
        //开启10个线程执行任务
        for (int i = 0; i < threadCount; i++) {
            executor.submit(new Worker(latch, names[i],i * every,(i + 1) * every));
        }

        /*
        普通方式开始10个线程
        for (int i = 0; i < threadCount; i++) {
            new Thread(new Worker(latch, names[i], i * every, (i + 1) * every)).start();
        }*/

        //main线程
        try {
            //main线程一直处于等待状态,除非其他所有线程执行结束
            latch.await();

            //计算进程执行时间
            long endTimes = System.currentTimeMillis();
            System.out.println("所有线程执行完毕:" + (endTimes - startTimes));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


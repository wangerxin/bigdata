package thread;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * 向mysql表中插入数据
 */
class Worker implements Runnable{

    static String sql = "INSERT INTO `tb_demo` (`name`,`cre_date`) VALUES (?, ?);";
    int start = 0;
    int end = 0;
    String name = "";
    CountDownLatch latch;


    public Worker(CountDownLatch latch,String name, int start,int end){
        this.start = start;
        this.end = end;
        this.name = name;
        this.latch = latch;
    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            System.out.println("线程" + Thread.currentThread().getName()+ "正在执行。。");
            Object[] params = new Object[] { name + i, new Date() };
            JdbcUtils.insert(sql, params);
        }

        // 表示当前线程运行结束
        // https://www.jianshu.com/p/965ffb474d89
        latch.countDown();
    }

}
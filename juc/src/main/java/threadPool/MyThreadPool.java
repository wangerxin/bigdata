package threadPool;

import java.util.concurrent.ThreadPoolExecutor;

class Number {
    private int number;

    public int getNumber() {
        return number;
    }

    public int add() {
        number += 1;
        return number;
    }
}

class MyThreadPool {

    public static void main(String[] args) throws InterruptedException {

        Number number = new Number();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    number.add();
                }
            }
        }, "A").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    number.add();
                }
            }
        }, "B").start();

        Thread.sleep(2000);
        System.out.println(number.getNumber());
    }
}

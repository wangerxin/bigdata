public class Lock {

    public static void main(String[] args) {

        Phone phone = new Phone();

        new Thread(() -> { phone.qq(); }, "qq").start();
        new Thread(() -> { phone.weixin(); }, "qq").start();
       // new Thread(() -> { phone.call(); }, "qq").start();

    }
}

class Phone {

    public synchronized void qq() {

        try {
            System.out.println("qq");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void weixin() {
        System.out.println("微信");
    }

    public void call() {
        System.out.println("打电话");
    }

}


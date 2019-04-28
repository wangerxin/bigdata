public class TicketThread_2 implements Runnable {

    String name;
    int ticket = 30;

    public TicketThread_2() {
    }

    public TicketThread_2(String name) {
        this.name = name;
    }

    public void run() {

        while (ticket >= 1){
            System.out.println(Thread.currentThread().getName()+":还剩下"+ticket--);
        }
    }
}

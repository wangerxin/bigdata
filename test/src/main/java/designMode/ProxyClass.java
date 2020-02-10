package designMode;

public class ProxyClass implements Work {

    Work work;

    public ProxyClass(Work work) {
        this.work = work;
    }

    @Override
    public void work() {

        System.out.println("开始静态代理");
        work.work();
    }
}

class Test{
    public static void main(String[] args) {

        RealClass realClass = new RealClass();

        ProxyClass proxyClass = new ProxyClass(realClass);
        proxyClass.work();
    }
}

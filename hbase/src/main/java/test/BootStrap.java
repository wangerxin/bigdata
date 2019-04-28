package test;

public class BootStrap {
    public static void main(String[] args) {
        Student stu1 = new Student();
        stu1.name="atguigu";
        Student stu2 = new Student();
        stu2.name="atguigu";
        System.out.println(stu1.name==stu2.name);
    }
}

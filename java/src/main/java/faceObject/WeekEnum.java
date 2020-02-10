package faceObject;

/**
 * 枚举是什么: 等价于public static final修饰的常量,据说更加安全.
 * 本章内容: 1.枚举的创建
 *           2.枚举的使用
 *
 */
public enum WeekEnum {

    //1.创建枚举类对象
    //2.使用
    //MONDAY是WeekEnum类的对象,monday是实际的值
    MONDAY("monday"),SUNDAY("sunday");

    private String Week;
    WeekEnum(String week) {
        Week = week;
    }


    public static void main(String[] args) {
        System.out.println(WeekEnum.MONDAY);
    }
}

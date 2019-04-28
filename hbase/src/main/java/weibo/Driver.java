package weibo;

import java.io.IOException;

public class Driver {
    public static void main(String[] args) throws IOException {

        //1.创建命名空间
        //Util.createNameSpace("weibo");

        //2.创建 微博内容表 用户关系表 收信箱表
        //Util.createTable("weibo:content",1,"info");
        //Util.createTable("weibo:relations",1,"attents","fans");
//        Util.createTable("weibo:box",3,"info");

        //3.测试发微博
        //DAO.publish("1","2_content");

        //5.测试取消关注
        //DAO.cancelAttents("21","1");

        //6测试 查询某个user的所有微博
        DAO.userGetContent("1");
    }
}

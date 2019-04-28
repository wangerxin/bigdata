package ddl_dml.ddl;

import java.io.IOException;

public class TestDDL {
    public static void main(String[] args) {

        try {

            //0.测试创建命名空间
            HbaseDDL.createNameSpace("test_createNS");

            //1.测试表是否存在
            /*boolean isExist = HbaseDDL.isTableExist("st");
            System.out.println("表存在?"+isExist);*/

            //2.测试创建表
            //HbaseDDL.createTable("test_createTable");

            //3.测试删除表
            //HbaseDDL.deleteTable("test_createTable");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package dml;

import java.io.IOException;

public class TestDML {
    public static void main(String[] args) throws IOException {

        //测试添加一个cell级别的数据
        //HbaseDML.addRowData("student", "1003", "info", "sex", "男");

        //测试删除多行数据,(rowkeys级别的数据)
        //HbaseDML.deleteRows("student","1003","1004");

        //测试查询整张表
        HbaseDML.scanTable("student");

        //测试查询一行数据
        //HbaseDML.getData("student","1003","info","name");

    }
}

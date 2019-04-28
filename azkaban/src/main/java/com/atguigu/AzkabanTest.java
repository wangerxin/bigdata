package com.atguigu;

import java.io.FileOutputStream;
import java.io.IOException;

public class AzkabanTest {
    public void run() throws Exception {
        // 根据需求编写具体代码
        FileOutputStream fos = new FileOutputStream("/opt/module/azkaban/output.txt");
        fos.write("this is a java progress".getBytes());
        fos.close();
    }

    public static void main(String[] args) throws Exception {
        AzkabanTest azkabanTest = new AzkabanTest();
        azkabanTest.run();

    }
}

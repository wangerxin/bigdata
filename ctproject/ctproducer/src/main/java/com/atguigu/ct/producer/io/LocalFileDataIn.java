package com.atguigu.ct.producer.io;

import com.atguigu.ct.common.bean.DataIn;

import java.io.*;

/**
 * 数据源是本地文件
 */
public class LocalFileDataIn implements DataIn {

    private BufferedReader reader;

    /**
     * 无参构造
     */
    public LocalFileDataIn(){}

    /**
     * 有参构造
     * @param path
     * @throws FileNotFoundException
     */
    public LocalFileDataIn(String path) throws FileNotFoundException {
        reader=new BufferedReader(new InputStreamReader(new FileInputStream(path)));
    }

    /**
     * 读取本地文件
     * @return
     */
    public Object read() throws IOException {

        String line;
        while ((line=reader.readLine())!=null){

        }
        return null;
    }

    /**
     * 关闭资源
     * @throws IOException
     */
    public void close() throws IOException {

    }
}

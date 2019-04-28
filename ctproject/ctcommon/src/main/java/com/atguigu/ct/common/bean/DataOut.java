package com.atguigu.ct.common.bean;

import java.io.Closeable;
import java.io.IOException;

/**
 * 数据目的地接口
 */
public interface DataOut extends Closeable{
    /**
     * 写出数据
     * @param o
     */
    void write(Object o);

    /**
     * 关闭资源
     */
    void close() throws IOException;
}

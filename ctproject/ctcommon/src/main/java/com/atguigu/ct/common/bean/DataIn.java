package com.atguigu.ct.common.bean;

import java.io.Closeable;
import java.io.IOException;

/**
 * 数据源接口
 */
public interface DataIn extends Closeable{
    /**
     * 读取数据
     * @return
     */
    Object read() throws IOException;

    /**
     * 关闭资源
     * @throws IOException
     */
    void close() throws IOException;
}

package com.atguigu.ct.common.bean;

import java.io.Closeable;
import java.io.IOException;

/**
 * 生产者接口
 */
public interface Producer extends Closeable {
    void setIn(DataIn in);
    void setOut(DataOut out);
    void produce();
    void close() throws IOException;
}

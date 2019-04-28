package com.atguigu.ct.producer.bean;

import com.atguigu.ct.common.bean.DataIn;
import com.atguigu.ct.common.bean.DataOut;
import com.atguigu.ct.common.bean.Producer;

import java.io.IOException;

/**
 * 本地文件生产者
 */

public class LocalFileProducer implements Producer {
    private DataIn in;
    private DataOut out;

    /**
     * 设置数据源
     * @param in
     */
    public void setIn(DataIn in) {
        this.in=in;
    }

    /**
     * 设置数据目的地
     * @param out
     */
    public void setOut(DataOut out) {

        this.out=out;
    }

    /**
     * 生产数据
     */
    public void produce() {

        //
    }

    /**
     * 关闭资源
     * @throws IOException
     */
    public void close() throws IOException {

        in.close();
        out.close();
    }
}

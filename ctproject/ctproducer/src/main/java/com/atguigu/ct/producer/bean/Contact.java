package com.atguigu.ct.producer.bean;

import com.atguigu.ct.common.bean.Data;

public class Contact extends Data{

    private String tel;
    private String name;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

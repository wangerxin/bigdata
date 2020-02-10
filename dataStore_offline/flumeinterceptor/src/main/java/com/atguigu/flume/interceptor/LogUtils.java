package com.atguigu.flume.interceptor;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {

    private static Logger logger = LoggerFactory.getLogger(LogUtils.class);

    public static boolean validate(String body) {

        try {
            //1.切割
            String[] split = body.split("\\|");

            //2.长度是否合法,防止后面的越界
            if (split.length<2){
                return false;
            }

            //3.第一串是否为时间戳
            if (split[0].length() != 13 || !NumberUtils.isDigits(split[0].trim())){
                return false;
            }

            //4.第二串是否为json
            if (!split[1].trim().startsWith("{") || !split[1].trim().endsWith("}")){
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error:"+body);
        }

        return true;
    }

    public static void test(){

    }
}

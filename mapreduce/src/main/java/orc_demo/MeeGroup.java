package orc_demo;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class MeeGroup extends WritableComparator {

    //2.在构造方法中传入比较对象
    protected MeeGroup() {
        super(Mee.class, true); //不指定为true报异常
    }

    //3.重写比较方法,注意区别于WritableComparable接口的compareTO方法
    @Override
    public int compare(WritableComparable a, WritableComparable b) {

        Mee aBean = (Mee) a;
        Mee bBean = (Mee) b;

        if (aBean.getMsisdn_1().equals(bBean.getMsisdn_1()) &&
                aBean.getMsimsi_1().equals(bBean.getMsimsi_1()) &&
                aBean.getMsimei_1().equals(bBean.getMsimei_1()) &&
                aBean.getGeohash6().equals(bBean.getGeohash6())){
            return 0;
        }else if (aBean.getCapture_time() >= aBean.getCapture_time()){
            return 1;
        }else {
            return -1;
        }
    }
}

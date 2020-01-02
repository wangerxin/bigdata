package orc_demo;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Mee3 implements WritableComparable<Mee3>{

    /**
     2: msisdn_1
     3: msimsi_1
     4: msimei_1
     48: geohash6
     0: capture_time
     */
    private String msisdn_1;
    private String msimsi_1;
    private String msimei_1;
    private String geohash6;
    private Long capture_time;
    private Long startTime;
    private Long stayTime;

    public Mee3() {
        super();
    }

    public Mee3(String msisdn_1, String msimsi_1, String msimei_1, String geohash6, Long capture_time) {
        super();
        this.msisdn_1 = msisdn_1;
        this.msimsi_1 = msimsi_1;
        this.msimei_1 = msimei_1;
        this.geohash6 = geohash6;
        this.capture_time = capture_time;
    }

    public Mee3(String msisdn_1, String msimsi_1, String msimei_1, String geohash6, Long capture_time, Long startTime, Long stayTime) {
        this.msisdn_1 = msisdn_1;
        this.msimsi_1 = msimsi_1;
        this.msimei_1 = msimei_1;
        this.geohash6 = geohash6;
        this.capture_time = capture_time;
        this.startTime = startTime;
        this.stayTime = stayTime;
    }

    public String getMsisdn_1() {
        return msisdn_1;
    }

    public void setMsisdn_1(String msisdn_1) {
        this.msisdn_1 = msisdn_1;
    }

    public String getMsimsi_1() {
        return msimsi_1;
    }

    public void setMsimsi_1(String msimsi_1) {
        this.msimsi_1 = msimsi_1;
    }

    public String getMsimei_1() {
        return msimei_1;
    }

    public void setMsimei_1(String msimei_1) {
        this.msimei_1 = msimei_1;
    }

    public String getGeohash6() {
        return geohash6;
    }

    public void setGeohash6(String geohash6) {
        this.geohash6 = geohash6;
    }

    public Long getCapture_time() {
        return capture_time;
    }

    public void setCapture_time(Long capture_time) {
        this.capture_time = capture_time;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getStayTime() {
        return stayTime;
    }

    public void setStayTime(Long stayTime) {
        this.stayTime = stayTime;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(msisdn_1);
        dataOutput.writeUTF(msimsi_1);
        dataOutput.writeUTF(msimei_1);
        dataOutput.writeUTF(geohash6);
        dataOutput.writeLong(capture_time);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.msisdn_1 = dataInput.readUTF();
        this.msimsi_1 = dataInput.readUTF();
        this.msimei_1 = dataInput.readUTF();
        this.geohash6 = dataInput.readUTF();
        this.capture_time = dataInput.readLong();
    }

    @Override
    public int compareTo(Mee3 o) {
        return msisdn_1.compareTo(o.msisdn_1);
    }
}

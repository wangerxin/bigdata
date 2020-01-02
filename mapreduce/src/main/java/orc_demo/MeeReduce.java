package orc_demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.orc.TypeDescription;
import org.apache.orc.mapred.OrcStruct;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 1.schema
 * 2.OrcStruct
 * 3.pair.setFieldValue(0,value)
 * 4.context.write(NullWritable.get(),OrcStruct)
 */
public class MeeReduce extends Reducer<Mee,LongWritable,NullWritable,OrcStruct> {

    /**
     2: msisdn_1
     3: msimsi_1
     4: msimei_1
     48: geohash6
     0: capture_time
     */
    private static Log log= LogFactory.getLog(MeeReduce.class);
    private final NullWritable k = NullWritable.get();
    private TypeDescription table_schema = TypeDescription.fromString("struct<msisdn_1:string," +
                                                                                "msimsi_1:string," +
                                                                                "msimei_1:string," +
                                                                                "geohash6:string," +
                                                                                "capture_time:bigint," +
                                                                                "start_time:bigint," +
                                                                                "stay_time:bigint>");
    private OrcStruct v = (OrcStruct) OrcStruct.createValue(table_schema);

    // 封装orcStruct
    private Text msisdn_1 = new Text();
    private Text msimsi_1 = new Text();
    private Text msimei_1 = new Text();
    private Text geohash6 = new Text();
    private LongWritable captureTime = new LongWritable();
    private LongWritable startTime = new LongWritable();
    private LongWritable stayTime = new LongWritable();

    @Override
    protected void reduce(Mee key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {

        //构建OrcStruct
        msisdn_1.set(key.getMsisdn_1());
        msimsi_1.set(key.getMsimsi_1());
        msimei_1.set(key.getMsimei_1());
        geohash6.set(key.getGeohash6());
        captureTime.set(key.getCapture_time());
        v.setFieldValue("msisdn_1",msisdn_1);
        v.setFieldValue("msimsi_1",msimsi_1);
        v.setFieldValue("msimei_1",msimei_1);
        v.setFieldValue("geohash6",geohash6);
        v.setFieldValue("capture_time",captureTime);

        //对时间排序
        ArrayList<Long> timeList = new ArrayList<>();
        for (LongWritable time : values) {
            timeList.add(time.get());
        }
        Collections.sort(timeList);

        //根据一个小时切片
        long meeStartTime = timeList.get(0);
        long meeSubTime = 0;
        long splitCount = 0;
        int listLentgth = timeList.size();
        for (int i = 0; i < listLentgth; i++) {
            splitCount ++;
            meeSubTime = timeList.get(i) - meeStartTime;
            if (meeSubTime > 3600){
                if (splitCount >= 2){
                    startTime.set(meeStartTime);
                    stayTime.set(timeList.get(i - 1) - meeStartTime);
                    v.setFieldValue("start_time",startTime);
                    v.setFieldValue("stay_time",stayTime);
                    context.write(k,v);

                    //归零
                    meeStartTime = timeList.get(i);
                    splitCount = 0;

                    //如果是最后一个时间点
                    if (1 == listLentgth - 1){
                        startTime.set(meeStartTime);
                        stayTime.set(0);
                        v.setFieldValue("start_time",startTime);
                        v.setFieldValue("stay_time",stayTime);
                        context.write(k,v);
                    }
                }else {
                    // 一个时间切片只有一个时间点
                    startTime.set(meeStartTime);
                    stayTime.set(0);
                    v.setFieldValue("start_time",startTime);
                    v.setFieldValue("stay_time",stayTime);
                    context.write(k,v);

                    // 归零
                    meeStartTime = timeList.get(i);
                    splitCount = 0;

                    //如果这是最后一个时间点
                    if (i == listLentgth - 1){
                        startTime.set(meeStartTime);
                        stayTime.set(0);
                        v.setFieldValue("start_time",startTime);
                        v.setFieldValue("stay_time",stayTime);
                        context.write(k,v);
                    }
                }
            }else {
                // subtime < 3600

                //如果这是最后一个时间点
                if (1 == listLentgth - 1){
                    startTime.set(meeStartTime);
                    stayTime.set(timeList.get(i) - meeStartTime);
                    v.setFieldValue("start_time",startTime);
                    v.setFieldValue("stay_time",stayTime);
                    context.write(k,v);
                }else {
                    continue;
                }
            }
        }
    }
}

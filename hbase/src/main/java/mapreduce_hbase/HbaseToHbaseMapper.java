package mapreduce_hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class HbaseToHbaseMapper extends TableMapper<ImmutableBytesWritable, Put> {


    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

        //得到rowkey
        byte[] rowkey = key.get();

        //解析value
        Cell[] cells = value.rawCells();
        for (Cell cell : cells) {

            //封装put
            Put put = new Put(rowkey);
            put.add(cell);

            //写出
            context.write(key,put);
        }
    }
}

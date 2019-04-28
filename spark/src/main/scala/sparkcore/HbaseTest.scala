import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{Cell, CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{TableInputFormat, TableOutputFormat}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object HbaseTest {
  def main(args: Array[String]): Unit = {

    //初始化sparkContext
    val conf: SparkConf = new SparkConf().setMaster("local").setAppName("jdbc")
    val sc = new SparkContext(conf)

    //配置habse
    val hbaseConf: Configuration = HBaseConfiguration.create()
    hbaseConf.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104") //访问zookeeper
    hbaseConf.set(TableInputFormat.INPUT_TABLE, "student")

    //读取hbase
    val resultRDD: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(
      hbaseConf,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]
    )

    //解析
    resultRDD.foreach {
      case (rowkey, result) => {
        val cells: Array[Cell] = result.rawCells()
        cells.foreach {
          cell => {
            val rowkey: String = Bytes.toString(CellUtil.cloneRow(cell))
            val family: String = Bytes.toString(CellUtil.cloneFamily(cell))
            val qualifier: String = Bytes.toString(CellUtil.cloneQualifier(cell))
            val value: String = Bytes.toString(CellUtil.cloneValue(cell))

            println(s"$rowkey - $family - $qualifier - $value")
          }
        }
      }
    }

    //--------------------写入hbase----------------------------

    //1.配置连接hbase
    val hbaseConf2: Configuration = HBaseConfiguration.create()
    hbaseConf2.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104")
    hbaseConf2.set(TableOutputFormat.OUTPUT_TABLE, "student")

    //2.配置写出类型
    val job: Job = Job.getInstance(hbaseConf2)
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Put])

    //3.准备数据
    val rdd: RDD[(String, String, String)] = sc.makeRDD(Array(("1001", "zhangsan", "woman"), ("1002", "lisi", "man")))
    val habseRDD: RDD[(ImmutableBytesWritable, Put)] = rdd.map {
      case (rowkey, name, sex) => {
        val put = new Put(Bytes.toBytes(rowkey))
        put.addColumn(Bytes.toBytes("info"), Bytes.toBytes(name), Bytes.toBytes(sex))
        (new ImmutableBytesWritable(), put)
      }
    }
    //4.写出
    habseRDD.saveAsNewAPIHadoopDataset(job.getConfiguration)
  }
}
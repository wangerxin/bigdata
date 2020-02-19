package com.atguigu.utils

import java.util

import com.alibaba.fastjson.JSONObject
import org.apache.kudu.client.{KuduClient, KuduSession, KuduTable, PartialRow}
import org.apache.kudu.{ColumnSchema, Schema}

object KuduUtil {

  /**
    * 获取表的列名和列的类型
    *
    * @param kuduTable
    * @return
    */
  def getSchema(kuduTable: KuduTable): Map[String, String] = {
    val schema: Schema = kuduTable.getSchema
    val columnsIter: util.Iterator[ColumnSchema] = schema.getColumns.iterator()
    var tableColumnMap: Map[String, String] = Map()
    while (columnsIter.hasNext) {
      var column: ColumnSchema = columnsIter.next()
      var colName: String = column.getName
      var colType: String = column.getType.getName
      tableColumnMap += (colName -> colType)
    }
    tableColumnMap
  }

  /**
    * 将log中的数据封装在row
    *
    * @param row
    * @param logJson
    * @param tableColumnMap
    */
  def setRow(row: PartialRow, logJson: JSONObject, tableColumnMap: Map[String, String]): Unit = {
    val logIter: util.Iterator[util.Map.Entry[String, AnyRef]] = logJson.entrySet().iterator()
    while (logIter.hasNext) {
      var logEntry: util.Map.Entry[String, AnyRef] = logIter.next()
      var k: String = logEntry.getKey
      val v: String = logEntry.getValue.toString
      try {
        //如果日志中有这一列,则获取列的类型,如果没有则为""
        var columnType: String = tableColumnMap.getOrElse(k, "")
        import java.lang.{Boolean, Byte, Double, Float, Long, Short}
        import java.math.BigDecimal
        columnType match {
//        INT8(DataType.INT8, "int8"),
//        INT16(DataType.INT16, "int16"),
//        INT32(DataType.INT32, "int32"),
//        INT64(DataType.INT64, "int64"),
//        BINARY(DataType.BINARY, "binary"),
//        STRING(DataType.STRING, "string"),
//        BOOL(DataType.BOOL, "bool"),
//        FLOAT(DataType.FLOAT, "float"),
//        DOUBLE(DataType.DOUBLE, "double"),
//        UNIXTIME_MICROS(DataType.UNIXTIME_MICROS, "unixtime_micros"),
//        DECIMAL(Arrays.asList(DataType.DECIMAL32, DataType.DECIMAL64, DataType.DECIMAL128), "decimal");
          case "string" => row.addString(k, String.valueOf(v))
          case "int64" => row.addLong(k,Long.valueOf(v))
          case "int32" => row.addInt(k, Integer.valueOf(v))
          case "int16" => row.addShort(k,Short.valueOf(v))
          case "int8" => row.addByte(k,Byte.valueOf(v))
          case "double" => row.addDouble(k,Double.valueOf(v))
          case "float" => row.addFloat(k,Float.valueOf(v))
          case "decimal" => row.addDecimal(k,BigDecimal.valueOf(Long.valueOf(v)))
          case "bool" => row.addBoolean(k,Boolean.valueOf(v))
          case "" =>
        }
      } catch {
        case e: Exception => {
          e.printStackTrace()
          //todo 写入错误日志
        }
      }
    }
  }

  /**
    * 关闭资源
    * @param kuduSession
    * @param kuduClient
    */
  def close(kuduSession: KuduSession, kuduClient: KuduClient): Unit = {
    try {
      if (kuduSession != null) {
        kuduSession.close()
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
    try {
      if (kuduClient != null) {
        kuduClient.close()
      }
    } catch {
      case e: Exception => e.printStackTrace()
    }
  }
}

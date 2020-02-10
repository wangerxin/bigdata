package utils

import java.util

import org.apache.kudu.ColumnSchema
import org.apache.kudu.client.KuduTable

object KuduUtil {

  def getSchema(kuduTable: KuduTable) :Map[String,String] ={
    val schema = kuduTable.getSchema
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


}

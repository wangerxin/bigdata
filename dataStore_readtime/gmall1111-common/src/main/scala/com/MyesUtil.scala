package com

import java.util
import java.util.Objects

import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.core.{Bulk, BulkResult, Index}


object MyesUtil {

  //定义连接参数
  private val ES_HOST = "http://hadoop102"
  private val ES_HTTP_PORT = 9200
  private var factory:JestClientFactory = null

  /**
    * 获取es客户端
    * @return jestclient
    */
  def getClient: JestClient = {

    //创建工厂,从工厂中获取客户端
    if (factory == null) build()
    factory.getObject
  }

  /**
    * 关闭客户端
    */
  def close(client: JestClient): Unit = {
    if (!Objects.isNull(client)) try
      client.shutdownClient()
    catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }


  /**
    * 创建工厂,连接es
    */
  private def build(): Unit = {
    factory = new JestClientFactory
    factory.setHttpClientConfig(
      new HttpClientConfig.Builder(ES_HOST + ":" + ES_HTTP_PORT).multiThreaded(true)
        .maxTotalConnection(20) //连接总数
        .connTimeout(10000).readTimeout(10000).build)
  }

  /**
    * 批量写入es
    * @param indexName
    * @param docs
    */
  def insertEsBulk(indexName:String,docs:List[Any]): Unit ={

    //获取客户端
    val jest: JestClient = getClient

    //bulk封装数据: bulk=>bulkBuilder=>Index=>真实数据
    val bulkBuilder = new Bulk.Builder
    bulkBuilder.defaultIndex(indexName).defaultType("_doc")
    for (doc <- docs ) {
      val index: Index = new Index.Builder(doc).build()
      bulkBuilder.addAction(index)
    }

    //执行插入
    val items: util.List[BulkResult#BulkResultItem] = jest.execute(bulkBuilder.build()).getItems
    println("保存"+items.size()+"条数据")
    close(jest)
  }

  def insertData(indexName:String,docs:List[Any]): Unit ={

    //连接es
    val jest: JestClient = getClient
    //使用bulk封装数据
    val builder: Bulk.Builder = new Bulk.Builder
    //执行插入
  }

  def main(args: Array[String]): Unit = {
    // 单个document保存
    //     val jest: JestClient = getClient
    //     val movie= "{   \"name\":\"liulangdiqiu\",\n    \"movie_time\": 2.0\n}"
    //     val index: Index = new Index.Builder(movie).index("movie1111_index").`type`("_doc").build()
    //     jest.execute(index)
    //     close(jest)
    //批量保存
  }
}

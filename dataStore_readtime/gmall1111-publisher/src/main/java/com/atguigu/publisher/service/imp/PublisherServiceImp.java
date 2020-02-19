package com.atguigu.publisher.service.imp;

import com.atguigu.gmall1111.commom.constant.GmallConstant;
import com.atguigu.publisher.service.PublisherService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PublisherServiceImp implements PublisherService {

    @Autowired
    JestClient jestClient;


    @Override
    public int getDauTotal(String date) {

        int dauTotal = 0;

        //1.封装search
        //1.1 查询条件方法一: 使用字符串
        String query = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"filter\": {\n" +
                "        \"term\": {\n" +
                "          \"logDate\": \"" + date + "\"\n" +
                "        }\n" +
                "      \n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";
        //1.2查询条件方法二: 使用java API,但还是基于方法一的. 使用逆向思维编码,需要什么new什么
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("logDate", date);
        boolQueryBuilder.filter(termQueryBuilder);
        searchSourceBuilder.query(boolQueryBuilder);

        //1.3 使用构造器构造search
        Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(GmallConstant.ES_INDEX_DAU).addType("_doc").build();

        //2.执行search
        try {
            dauTotal = jestClient.execute(search).getTotal().intValue();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3. 返回结果
        return dauTotal;
    }

    @Override
    public Map getDauHours(String date) {

        //1.构建search
        //1.1方法一,使用字符串
        String query = "{\n" +
                "  \"query\": {\n" +
                "    \"bool\": {\n" +
                "      \"filter\": {\n" +
                "        \"term\": {\n" +
                "          \"logDate\": \""+date+"\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"aggs\": {\n" +
                "    \"groupby_logHour\": {\n" +
                "      \"terms\": {\n" +
                "        \"field\": \"logHour\",\n" +
                "        \"size\": 24\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n";

        Search search = new Search.Builder(query).addIndex(GmallConstant.ES_INDEX_DAU).addType("_doc").build();


        //执行search
        SearchResult searchResult = null;
        try {

            //应该判空,此处省略
            searchResult = jestClient.execute(search);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //分析结果
        List<TermsAggregation.Entry> buckets = searchResult.getAggregations().getTermsAggregation("groupby_logHour").getBuckets();
        HashMap<String, Long> hashMap = new HashMap<>();
        for (TermsAggregation.Entry bucket : buckets) {
            String key = bucket.getKey();
            Long count = bucket.getCount();
            hashMap.put(key,count);
        }
        return hashMap;
    }
}

package com.hua.store.api.solrj;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class SolrjTest {


    @Test
    public void addDocument() throws IOException, SolrServerException {

        //建立连接
        HttpSolrClient client = new HttpSolrClient("http://10.0.0.97:8080/solr");

        //创建文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "test000001");
        document.addField("item_title", "测试商品");
        document.addField("item_price", 11000);

        //把文档写入索引库
        client.add(document);
        //提交
        client.commit();

    }

    @Test
    public void updateDocument() throws IOException, SolrServerException {

        //建立连接
        HttpSolrClient client = new HttpSolrClient.Builder("http://10.0.0.97:8080/solr").build();

        //创建文档对象
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "test000001");
        document.addField("item_title", "测试修改商品");
        document.addField("item_price", 11001);

        //把文档写入索引库
        client.add(document);
        //提交
        client.commit();

    }

    @Test
    public void deleteDocument() throws IOException, SolrServerException {
        
        HttpSolrClient client = new HttpSolrClient.Builder("http://10.0.0.97:8080/solr").build();
        client.deleteByQuery("*:*");
        client.commit();

    }

    @Test
    public void queryDocument() throws IOException, SolrServerException {

        HttpSolrClient client = new HttpSolrClient.Builder("http://10.0.0.97:8080/solr").build();
        //client.deleteById("test000001");

        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");

        query.setStart(4);
        query.setRows(50);
        QueryResponse response = client.query(query);

        SolrDocumentList list = response.getResults();

        System.out.println(list.getNumFound());
        for(SolrDocument document : list){
            System.out.print(document.get("id") + " - ");
            System.out.print(document.get("item_title") + " - ");
            System.out.print(document.get("item_price") + " - ");
            System.out.println(document.get("item_sell_point"));
        }

    }

}

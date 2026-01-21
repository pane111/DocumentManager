package com.fhtw.ocrservice;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;

public class ElasticSearchConfig {

    @Bean
    public RestClient restClient() {
        return RestClient.builder(new HttpHost("elasticsearch", 9200, "http")).build();
    }
}

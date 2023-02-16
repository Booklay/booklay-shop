package com.nhnacademy.booklay.server.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Slf4j
@Configuration
@EnableElasticsearchRepositories // elasticsearch repository 허용
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration implements AutoCloseable {

    @Value("${booklay.elasticsearch.uri}")
    private String hostAndPort;

    @Override
    public RestHighLevelClient elasticsearchClient() {

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
            .connectedTo(hostAndPort)
            .withConnectTimeout(10000)
            .withSocketTimeout(30000)
            .build();

        try {
            return RestClients.create(clientConfiguration).rest();
        } catch (Exception e) {
            throw new ElasticsearchException(e.getMessage());
        }

    }

    @Override
    public void close() throws Exception {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
            .connectedTo(hostAndPort)
            .withConnectTimeout(10000)
            .withSocketTimeout(30000)
            .build();


        RestClients.create(clientConfiguration).close();

    }
}

package com.nhnacademy.booklay.server.config;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
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
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    @Value("${booklay.elasticsearch.uri}")
    private String hostAndPort;

    @Override
    public RestHighLevelClient elasticsearchClient() {

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
            .connectedTo(hostAndPort)
            .build();


        try (RestClients.ElasticsearchRestClient client = RestClients.create(clientConfiguration)) {

            return client.rest();

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

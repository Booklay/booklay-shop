package com.nhnacademy.booklay.server.config;

import com.nhnacademy.booklay.server.dto.secrets.DatasourceInfo;
import com.nhnacademy.booklay.server.dto.secrets.SecretResponse;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.Optional;

@Configuration
@Slf4j
public class DatabaseConfig {

    @Value("${booklay.secure.url}")
    private String url;

    @Value("${booklay.secure.db_username}")
    private String username;

    @Value("${booklay.secure.db_password}")
    private String password;

    @Value("${booklay.secure.db_url}")
    private String dbUrl;

    @Value("${booklay.pool_size}")
    private int poolSize;


    @Bean
    public DataSource dataSource() {

        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(datasourceInfo.getDbUrl());
        dataSource.setPassword(datasourceInfo.getPasswword());
        dataSource.setUsername(datasourceInfo.getUsername());


        return;
    }

    private DatasourceInfo getDatasourceInfo(RestTemplate restTemplate) {
        SecretResponse usernameResponse = restTemplate.getForObject(url + this.username, SecretResponse.class);
        SecretResponse passwordResponse = restTemplate.getForObject(url + this.password, SecretResponse.class);
        SecretResponse dbUrlResponse = restTemplate.getForObject(url + this.dbUrl, SecretResponse.class);


        return DatasourceInfo.builder()
                .passwword(passwordResponse.getBody().getSecret())
                .dbUrl(dbUrlResponse.getBody().getSecret())
                .username(usernameResponse.getBody().getSecret())
                .build();

    }

}

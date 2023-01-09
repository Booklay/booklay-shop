package com.nhnacademy.booklay.server.config;

import com.nhnacademy.booklay.server.dto.secrets.SecretResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DatabaseConfig {

    @Value("${booklay.secure.url}")
    private String url;

    @Value("${booklay.secure.db_username}")
    private String username;

    @Bean
    public DataSource dataSource(RestTemplate restTemplate) {

        SecretResponse usernameResponse = restTemplate.getForObject(url + this.username, SecretResponse.class);
        String username = usernameResponse.getBody().getSecret();

        log.info("=============");
        log.info(username);

        DataSource datasource = DataSourceBuilder.create()
                .url("jdbc:mysql://133.186.151.141:3306/booklay_shop?serverTimezone=Asia/Seoul")
                .password("_nys_6*0osSnt5Pa")
                .driverClassName("com.mysql.cj.jdbc.Driver")
                .username("booklay")
                .build();
        return datasource;
    }

}

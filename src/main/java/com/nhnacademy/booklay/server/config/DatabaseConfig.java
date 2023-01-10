package com.nhnacademy.booklay.server.config;

import com.nhnacademy.booklay.server.dto.secrets.SecretResponse;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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

    @Value("${booklay.secure.db_password}")
    private String password;

    @Value("${booklay.secure.db_url}")
    private String dbUrl;

    @Bean
    public DataSource dataSource(RestTemplate restTemplate) {

        SecretResponse usernameResponse = restTemplate.getForObject(url + this.username, SecretResponse.class);
        SecretResponse passwordResponse = restTemplate.getForObject(url + this.password, SecretResponse.class);
        SecretResponse dbUrlResponse = restTemplate.getForObject(url + this.dbUrl, SecretResponse.class);

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl(dbUrlResponse.getBody().getSecret());
        hikariConfig.setUsername(usernameResponse.getBody().getSecret());
        hikariConfig.setPassword(passwordResponse.getBody().getSecret());

        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        hikariDataSource.setMaximumPoolSize(2);

        return hikariDataSource;
    }

}

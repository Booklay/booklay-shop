package com.nhnacademy.booklay.server.config;

import com.nhnacademy.booklay.server.dto.secrets.DatasourceInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * Spring Boot의 기본 Datasource인 hikari를 DBCP2로 바꾸기 위한 설정 파일입니다.
 *
 * @author 조현진
 */
@Configuration
@Slf4j
public class DatabaseConfig {

    @Value("${booklay.pool_size}")
    private int poolSize;

    @Bean
    public DataSource dataSource(DatasourceInfo datasourceInfo) {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(datasourceInfo.getDbUrl());
        dataSource.setPassword(datasourceInfo.getPasswword());
        dataSource.setUsername(datasourceInfo.getUsername());
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMaxTotal(poolSize);
        dataSource.setInitialSize(poolSize);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager;
    }

}

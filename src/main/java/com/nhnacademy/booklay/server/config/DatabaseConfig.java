package com.nhnacademy.booklay.server.config;

import com.nhnacademy.booklay.server.dto.secrets.DatasourceInfo;
import com.p6spy.engine.spy.P6DataSource;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

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

        var dataSource = new BasicDataSource();
        dataSource.setUrl(datasourceInfo.getDbUrl());
        dataSource.setPassword(datasourceInfo.getPasswword());
        dataSource.setUsername(datasourceInfo.getUsername());
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setMaxTotal(poolSize);
        dataSource.setInitialSize(poolSize);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
        EntityManagerFactory entityManagerFactory) {

        var jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);

        return jpaTransactionManager;
    }

    @Bean
    public DataSource logDataSource(DataSource dataSource) {
        return new P6DataSource(dataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
        @Qualifier("logDataSource") DataSource dataSource) {
        var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.nhnacademy.booklay.server.entity");
        emf.setJpaVendorAdapter(jpaVendorAdapters());
        emf.setJpaProperties(jpaProperties());

        return emf;
    }

    private JpaVendorAdapter jpaVendorAdapters() {
        var hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);

        return hibernateJpaVendorAdapter;
    }

    private Properties jpaProperties() {
        var jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.show_sql", "false");
        jpaProperties.setProperty("hibernate.format_sql", "true");
        jpaProperties.setProperty("hibernate.use_sql_comments", "true");
        jpaProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
        jpaProperties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");

        return jpaProperties;
    }

}

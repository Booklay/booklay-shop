package com.nhnacademy.booklay.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableDiscoveryClient
public class BooklayShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BooklayShopApplication.class, args);
    }
}

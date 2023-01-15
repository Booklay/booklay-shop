package com.nhnacademy.booklay.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableBatchProcessing
@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
public class BooklayShopApplication {

  public static void main(String[] args) {
    SpringApplication.run(BooklayShopApplication.class, args);
  }

}

package com.example.bookstore.config;

import com.example.bookstore.errorhandler.DbErrorHandler;
import com.example.bookstore.errorhandler.PostgresDbErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaAuditing
public class PersistenceConfig {
  @Bean
  public DbErrorHandler dbErrorHandler() {
    return new PostgresDbErrorHandler();
  }
}
